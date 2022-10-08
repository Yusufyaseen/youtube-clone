package com.youtube.clone.youtubeclone.service;

import com.youtube.clone.youtubeclone.dto.CommentDto;
import com.youtube.clone.youtubeclone.dto.UploadVideoResponse;
import com.youtube.clone.youtubeclone.dto.VideoDto;
import com.youtube.clone.youtubeclone.model.Comment;
import com.youtube.clone.youtubeclone.model.Video;
import com.youtube.clone.youtubeclone.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {
    @Autowired
    private S3Service s3Service;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserService userService;

    public UploadVideoResponse uploadVideo(MultipartFile file) {
        String videoUrl = s3Service.uploadFile(file);
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        Video savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        Video savedVideo = getVideoById(videoDto.getId());
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadVideoThumbnail(MultipartFile file, String videoId) {
        Video savedVideo = getVideoById(videoId);
        String thumbnailUrl = s3Service.uploadFile(file);
        savedVideo.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    private Video getVideoById(String id) {
        return videoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Can not find the video."));
    }

    public VideoDto getVideoDetails(String videoId) {
        Video video = getVideoById(videoId);
        incrementVideoCount(video);

//        userService.addToVideoHistory(videoId);

        return mapToVideoDto(video);
    }

    private void incrementVideoCount(Video video) {
        video.incrementVideoCount();
        videoRepository.save(video);
    }

    public VideoDto likeVideo(String videoId) {
        Video video = getVideoById(videoId);

        if (userService.ifLikedVideo(videoId)) {
            video.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if (userService.ifDisLikedVideo(videoId)) {
            video.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else {
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
        }
        videoRepository.save(video);
        return mapToVideoDto(video);

    }

    public VideoDto disLikeVideo(String videoId) {
        Video video = getVideoById(videoId);

        if (userService.ifLikedVideo(videoId)) {
            video.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            video.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        } else if (userService.ifDisLikedVideo(videoId)) {
            video.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
        } else {
            video.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }
        videoRepository.save(video);
        return mapToVideoDto(video);

    }
    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }
    private VideoDto mapToVideoDto(Video video){
        VideoDto videoDto = VideoDto.builder().id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .tags(video.getTags())
                .videoStatus(video.getVideoStatus())
                .thumbnailUrl(video.getThumbnailUrl())
                .videoUrl(video.getVideoUrl())
                .likeCount(video.getLikes().get())
                .dislikeCount(video.getDisLikes().get())
                .viewCount(video.getViewCount().get())
                .build();

        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setAuthorId(commentDto.getAuthorId());
        comment.setText(commentDto.getText());
        video.addComment(comment);
        videoRepository.save(video);
    }

    public List<CommentDto> getCommentsOfAVideo(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentsList = video.getComments();
        List<CommentDto> comments = commentsList.stream().map(comment -> mapToCommentDto(comment)).collect(Collectors.toList());
        return comments;
    }
    private CommentDto mapToCommentDto(Comment comment){
        CommentDto commentDto = CommentDto.builder()
                .authorId(comment.getAuthorId())
                .text(comment.getText())
                .likeCount(comment.getLikeCount().get())
                .disLikeCount(comment.getDisLikeCount().get())
                .build();
        return commentDto;
    }


}
