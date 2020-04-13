package ru.skillbox.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.ProfileDto;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.services.ProfileService;

@RestController
@RequestMapping("/api/profile/my")
public class ProfileController {

    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> updateProfile(@RequestBody ProfileDto profileData) {

        return profileService.updateProfile(profileData);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> updateProfileWithPhoto(@RequestParam("photo") MultipartFile photo,
                                                                 @RequestParam("removePhoto") boolean removePhoto,
                                                                 @RequestParam("name") String name,
                                                                 @RequestParam("email") String email,
                                                                 @RequestParam(name = "password", required = false) String password) {

        return profileService.updateProfileWithPhoto(photo, removePhoto, name, email, password);
    }
}
