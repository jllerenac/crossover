/**
 * 
 */
package com.crossover.techtrial.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;

/**
 * Person repository for basic operations on Person entity.
 * @author crossover
 */
@RestResource(exported=false)
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {
  Optional<Member> findById(Long id);
  Optional<Member> findByEmail(String email);
  List<Member> findAll();
  
  @Query("SELECT " +
          "new com.crossover.techtrial.dto.TopMemberDTO(t.member.id, t.member.name, t.member.email, COUNT(t.member.id)) " +
          "FROM Transaction t WHERE t.dateOfIssue BETWEEN ?1 AND ?2 AND " +
          "t.dateOfReturn BETWEEN ?1 AND ?2 " +
          "GROUP BY t.member.id, t.member.name, t.member.email " +
          "ORDER BY COUNT(t.member.id) DESC")
  List<TopMemberDTO> Top5Members(LocalDateTime startTime, LocalDateTime endTime);
  
}
