package com.youtube.clone.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;
    private String authorId;
    private String text;
    private AtomicInteger likeCount = new AtomicInteger(0);;
    private AtomicInteger disLikeCount = new AtomicInteger(0);;
}
