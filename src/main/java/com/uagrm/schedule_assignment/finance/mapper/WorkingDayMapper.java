package com.uagrm.schedule_assignment.finance.mapper;

import com.uagrm.schedule_assignment.finance.dto.WorkingDayRequestDto;
import com.uagrm.schedule_assignment.finance.dto.WorkingDayResponseDto;
import com.uagrm.schedule_assignment.finance.entity.WorkingDay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkingDayMapper {

    public WorkingDay toEntity(WorkingDayRequestDto request) {
        if (request == null) return null;

        return WorkingDay.builder()
                .date(request.date())
                .status(request.status())
                .amountWon(request.amountWon())
                .build();
    }

    public WorkingDayResponseDto toDto(WorkingDay workingDay) {
        return new WorkingDayResponseDto(
                workingDay.getId(),
                workingDay.getUser().getId(),
                workingDay.getDate(),
                workingDay.getStatus(),
                workingDay.getAmountWon(),
                workingDay.getPaidAmount()
        );
    }
}
