package com.uagrm.personal.finance.mapper;

import com.uagrm.personal.finance.dto.WorkingDayRequestDto;
import com.uagrm.personal.finance.dto.WorkingDayResponseDto;
import com.uagrm.personal.finance.entity.WorkingDay;
import org.springframework.stereotype.Component;

@Component
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
