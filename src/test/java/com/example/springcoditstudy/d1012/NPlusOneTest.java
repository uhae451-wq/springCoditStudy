package com.example.springcoditstudy.d1012;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springcoditstudy.d1012.entity.MemberD1012;
import com.example.springcoditstudy.d1012.entity.TeamD1012;
import com.example.springcoditstudy.d1012.repository.TeamRepositoryD1012;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// N+1 재현 및 해결 실습
// 쿼리 횟수는 Hibernate Statistics 로 센다. (SQL 로그와 같이 눈으로도 확인해볼 것)
@SpringBootTest
@Transactional
class NPlusOneTest {

  static final int TEAM_COUNT = 5;
  static final int MEMBER_PER_TEAM = 3;

  @PersistenceContext
  EntityManager em;

  @Autowired
  TeamRepositoryD1012 teamRepository;

  Statistics statistics;

  @BeforeEach
  void setUp() {
    // 팀 5개 x 멤버 3명
    for (int i = 1; i <= TEAM_COUNT; i++) {
      TeamD1012 team = new TeamD1012("팀" + i);
      em.persist(team);
      for (int j = 1; j <= MEMBER_PER_TEAM; j++) {
        em.persist(new MemberD1012("팀" + i + "-멤버" + j, team));
      }
    }

    // 쿼리를 DB로 보내고 영속성 컨텍스트를 비워야, 아래 조회가 진짜 DB 조회가 된다.
    // (안 비우면 1차 캐시에서 꺼내와서 N+1이 재현되지 않는다.)
    em.flush();
    em.clear();

    statistics = em.getEntityManagerFactory().unwrap(SessionFactory.class).getStatistics();
    // 통계는 기본이 꺼져 있다. 안 켜면 쿼리가 나가도 횟수가 항상 0 으로 나온다.
    statistics.setStatisticsEnabled(true);
    statistics.clear();
  }

  private long queryCount() {
    return statistics.getPrepareStatementCount();
  }

  @Test
  void findAll만_하면_쿼리_1번() {
    List<TeamD1012> teams = teamRepository.findAll();
    System.out.println("팀 수 : " + teams.size());

    // members 는 건드리지 않았으므로 LAZY 프록시 상태 그대로 -> 팀 조회 쿼리 1번뿐
    System.out.println("쿼리 수 : " + queryCount());
    assertThat(queryCount()).isEqualTo(1);
  }

  @Test
  void N_plus_1_발생() {
    List<TeamD1012> teams = teamRepository.findAll();            // 팀 조회 1번

    for (TeamD1012 team : teams) {
      System.out.println(team.getName() + " 멤버 수 : " + team.getMembers().size());  // 팀마다 쿼리 1번씩
    }

    System.out.println("쿼리 수 : " + queryCount());
    assertThat(queryCount()).isEqualTo(1 + TEAM_COUNT);          // 1 + N
  }

  @Test
  void fetch_join으로_해결() {
    List<TeamD1012> teams = teamRepository.findAllWithMembers();

    for (TeamD1012 team : teams) {
      System.out.println(team.getName() + " 멤버 수 : " + team.getMembers().size());
    }

    System.out.println("쿼리 수 : " + queryCount());
    assertThat(queryCount()).isEqualTo(1);
  }

  @Test
  void entityGraph로_해결() {
    List<TeamD1012> teams = teamRepository.findAllWithMembersByGraph();

    for (TeamD1012 team : teams) {
      System.out.println(team.getName() + " 멤버 수 : " + team.getMembers().size());
    }

    System.out.println("쿼리 수 : " + queryCount());
    assertThat(queryCount()).isEqualTo(1);
  }

  @Test
  void batch_size로_해결() {
    // 전역 설정(default_batch_fetch_size)을 쓰면 위의 N+1 테스트도 같이 바뀌므로,
    // 이 테스트에서만 세션에 직접 적용한다.
    em.unwrap(Session.class).setFetchBatchSize(100);

    List<TeamD1012> teams = teamRepository.findAll();            // 팀 조회 1번

    for (TeamD1012 team : teams) {
      System.out.println(team.getName() + " 멤버 수 : " + team.getMembers().size());
    }

    // 첫 팀의 멤버를 가져올 때 팀 id 들을 IN 으로 묶어 한 번에 조회 -> 총 2번
    System.out.println("쿼리 수 : " + queryCount());
    assertThat(queryCount()).isEqualTo(2);
  }

}
