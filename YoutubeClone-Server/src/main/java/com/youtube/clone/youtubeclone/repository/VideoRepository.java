package com.youtube.clone.youtubeclone.repository;

import com.youtube.clone.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {
}
