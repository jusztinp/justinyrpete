package com.justiny.rpete.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    Long postId;
    String subredditName;
    String postName;
    String url;
    String description;
}