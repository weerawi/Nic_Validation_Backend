package com.example.nicbackend.Controller;

import com.example.nicbackend.Entity.Profile;
import com.example.nicbackend.Exceptions.UserException;
import com.example.nicbackend.Repository.ProfileRepository;
import com.example.nicbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired
    private ProfileRepository ProfileRepository;


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Profile> registerUserHandler(@RequestBody Profile user) throws UserException {
        Profile newUser = userService.registerUser(user);
        return new ResponseEntity<Profile>(newUser, HttpStatus.CREATED);
    }



    @PostMapping("/signin")
    public ResponseEntity<Profile> signinUserHandler(Authentication auth) throws BadCredentialsException {

        Optional<Profile> opt = ProfileRepository.findByEmail(auth.getName());
        if(opt.isPresent()){
            Profile profile = opt.get();
            return new ResponseEntity<Profile>(opt.get(), HttpStatus.ACCEPTED);
        }
        throw new BadCredentialsException("Invalid username or password");

    }
}
