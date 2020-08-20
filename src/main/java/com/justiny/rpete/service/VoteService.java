package com.justiny.rpete.service;

import com.justiny.rpete.dto.VoteDto;
import com.justiny.rpete.exceptions.PostNotFoundException;
import com.justiny.rpete.exceptions.RpeteException;
import com.justiny.rpete.model.Post;
import com.justiny.rpete.model.Vote;
import com.justiny.rpete.model.VoteType;
import com.justiny.rpete.repository.PostRepository;
import com.justiny.rpete.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private static final int INCREMENT = 1;

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) {
        Long postId = voteDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with ID " + postId));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (hasTheSameVoteAlready(voteDto, voteByPostAndUser)) {
            throw new RpeteException("You have already " + voteDto.getVoteType() + "'d this post!");
        }

        int voteCountDelta = VoteType.UPVOTE.equals(voteDto.getVoteType()) ? INCREMENT : -INCREMENT;
        post.setVoteCount(post.getVoteCount() + voteCountDelta);

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }


    private boolean hasTheSameVoteAlready(VoteDto voteDto, Optional<Vote> voteByPostAndUser) {
        return voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType());
    }
}
