package com.ditcherj.generator.viewer;

import com.ditcherj.generator.PlayerGenerator;
import com.ditcherj.generator.dto.Player;
import com.ditcherj.generator.viewer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by Jonathan Ditcher on 17/04/2017.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.ditcherj.generator.viewer")
public class WebAppInitializer{

    public static void main(String[] args) throws Exception {

        boolean populatePlayers = true;
        if(populatePlayers) {
            populatePlayers(1);
        }

        SpringApplication.run(WebAppInitializer.class, args);
    }

    private static void populatePlayers(int num) {
        PlayerGenerator generator = new PlayerGenerator();
        List<Player> players = generator.generatePlayers(num);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            players.forEach(player -> session.save(player));
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
