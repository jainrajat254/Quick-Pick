package com.rajat.quickpick.controller;


import com.rajat.quickpick.dto.college.CreateCollegeDto;
import com.rajat.quickpick.dto.college.CollegeResponseDto;
import com.rajat.quickpick.dto.college.CollegesResponseDto;
import com.rajat.quickpick.dto.college.CitiesResponseDto;
import com.rajat.quickpick.dto.college.StatesResponseDto;
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



    @GetMapping("/public/count")
    public ResponseEntity<Map<String, Long>> getCollegeCount() {
        long count = collegeService.getCollegeCount();
        return ResponseEntity.ok(Map.of("count", count));
    }


    @GetMapping("/public/search")
    public ResponseEntity<CollegesResponseDto> searchColleges(@RequestParam String query) {
        CollegesResponseDto response = collegeService.searchColleges(query);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/public/cities")
    public ResponseEntity<CitiesResponseDto> getAllCities() {
        CitiesResponseDto response = collegeService.getAllCities();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/states")
    public ResponseEntity<StatesResponseDto> getAllStates() {
        StatesResponseDto response = collegeService.getAllStates();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/city/{city}")
    public ResponseEntity<CollegesResponseDto> getCollegesByCity(@PathVariable String city) {
        CollegesResponseDto response = collegeService.getCollegesByCity(city);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/state/{state}")
    public ResponseEntity<CollegesResponseDto> getCollegesByState(@PathVariable String state) {
        CollegesResponseDto response = collegeService.getCollegesByState(state);
        return ResponseEntity.ok(response);
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