package study.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Team;

/**
 * TeamRepository
 * 
 */

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    @PersistenceContext
    private final EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public Long count() {
        return em.createQuery("select count(t) from Team t ", Long.class).getSingleResult();
    }


}