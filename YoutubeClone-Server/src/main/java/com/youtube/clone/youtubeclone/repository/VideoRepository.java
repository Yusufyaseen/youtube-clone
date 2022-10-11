package com.youtube.clone.youtubeclone.repository;

import com.youtube.clone.youtubeclone.model.User;
import com.youtube.clone.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends MongoRepository<Video, String> {
    Optional<List<Video>> findByUserId(String userId);
}
