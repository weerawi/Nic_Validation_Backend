package com.example.nicbackend.Service;

import com.example.nicbackend.Dto.DashboardStats;
import com.example.nicbackend.Entity.Nic;
import com.example.nicbackend.Repository.NicRepository;
import com.example.nicbackend.Repository.ProfileRepository;
import com.example.nicbackend.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NicService {

    @Autowired
    private NicRepository nicRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ProfileRepository profileRepository;

    public List<Nic> processAndValidateNICFiles(MultipartFile[] files) throws Exception {
        List<Nic> nicDetailsList = new ArrayList<>();

        for (MultipartFile file : files) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String nicNumber = line.trim();
                    Nic nic = validateAndExtractNICDetails(nicNumber);
                    nicDetailsList.add(nic);
                }
            }
        }

        return nicDetailsList;
    }


    public void saveValidatedNics(List<Nic> nics) {
        nicRepository.saveAll(nics);
    }

    public List<Nic> getAllNics() {
        return nicRepository.findAll();
    }

    private Nic validateAndExtractNICDetails(String nicNumber ) {
        // Example validation logic and extraction (adjust based on NIC format)
        if (nicNumber.length() != 10 && nicNumber.length() != 12) {
            throw new IllegalArgumentException("Invalid NIC number.");
        }

        String birthday = extractBirthdayFromNIC(nicNumber);
        int age = calculateAge(birthday);
        String gender = extractGenderFromNIC(nicNumber);

        Nic nic = new Nic();
        nic.setNic(nicNumber);
        nic.setBirthday(birthday);
        nic.setAge(age);
        nic.setGender(gender);

        return nicRepository.save(nic);
    }

    private String extractBirthdayFromNIC(String nicNumber) {
        String yearPart;
        int days;

        if (nicNumber.length() == 10) {
            yearPart = "19" + nicNumber.substring(0, 2);
            days = Integer.parseInt(nicNumber.substring(2, 5));
        } else if (nicNumber.length() == 12) {
            yearPart = nicNumber.substring(0, 4);
            days = Integer.parseInt(nicNumber.substring(4, 7));
        } else {
            throw new IllegalArgumentException("Invalid NIC length.");
        }

        // Adjust day value for female NICs
        if (days > 500) {
            days -= 500;
        }

        // Validate day value
        if (days < 1 || days > 366) {
            throw new IllegalArgumentException("Invalid day value extracted from NIC: " + days);
        }

        LocalDate birthday = LocalDate.ofYearDay(Integer.parseInt(yearPart), days);
        return birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    private int calculateAge(String birthday) {
        LocalDate birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private String extractGenderFromNIC(String nicNumber) {
        // Implement logic to extract gender from NIC number
        int days = Integer.parseInt(nicNumber.substring(nicNumber.length() - 7, nicNumber.length() - 4));
        return days > 500 ? "Female" : "Male";
    }


    public DashboardStats getDashboardStats() {
        long totalRecords = nicRepository.count();
        long maleCount = nicRepository.countByGender("Male");
        long femaleCount = nicRepository.countByGender("Female");

        return new DashboardStats(totalRecords, maleCount, femaleCount);
    }

}
