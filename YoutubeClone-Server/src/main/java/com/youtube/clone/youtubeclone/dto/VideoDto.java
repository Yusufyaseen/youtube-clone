package com.youtube.clone.youtubeclone.dto;

import com.youtube.clone.youtubeclone.model.Comment;
import com.youtube.clone.youtubeclone.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
    private String videoUrl;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
}
