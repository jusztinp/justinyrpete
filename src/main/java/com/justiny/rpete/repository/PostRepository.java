package com.justiny.rpete.repository;

import com.justiny.rpete.model.Post;
import com.justiny.rpete.model.Subreddit;
import com.justiny.rpete.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findAllByUser(User user);
}
