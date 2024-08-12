package com.example.nicbackend.Service;

import com.example.nicbackend.Entity.Profile;
import com.example.nicbackend.Exceptions.UserException;
import com.example.nicbackend.Repository.ProfileRepository;
import com.example.nicbackend.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileRepository profileRepository;

    public Profile registerUser(Profile user) throws UserException {
        Optional<Profile> isEmailExist = profileRepository.findByEmail(user.getEmail());
        if(isEmailExist.isPresent()){
            throw new UserException("Email already exist");
        }
        Optional<Profile> isUsernameExist = profileRepository.findByUsername(user.getUsername());
        if(isUsernameExist.isPresent()){
            throw new UserException("Username already taken");
        }
        if(user.getEmail()==null || user.getPassword()==null  ){
            throw new UserException("all filed are required");
        }
        Profile newProfle = new Profile();
        newProfle.setEmail(user.getEmail());
        newProfle.setPassword(passwordEncoder.encode(user.getPassword()));

        newProfle.setUsername(user.getUsername());

        return profileRepository.save(newProfle);
    }


    public Profile updateUser(Profile updateUser, Profile PresentUser) throws UserException {
        if(updateUser.getEmail()!=null){
            PresentUser.setEmail(updateUser.getEmail());
        }

        if(updateUser.getPassword()!=null){
            PresentUser.setPassword(updateUser.getPassword());
        }
        if(updateUser.getUsername()!=null){
            PresentUser.setUsername(updateUser.getUsername());
        }
        if (updateUser.getUserId() == PresentUser.getUserId()){
            return profileRepository.save(PresentUser);
        }
        throw new UserException("you are not authorized to update this user");
    }


}
