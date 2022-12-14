package com.youtube.clone.youtubeclone.service;

import com.youtube.clone.youtubeclone.dto.CommentDto;
import com.youtube.clone.youtubeclone.dto.SubscriberDto;
import com.youtube.clone.youtubeclone.dto.UploadVideoResponse;
import com.youtube.clone.youtubeclone.dto.VideoDto;
import com.youtube.clone.youtubeclone.model.Comment;
import com.youtube.clone.youtubeclone.model.User;
import com.youtube.clone.youtubeclone.model.Video;
import com.youtube.clone.youtubeclone.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
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

        String pattern = "d MMM YYYY";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        video.setDate(date);

        video.setUserId(userService.getCurrentUser().getId());
        Video savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        Video savedVideo = getVideoById(videoDto.getId());
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        Video v = videoRepository.save(savedVideo);

        return videoDto;
    }

    public List<VideoDto> getVideosOfUser(String userId) {
        List<Video> videos = videoRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("No videos here"));
        return videos.stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    public Set<VideoDto> userHistory() {
        User user = userService.getCurrentUser();
        Set<VideoDto> videos = user.getViewHistory().stream().map((id) -> {
            Video video = getVideoById(id);
            return mapToVideoDto(video);
        }).collect(Collectors.toSet());
        return videos;
    }

    public String uploadVideoThumbnail(MultipartFile file, String videoId) {
        Video savedVideo = getVideoById(videoId);
        String thumbnailUrl = s3Service.uploadFile(file);
        savedVideo.setThumbnailUrl(thumbnailUrl);
        Video v = videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    private Video getVideoById(String id) {
        return videoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Can not find the video."));
    }

    public VideoDto getVideoDetails(String videoId) {
        Video video = getVideoById(videoId);
        incrementVideoCount(video);

        boolean found = userService.addToVideoHistory(videoId, video.getUserId());

        VideoDto videoDto = mapToVideoDto(video);
        videoDto.setSubscribed(found);
        return videoDto;
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

    private VideoDto mapToVideoDto(Video video) {
        User user = userService.getUserById(video.getUserId());
        VideoDto videoDto = VideoDto.builder().id(video.getId())
                .title(video.getTitle())
                .userId(video.getUserId())
                .description(video.getDescription())
                .tags(video.getTags())
                .videoStatus(video.getVideoStatus())
                .thumbnailUrl(video.getThumbnailUrl())
                .videoUrl(video.getVideoUrl())
                .likeCount(video.getLikes().get())
                .dislikeCount(video.getDisLikes().get())
                .viewCount(video.getViewCount().get())
                .date(video.getDate())
                .authorName(user.getFirstName() + " " + user.getLastName())
                .authorPhoto(user.getPhoto())
                .build();

        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        User user = userService.getCurrentUser();
        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        String pattern = "d MMM YYYY";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        comment.setAuthorId(user.getId());
        comment.setDate(date);
        comment.setText(commentDto.getText());
        video.addComment(comment);
        videoRepository.save(video);
    }

    public List<CommentDto> getCommentsOfAVideo(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentsList = video.getComments();
        List<CommentDto> comments = commentsList.stream().map(comment -> mapToCommentDto(comment)
        ).collect(Collectors.toList());
        return comments;
    }

    private CommentDto mapToCommentDto(Comment comment) {
        User user = userService.getUserById(comment.getAuthorId());
        CommentDto commentDto = CommentDto.builder()
                .authorName(user.getFirstName() + " " + user.getLastName())
                .authorPhoto(user.getPhoto())
                .text(comment.getText())
                .date(comment.getDate())
                .likeCount(comment.getLikeCount().get())
                .disLikeCount(comment.getDisLikeCount().get())
                .build();
        return commentDto;
    }


}
