package com.ditcherj.generator.viewer.contoller;


import com.ditcherj.generator.Progression;
import com.ditcherj.generator.SimpleDataCache;
import com.ditcherj.generator.Template;
import com.ditcherj.generator.dto.Player;
import com.ditcherj.generator.viewer.util.PositionDrawer;
import com.ditcherj.generator.viewer.service.PlayersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Jonathan Ditcher on 17/04/2017.
 */
@Controller
public class PlayersController {

    private static final Logger logger = LoggerFactory.getLogger(PlayersController.class);
    private static BufferedImage PITCH_IMG = null;
    static {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            PITCH_IMG = ImageIO.read(classLoader.getResourceAsStream("images/Pitch.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PlayersService playersService;

    @Autowired
    public void setPlayersService(PlayersService playersService) {
        this.playersService = playersService;
    }

    @ResponseBody
    @RequestMapping(
            value="/players",
            params = {"page", "size"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Page<Player> getPlayers(@RequestParam("page") int page, @RequestParam("size") int size) {
        logger.trace("");

        Page<Player> players = this.playersService.getPlayers(new PageRequest(page -1, size));

        players.forEach(p-> System.out.println(p));

        //if (page > players.getTotalPages()) {
       //     throw new MyResourceNotFoundException();
       // }

        return players;
    }

    @ResponseBody
    @RequestMapping(
            value="/player",
            params = {"id"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Player getPlayer(@RequestParam(name = "id") Long id) {
        logger.trace("");
        return this.playersService.getPlayer(id);
    }

    @ResponseBody
    @RequestMapping(
            value = "/position",
            params = {"id"},
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getPlayerPositionImage(@RequestParam(name = "id") Long id) throws IOException {
        logger.trace("");

        Player player = this.playersService.getPlayer(id);

        BufferedImage image = new PositionDrawer().draw(player.getPosition(), PITCH_IMG);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write( image  , "png", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    @ResponseBody
    @RequestMapping(
            value = "/progress",
            params = {"id"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Player progressPlayer(@RequestParam(name = "id") Long id) throws IOException {
        logger.trace("");

        final Player player = this.playersService.getPlayer(id);
        Template template = SimpleDataCache.getInstance().getTemplates().stream().filter(t-> t.getId().equals(player.getPrimaryTemplateId())).findFirst().orElse(null);

        double M = 1.2;
        double C = -0.6;

        Player progressedPlayer = Progression.calcYearProgression(player, player.getAge()+1, M, C, template);
        progressedPlayer.setAge(progressedPlayer.getAge() + 1);
        logger.trace("CurrentAbility[{}]", progressedPlayer.getCurrentAbility());
        logger.trace(progressedPlayer.toString());

        this.playersService.updatePlayer(progressedPlayer);
        return progressedPlayer;
    }
}
