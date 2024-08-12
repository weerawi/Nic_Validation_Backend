package com.example.nicbackend.Repository;

import com.example.nicbackend.Entity.Nic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicRepository extends JpaRepository<Nic,Long> {
    Long countByGender(String gender);
}
