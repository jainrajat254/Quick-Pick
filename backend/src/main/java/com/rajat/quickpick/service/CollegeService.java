package com.rajat.quickpick.service;


import com.rajat.quickpick.dto.college.CitiesResponseDto;
import com.rajat.quickpick.dto.college.CreateCollegeDto;
import com.rajat.quickpick.dto.college.CollegeResponseDto;
import com.rajat.quickpick.dto.college.StatesResponseDto;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.College;
import com.rajat.quickpick.repository.CollegeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Slf4j
@Transactional
public class CollegeService {

    private static final Logger log = LoggerFactory.getLogger(CollegeService.class);
    @Autowired
    private CollegeRepository collegeRepository;

    public CollegeResponseDto createCollege(CreateCollegeDto createDto) {
        if (collegeRepository.existsByName(createDto.getName())) {
            throw new BadRequestException("College with name '" + createDto.getName() + "' already exists");
        } else {
            College college = new College();
            college.setName(createDto.getName().trim());
            college.setAddress(createDto.getAddress().trim());
            college.setCity(createDto.getCity().trim());
            college.setState(createDto.getState().trim());
            college.setCreatedAt(LocalDateTime.now());
            college.setUpdatedAt(LocalDateTime.now());

            College savedCollege = collegeRepository.save(college);
            log.info("College created successfully: {}", savedCollege.getName());

            return mapToResponseDto(savedCollege);
        }


    }

    public List<CollegeResponseDto> getAllColleges() {
        List<College> colleges = collegeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return colleges.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<CollegeResponseDto> getCollegesByCity(String city) {
        List<College> colleges = collegeRepository.findByCity(city);
        return colleges.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<CollegeResponseDto> getCollegesByState(String state) {
        List<College> colleges = collegeRepository.findByState(state);
        return colleges.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    public CollegeResponseDto getCollegeById(String id) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found with id: " + id));

        return mapToResponseDto(college);
    }

    public CollegeResponseDto getCollegeByName(String name) {
        College college = collegeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("College not found with name: " + name));

        return mapToResponseDto(college);
    }

    //add  queries
    public Page<CollegeResponseDto> getColleges(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<College> collegePage = collegeRepository.findAll(pageable);

        return collegePage.map(this::mapToResponseDto);
    }

    public List<CollegeResponseDto> searchColleges(String query) {
        List<College> colleges = collegeRepository.findByNameContainingIgnoreCase(query);
        return colleges.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    //admin only
    public CollegeResponseDto updateCollege(String id, CreateCollegeDto updateDto) {
        College existingCollege = collegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found with id: " + id));

        if (!existingCollege.getName().equals(updateDto.getName()) &&
                collegeRepository.existsByName(updateDto.getName())) {
            throw new BadRequestException("College with name '" + updateDto.getName() + "' already exists");
        }

        existingCollege.setName(updateDto.getName().trim());
        existingCollege.setAddress(updateDto.getAddress().trim());
        existingCollege.setCity(updateDto.getCity().trim());
        existingCollege.setState(updateDto.getState().trim());
        existingCollege.setUpdatedAt(LocalDateTime.now());

        College updatedCollege = collegeRepository.save(existingCollege);
        log.info("College updated successfully: {}", updatedCollege.getName());

        return mapToResponseDto(updatedCollege);
    }

    //for admin only
    public void deleteCollege(String id) {
        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found with id: " + id));
        collegeRepository.delete(college);
        log.info("College deleted successfully: {}", college.getName());
    }

    public long getCollegeCount() {
        return collegeRepository.count();
    }

    public CitiesResponseDto getAllCities() {
        List<String> cities = collegeRepository.findAll()
                .stream()
                .map(College::getCity)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        CitiesResponseDto dto = new CitiesResponseDto();
        dto.setCities(cities);
        return dto;
    }

    public StatesResponseDto getAllStates() {
        List<String> states = collegeRepository.findAll()
                .stream()
                .map(College::getState)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        StatesResponseDto dto = new StatesResponseDto();
        dto.setStates(states);
        return dto;
    }

    private CollegeResponseDto mapToResponseDto(College college) {
        CollegeResponseDto dto = new CollegeResponseDto();
        dto.setId(college.getId());
        dto.setName(college.getName());
        dto.setAddress(college.getAddress());
        dto.setCity(college.getCity());
        dto.setState(college.getState());
        return dto;
    }
}