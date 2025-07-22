package com.jjw.tboard.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(String memberId);

    @Query("select m from Member m where m.memberId = :memberId")
    Optional<Member> findByMemberId(@Param("memberId") String memberId);

}
