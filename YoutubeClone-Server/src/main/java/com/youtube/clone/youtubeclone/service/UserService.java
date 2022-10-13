package com.youtube.clone.youtubeclone.service;

import com.youtube.clone.youtubeclone.dto.SubscriberDto;
import com.youtube.clone.youtubeclone.dto.VideoDto;
import com.youtube.clone.youtubeclone.model.User;
import com.youtube.clone.youtubeclone.model.Video;
import com.youtube.clone.youtubeclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public boolean addToVideoHistory(String videoId, String userId) {
        User user = getCurrentUser();
        boolean found = user.getSubscribedToUsers().contains(userId);
        user.addToVideoHistory(videoId);
        userRepository.save(user);
        return found;
    }

    public void subscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUsers(userId);
        System.out.println(currentUser.getFullName());
        User user = getUserById(userId);
        user.addToSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public void unSubscribeUser(String userId) {
        User currentUser = getCurrentUser();
        System.out.println(currentUser.getSubscribedToUsers());
        System.out.println(currentUser.getSubscribers());
        currentUser.removeFromSubscribedToUsers(userId);

        User user = getUserById(userId);
        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }
    public User getUserById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User is not found.!"));
    }



    public List<SubscriberDto> getSubscriptions() {
        User user = getCurrentUser();
        return user.getSubscribedToUsers().stream().map(this::mapToSubscriberDto).collect(Collectors.toList());
    }
    private SubscriberDto mapToSubscriberDto(String id){
        User user = getUserById(id);
        SubscriberDto subscriberDto = SubscriberDto.builder().userId(id)
                .name(user.getFirstName() + " " + user.getLastName())
                .photo(user.getPhoto())
                .build();
        return subscriberDto;
    }


}
