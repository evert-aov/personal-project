package com.uagrm.personal.finance.service;

import com.uagrm.personal.finance.dto.UserWorkingDaySummaryDto;
import com.uagrm.personal.finance.dto.WorkingDayRequestDto;
import com.uagrm.personal.finance.dto.WorkingDayResponseDto;
import com.uagrm.personal.finance.entity.WorkingDay;
import com.uagrm.personal.finance.mapper.WorkingDayMapper;
import com.uagrm.personal.finance.repository.WorkingDayRepository;
import com.uagrm.personal.security.entity.User;
import com.uagrm.personal.security.repository.UserRepository;
import com.uagrm.personal.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WorkingDayService {

    private final WorkingDayRepository workingDayRepository;
    private final UserService userService;
    private final WorkingDayMapper workingDayMapper;
    private final UserRepository userRepository;
    private final AdvanceBalanceService advanceBalanceService;


    @Transactional
    public WorkingDayResponseDto createWorkingDay(WorkingDayRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();

        if (workingDayRepository.existsByUserIdAndDate(userCurrent.getId(), requestDto.date()))
            throw new RuntimeException("Ya registraste un día trabajado para la fecha " + requestDto.date());

        WorkingDay workingDay = workingDayMapper.toEntity(requestDto);
        workingDay.setUser(userCurrent);
        applyAvailableCredit(userCurrent, workingDay);

        return workingDayMapper.toDto(workingDayRepository.save(workingDay));
    }

    /**
     * Drains any advance credit the user already has (from an over-sized PAYMENT) into this
     * brand-new day, so a prior anticipo automatically covers work registered afterward.
     */
    private void applyAvailableCredit(User user, WorkingDay workingDay) {
        BigDecimal applied = advanceBalanceService.applyAvailableCredit(user, workingDay.getAmountWon());
        if (applied.compareTo(BigDecimal.ZERO) > 0)
            workingDay.setPaidAmount(applied);
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


    @Transactional(readOnly = true)
    public Iterable<WorkingDayResponseDto> myWorkingDays() {
        User userCurrent = userService.getCurrentUser();

        return workingDayRepository.findAllByUserId(userCurrent.getId())
                .stream()
                .map(workingDayMapper::toDto)
                .toList();
    }


    @Transactional
    public WorkingDayResponseDto createWorkingDay(Long userId, WorkingDayRequestDto requestDto) {
        User userTarget = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (workingDayRepository.existsByUserIdAndDate(userId, requestDto.date()))
            throw new RuntimeException("This user already has a working day registered for " + requestDto.date());

        WorkingDay workingDay = workingDayMapper.toEntity(requestDto);
        workingDay.setUser(userTarget);
        applyAvailableCredit(userTarget, workingDay);

        return workingDayMapper.toDto(workingDayRepository.save(workingDay));
    }

    @Transactional(readOnly = true)
    public WorkingDayResponseDto getWorkingDayById(Long id) {
        User userCurrent = userService.getCurrentUser();
        WorkingDay workingDay = workingDayRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Working day not found"));

        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !workingDay.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to view this record.");

        return workingDayMapper.toDto(workingDay);
    }

    @Transactional(readOnly = true)
    public Iterable<WorkingDayResponseDto> getAllWorkingDays() {
        return workingDayRepository.findAll().stream().map(workingDayMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Iterable<UserWorkingDaySummaryDto> getUserSummaries() {
        return workingDayRepository.findUserSummaries();
    }

    @Transactional(readOnly = true)
    public Iterable<WorkingDayResponseDto> getWorkingDaysByUser(Long userId) {
        return workingDayRepository.findAllByUserId(userId).stream().map(workingDayMapper::toDto).toList();
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

    @Transactional
    public BigDecimal totalDebt() {
        User userCurrent = userService.getCurrentUser();

        return workingDayRepository.calculateTotalDeb(userCurrent.getId());
    }
}
