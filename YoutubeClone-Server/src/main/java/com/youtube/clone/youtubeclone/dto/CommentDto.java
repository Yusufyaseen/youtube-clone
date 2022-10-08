package com.youtube.clone.youtubeclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private String authorId;
    private String text;
    private Integer likeCount;
    private Integer disLikeCount;
}
