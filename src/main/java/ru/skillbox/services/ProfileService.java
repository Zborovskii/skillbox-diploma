package ru.skillbox.services;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.dto.ProfileDto;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.model.User;
import ru.skillbox.repository.UserRepository;

@Service
public class ProfileService {

    private AuthService authService;
    private StorageService storageService;
    private UserRepository userRepository;

    public ProfileService(AuthService authService, StorageService storageService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.storageService = storageService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResultResponse> updateProfile(ProfileDto profileData) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        ResultResponse result = new ResultResponse();
        result.setResult(updateProfileCommon(user, profileData));

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<ResultResponse> updateProfileWithPhoto(MultipartFile photo,
                                                                 Boolean removePhoto,
                                                                 String name,
                                                                 String email,
                                                                 String password) {

        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        String pathToSavedFile = storageService.store(photo);

        ProfileDto profileData = ProfileDto.builder()
            .photo(pathToSavedFile)
            .removePhoto(removePhoto)
            .name(name)
            .email(email)
            .password(password)
            .build();
        ResultResponse result = new ResultResponse();
        result.setResult(updateProfileCommon(user, profileData));

        return ResponseEntity.ok(result);
    }

    public Boolean updateProfileCommon(User user, ProfileDto profileDto) {

        String photo = profileDto.getPhoto();
        Boolean removePhoto = profileDto.isRemovePhoto();
        String name = profileDto.getName();
        String email = profileDto.getEmail();
        String password = profileDto.getPassword();

        if (photo != null && (!photo.isBlank() && !photo.equals(user.getPhoto()))) {
            user.setPhoto(photo);
        }

        if (removePhoto && (user.getPhoto() != null)) {
            storageService.delete(user.getPhoto());
            user.setPhoto(null);
        }

        if (!name.isBlank() && !name.equals(user.getName())) {
            user.setName(name);
        }

        if (!email.isBlank() && !email.equals(user.getEmail())) {
            user.setEmail(email);
        }

        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }

        User savedUser = userRepository.save(user);

        return user.getId().equals(savedUser.getId());
    }
}
