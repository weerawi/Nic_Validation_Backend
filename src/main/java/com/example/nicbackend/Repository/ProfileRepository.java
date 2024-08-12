package com.example.nicbackend.Repository;

import com.example.nicbackend.Entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    public Optional<Profile> findByUserId(long id);
    public Optional<Profile> findByUsername(String username);
    public Optional<Profile> findByEmail(String email);

}
