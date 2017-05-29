package com.ditcherj.generator.viewer.service;

import com.ditcherj.generator.SimpleDataCache;
import com.ditcherj.generator.dto.Player;
import com.ditcherj.generator.viewer.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by Jonathan Ditcher on 07/05/2017.
 */
@Service
public class PlayersServiceImpl implements PlayersService {

    @Override
    public Page<Player> getPlayers(Pageable pageable) {
        List<Player> players = null;
        Long count = 0L;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();

            System.out.println("ofset["+pageable.getOffset()+"] max["+pageable.getPageSize()+"]");
            CriteriaQuery<Player> criteria = builder.createQuery(Player.class);
            criteria.select(criteria.from(Player.class));
            players = session.createQuery(criteria)
                    .setFirstResult(pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
            countQuery.select(builder.count(countQuery.from(Player.class)));
            count = session.createQuery(countQuery).getSingleResult();

            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        players.forEach(player -> player.setNationality(SimpleDataCache.getInstance().getNationalities().getNationalities().get(player.getNationality().getIsoCode())));

        return new PageImpl<>(players, pageable, count);
    }

    @Override
    public Player getPlayer(Long id) {
        Player player = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            player = session.get(Player.class, id);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        player.setNationality(SimpleDataCache.getInstance().getNationalities().getNationalities().get(player.getNationality().getIsoCode()));

        return player;
    }

    @Override
    public void updatePlayer(Player player) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(player);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
