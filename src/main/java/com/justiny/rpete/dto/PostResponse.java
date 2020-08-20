package com.justiny.rpete.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    Long id;
    String postName;
    String url;
    String description;
    String userName;
    String subredditName;
    Integer commentCount;
    Integer voteCount;
    String duration;
}
