package com.justiny.rpete.service;

import com.justiny.rpete.dto.CommentDto;
import com.justiny.rpete.exceptions.PostNotFoundException;
import com.justiny.rpete.mapper.CommentMapper;
import com.justiny.rpete.model.Comment;
import com.justiny.rpete.model.NotificationEmail;
import com.justiny.rpete.model.Post;
import com.justiny.rpete.model.User;
import com.justiny.rpete.repository.CommentRepository;
import com.justiny.rpete.repository.PostRepository;
import com.justiny.rpete.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;

    public void save(CommentDto commentDto) {
        Long postId = commentDto.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        User user = authService.getCurrentUser();
        Comment comment = commentMapper.map(commentDto, post, user);
        commentRepository.save(comment);

        String message = comment.getUser().getUsername() + " posted a comment on your post. " + POST_URL;
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post", user.getEmail(), message));
    }

    public List<CommentDto> getAllCommentsForPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));

        return commentRepository.findByPost(post).stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return commentRepository.findAllByUser(user).stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
