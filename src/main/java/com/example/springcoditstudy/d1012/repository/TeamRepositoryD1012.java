package com.example.springcoditstudy.d1012.repository;

import com.example.springcoditstudy.d1012.entity.Team1012;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepositoryD1012 extends JpaRepository<Team1012, Long> {

  // fetch join : 팀과 멤버를 JOIN 한 번으로 같이 가져온다.
  @Query("select distinct t from Team1012 t join fetch t.members")
  List<Team1012> findAllWithMembers();

  // EntityGraph : 애너테이션만으로 fetch join 과 같은 효과 (LEFT OUTER JOIN)
  @EntityGraph(attributePaths = "members")
  @Query("select t from Team1012 t")
  List<Team1012> findAllWithMembersByGraph();

}
