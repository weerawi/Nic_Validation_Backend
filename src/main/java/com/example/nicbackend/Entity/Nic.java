package com.example.nicbackend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nic;
    private String birthday;
    private int age;
    private String gender;

}