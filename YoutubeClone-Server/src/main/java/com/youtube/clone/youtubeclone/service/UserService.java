package com.youtube.clone.youtubeclone.service;

import com.youtube.clone.youtubeclone.model.User;
import com.youtube.clone.youtubeclone.model.Video;
import com.youtube.clone.youtubeclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        String sub = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("sub");
        return userRepository.findBySub(sub).orElseThrow(() -> new IllegalArgumentException("Can not find user"));
    }



    public boolean ifLikedVideo(String videoId) {
        return getCurrentUser().getLikedVideos().stream().anyMatch((id) -> videoId.equals(id));
    }

    public boolean ifDisLikedVideo(String videoId) {
        return getCurrentUser().getDisLikedVideos().stream().anyMatch((id) -> videoId.equals(id));
    }

    public void addToLikedVideos(String videoId) {
        User user = getCurrentUser();
        user.addToLikedVideos(videoId);
        userRepository.save(user);
    }
    public void addToDisLikedVideos(String videoId) {
        User user = getCurrentUser();
        user.addToDisLikedVideos(videoId);
        userRepository.save(user);
    }
    public void removeFromLikedVideos(String videoId) {
        User user = getCurrentUser();
        user.removeFromLikedVideos(videoId);
        userRepository.save(user);
    }

    public void removeFromDisLikedVideos(String videoId) {
        User user = getCurrentUser();
        user.removeFromDisLikedVideos(videoId);
        userRepository.save(user);
    }

    public void addToVideoHistory(String videoId) {
        User user = getCurrentUser();
        user.addToVideoHistory(videoId);
        userRepository.save(user);
    }

    public boolean ifSubscribedToUser(String userId) {
        return getCurrentUser().getSubscribedToUsers().stream().anyMatch((id) -> userId.equals(id));
    }
    public void subscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);
        System.out.println(currentUser.getFullName());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User is not found.!"));
        user.addToSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public void unSubscribeUser(String userId) {
        User currentUser = getCurrentUser();
        System.out.println(currentUser.getSubscribedToUsers());
        System.out.println(currentUser.getSubscribers());
        currentUser.removeFromSubscribedToUsers(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User is not found.!"));
        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public Set<String> userHistory() {
        User user = getCurrentUser();
        return user.getViewHistory();
    }
}
