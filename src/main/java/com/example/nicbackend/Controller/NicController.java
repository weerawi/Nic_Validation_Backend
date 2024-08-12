package com.example.nicbackend.Controller;

import com.example.nicbackend.Dto.DashboardStats;
import com.example.nicbackend.Entity.Nic;
import com.example.nicbackend.Security.JwtTokenProvider;
import com.example.nicbackend.Service.NicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nic")
@CrossOrigin(origins = "http://localhost:3000")
public class NicController {

    @Autowired
    private NicService nicService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("file1") MultipartFile file1,
                                         @RequestParam("file2") MultipartFile file2,
                                         @RequestParam("file3") MultipartFile file3,
                                         @RequestParam("file4") MultipartFile file4,
                                         HttpServletRequest request) {
        // Extract the JWT token from the Authorization header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
        }

        String token = header.substring(7).trim(); // Remove "Bearer " and trim any extra spaces

        try {
            // Process and validate NIC files
            MultipartFile[] files = {file1, file2, file3, file4};
            List<Nic> nicList = nicService.processAndValidateNICFiles(files);

            // Save validated NICs to the database
            nicService.saveValidatedNics(nicList);

            // Return response
            return ResponseEntity.ok(nicList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing files: " + e.getMessage());
        }
    }




    @GetMapping("/all")
    public ResponseEntity<List<Nic>> getAllNics() {
        try {
            // Retrieve all NICs from the database
            List<Nic> nics = nicService.getAllNics();
            return ResponseEntity.ok(nics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        try {
            DashboardStats stats = nicService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
