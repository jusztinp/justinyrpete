package com.justiny.rpete.repository;


import com.justiny.rpete.model.Post;
import com.justiny.rpete.model.User;
import com.justiny.rpete.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
