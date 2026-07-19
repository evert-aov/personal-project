package com.uagrm.personal.academic_catalog.service;

import com.uagrm.personal.academic_catalog.dto.ScheduleRequestDto;
import com.uagrm.personal.academic_catalog.dto.ScheduleResponseDto;
import com.uagrm.personal.academic_catalog.entity.Schedule;
import com.uagrm.personal.academic_catalog.mapper.ScheduleMapper;
import com.uagrm.personal.academic_catalog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        return scheduleMapper.toDto(scheduleRepository.save(scheduleMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public Iterable<ScheduleResponseDto> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(scheduleMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long id) {
        return scheduleMapper.toDto(scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found")));
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setStartTime(requestDto.startTime());
        schedule.setEndTime(requestDto.endTime());
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
