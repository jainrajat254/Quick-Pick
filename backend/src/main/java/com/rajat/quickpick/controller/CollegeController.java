package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.college.CreateCollegeDto;
import com.rajat.quickpick.dto.college.CollegeResponseDto;
import com.rajat.quickpick.service.CollegeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colleges")
@RequiredArgsConstructor
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    // public endpoints


    @GetMapping("/public/count")
    public ResponseEntity<Map<String, Long>> getCollegeCount() {
        long count = collegeService.getCollegeCount();
        return ResponseEntity.ok(Map.of("count", count));
    }


    @GetMapping("/public/search")
    public ResponseEntity<List<CollegeResponseDto>> searchColleges(@RequestParam String query) {
        List<CollegeResponseDto> colleges = collegeService.searchColleges(query);
        return ResponseEntity.ok(colleges);
    }


    @GetMapping("/public/cities")
    public ResponseEntity<List<String>> getAllCities() {
        List<String> cities = collegeService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/public/states")
    public ResponseEntity<List<String>> getAllStates() {
        List<String> states = collegeService.getAllStates();
        return ResponseEntity.ok(states);
    }

    @GetMapping("/public/city/{city}")
    public ResponseEntity<List<CollegeResponseDto>> getCollegesByCity(@PathVariable String city) {
        List<CollegeResponseDto> colleges = collegeService.getCollegesByCity(city);
        return ResponseEntity.ok(colleges);
    }

    @GetMapping("/public/state/{state}")
    public ResponseEntity<List<CollegeResponseDto>> getCollegesByState(@PathVariable String state) {
        List<CollegeResponseDto> colleges = collegeService.getCollegesByState(state);
        return ResponseEntity.ok(colleges);
    }


    @GetMapping("/public/{id}")
    public ResponseEntity<CollegeResponseDto> getCollegeById(@PathVariable String id) {
        CollegeResponseDto college = collegeService.getCollegeById(id);
        return ResponseEntity.ok(college);
    }


    @GetMapping("/public")
    public ResponseEntity<Page<CollegeResponseDto>> getAllColleges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CollegeResponseDto> colleges = collegeService.getColleges(page, size);
        return ResponseEntity.ok(colleges);
    }


    // for admin only
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollegeResponseDto> createCollege(@Valid @RequestBody CreateCollegeDto createDto) {
        CollegeResponseDto college = collegeService.createCollege(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(college);
    }


    //admin
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollegeResponseDto> updateCollege(@PathVariable String id,
                                                            @Valid @RequestBody CreateCollegeDto updateDto) {
        CollegeResponseDto updatedCollege = collegeService.updateCollege(id, updateDto);
        return ResponseEntity.ok(updatedCollege);
    }

    //admin
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCollege(@PathVariable String id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.ok(Map.of("message", "College deleted successfully"));
    }


}