package com.ditcherj.generator.viewer.service;

import com.ditcherj.generator.dto.Player;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Jonathan Ditcher on 07/05/2017.
 */
public interface PlayersService {

    @Cacheable("players")
    Page<Player> getPlayers(Pageable pageable);
    Player getPlayer(Long id);
    void updatePlayer(Player player);
}
