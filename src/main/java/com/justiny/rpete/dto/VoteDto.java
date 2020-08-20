package com.justiny.rpete.dto;

import com.justiny.rpete.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    VoteType voteType;
    Long postId;
}
