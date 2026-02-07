package com.uagrm.schedule_assignment.finance.service;

import com.uagrm.schedule_assignment.finance.dto.WorkingDayRequestDto;
import com.uagrm.schedule_assignment.finance.dto.WorkingDayResponseDto;
import com.uagrm.schedule_assignment.finance.entity.WorkingDay;
import com.uagrm.schedule_assignment.finance.mapper.WorkingDayMapper;
import com.uagrm.schedule_assignment.finance.repository.WorkingDayRepository;
import com.uagrm.schedule_assignment.security.entity.User;
import com.uagrm.schedule_assignment.security.repository.UserRepository;
import com.uagrm.schedule_assignment.security.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingDayService {

    private final WorkingDayRepository workingDayRepository;
    private final UserService userService;
    private final WorkingDayMapper workingDayMapper;
    private final UserRepository userRepository;


    @Transactional
    public WorkingDayResponseDto createWorkingDay(WorkingDayRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();

        WorkingDay workingDay = workingDayMapper.toEntity(requestDto);

        workingDay.setUser(userCurrent);

        return workingDayMapper.toDto(workingDayRepository.save(workingDay));
    }


    @Transactional
    public WorkingDayResponseDto updateWorkingDay(Long id, WorkingDayRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();
        WorkingDay workingDay = workingDayRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Working day not found"));

        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !workingDay.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to update this record.");

        workingDay.setDate(requestDto.date());
        workingDay.setStatus(requestDto.status());
        workingDay.setAmountWon(requestDto.amountWon());

        return workingDayMapper.toDto(workingDayRepository.save(workingDay));
    }


    @Transactional
    public Iterable<WorkingDayResponseDto> myWorkingDays() {
        User userCurrent = userService.getCurrentUser();
        return workingDayRepository.findAll().stream()
                .filter(workingDay -> workingDay.getUser().getId().equals(userCurrent.getId()))
                .map(workingDayMapper::toDto).toList();
    }


    @Transactional
    public WorkingDayResponseDto createWorkingDay(Long userId, WorkingDayRequestDto requestDto) {
        User userTarget = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkingDay workingDay = workingDayMapper.toEntity(requestDto);
        workingDay.setUser(userTarget);

        return workingDayMapper.toDto(workingDayRepository.save(workingDay));
    }

    @Transactional
    public WorkingDayResponseDto getWorkingDayById(Long id) {
        User userCurrent = userService.getCurrentUser();
        WorkingDay workingDay = workingDayRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Working day not found"));

        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !workingDay.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to view this record.");

        return workingDayMapper.toDto(workingDay);
    }

    @Transactional
    public Iterable<WorkingDayResponseDto> getAllWorkingDays() {
        return workingDayRepository.findAll().stream().map(workingDayMapper::toDto).toList();
    }

    @Transactional
    public void deleteWorkingDay(Long id) {
        User userCurrent = userService.getCurrentUser();
        WorkingDay workingDay = workingDayRepository.findById(id).orElseThrow(() -> new RuntimeException("Working day not found"));
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !workingDay.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to delete this record.");

        workingDayRepository.deleteById(id);
    }
}
