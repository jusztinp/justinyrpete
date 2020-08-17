package com.justiny.rpete.repository;

import com.justiny.rpete.model.Comment;
import com.justiny.rpete.model.Post;
import com.justiny.rpete.model.Redditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByRedditor(Redditor redditor);
}
