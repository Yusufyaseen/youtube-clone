package com.youtube.clone.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value = "User")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String sub;
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    @Indexed(unique = true)
    private String emailAddress;
    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet();
    private Set<String> subscribers = ConcurrentHashMap.newKeySet();
    private Set<String> viewHistory = ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos = ConcurrentHashMap.newKeySet(); // ensures set with a thread safe
    private Set<String> disLikedVideos = ConcurrentHashMap.newKeySet();

    public void addToLikedVideos(String videoId){
        this.likedVideos.add(videoId);
    }
    public void removeFromLikedVideos(String videoId) {
        this.likedVideos.remove(videoId);
    }
    public void addToDisLikedVideos(String videoId){
        this.disLikedVideos.add(videoId);
    }
    public void removeFromDisLikedVideos(String videoId) {
        this.disLikedVideos.remove(videoId);
    }

    public void addToVideoHistory(String videoId){
        this.viewHistory.add(videoId);
    }

    public void addToSubscribedToUsers(String userId){
        this.subscribedToUsers.add(userId);
    }
    public void removeFromSubscribedToUsers(String userId){
        this.subscribedToUsers.remove(userId);
    }

    public void addToSubscribers(String userId){
        this.subscribers.add(userId);
    }
    public void removeFromSubscribers(String userId){
        this.subscribers.remove(userId);
    }
}
