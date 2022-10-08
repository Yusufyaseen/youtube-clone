package com.youtube.clone.youtubeclone.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youtube.clone.youtubeclone.dto.UserInfoDto;
import com.youtube.clone.youtubeclone.model.User;
import com.youtube.clone.youtubeclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class UserRegistrationService {

    @Value("${spring.userinfoEndpoint}")
    private String userinfoEndpoint;

    @Autowired
    private UserRepository userRepository;

    public String registerUser(String tokenValue) {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userinfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = httpResponse.body();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDto userInfoDto = objectMapper.readValue(body, UserInfoDto.class);

            Optional<User> userBySubject = userRepository.findBySub(userInfoDto.getSub());
            if(userBySubject.isPresent()){
                return userBySubject.get().getId();
            }
            else {
                User user = User.builder().firstName(userInfoDto.getGivenName())
                        .lastName(userInfoDto.getFamilyName())
                        .fullName(userInfoDto.getName())
                        .emailAddress(userInfoDto.getEmail())
                        .sub(userInfoDto.getSub())
                        .build();

                return userRepository.save(user).getId();
            }

        } catch (Exception exception) {
            throw new RuntimeException("An exception occurs while registering the user.");
        }
    }
}
