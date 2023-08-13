package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    //팀 등록
    public Team save(Team team)
    {
        em.persist(team);
        return team;
    }

    //팀 삭제
    public void delete(Team team)
    {
        em.remove(team);
    }

    //모든 팀 조회
    public List<Team> findAll(){
        return em.createQuery("select t from Team t",Team.class)
                .getResultList();
    }

    //id로 팀 단건 조회
    public Optional<Team> findById(Long id)
    {
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team); //회원이 없으면 null값을 옵셔널로 감싸서 나감
    }

    //전체 팀 숫자 구하기
    public Long count(){
        return em.createQuery("select count(t) from Team t",Long.class)
                .getSingleResult();
    }
}
