package com.justiny.rpete.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Long id;
    Long postId;
    Instant createdDate;
    String text;
    String username;
}
