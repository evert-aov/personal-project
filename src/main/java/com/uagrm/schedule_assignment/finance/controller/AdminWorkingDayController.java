package com.uagrm.schedule_assignment.finance.controller;

import com.uagrm.schedule_assignment.finance.dto.WorkingDayRequestDto;
import com.uagrm.schedule_assignment.finance.dto.WorkingDayResponseDto;
import com.uagrm.schedule_assignment.finance.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/finance/admin/working-days")
@RequiredArgsConstructor
public class AdminWorkingDayController {
    private final WorkingDayService workingDayService;

    @GetMapping
    public ResponseEntity<Iterable<WorkingDayResponseDto>> getAll() {
        return ResponseEntity.ok(workingDayService.getAllWorkingDays());
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<WorkingDayResponseDto> createForUser(
            @PathVariable Long userId,
            @RequestBody WorkingDayRequestDto requestDto
    ) {
        return new ResponseEntity<>(workingDayService.createWorkingDay(userId, requestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAny(@PathVariable Long id) {
        workingDayService.deleteWorkingDay(id);
        return ResponseEntity.noContent().build();
    }

}
