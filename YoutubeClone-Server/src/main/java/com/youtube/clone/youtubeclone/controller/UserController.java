package com.youtube.clone.youtubeclone.controller;

import com.youtube.clone.youtubeclone.dto.SubscriberDto;
import com.youtube.clone.youtubeclone.dto.VideoDto;
import com.youtube.clone.youtubeclone.service.UserRegistrationService;
import com.youtube.clone.youtubeclone.service.UserService;
import com.youtube.clone.youtubeclone.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String id = userRegistrationService.registerUser(jwt.getTokenValue());
        System.out.println("----------------------"+id);
        return id;
    }

    @PostMapping("/subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser(@PathVariable String userId){
        System.out.println("---");
        userService.subscribeUser(userId);
        return true;
    }
    @PostMapping("/unsubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean unSubscribeUser(@PathVariable String userId){
        userService.unSubscribeUser(userId);
        return true;
    }
    @GetMapping("/get-subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public List<SubscriberDto> getSubscriptions(){
        return userService.getSubscriptions();
    }
    @GetMapping("/get-videos-of-user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getVideosOfUser(@PathVariable String userId){
        return videoService.getVideosOfUser(userId);
    }

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<VideoDto> getUserHistory(){
        return videoService.userHistory();
    }

}
