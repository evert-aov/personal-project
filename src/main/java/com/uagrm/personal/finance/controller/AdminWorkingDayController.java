package com.uagrm.personal.finance.controller;

import com.uagrm.personal.finance.dto.UserWorkingDaySummaryDto;
import com.uagrm.personal.finance.dto.WorkingDayRequestDto;
import com.uagrm.personal.finance.dto.WorkingDayResponseDto;
import com.uagrm.personal.finance.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/admin/working-days")
@RequiredArgsConstructor
public class AdminWorkingDayController {
    private final WorkingDayService workingDayService;

    @GetMapping
    public ResponseEntity<Iterable<WorkingDayResponseDto>> getAll() {
        return ResponseEntity.ok(workingDayService.getAllWorkingDays());
    }

    @GetMapping("/summary")
    public ResponseEntity<Iterable<UserWorkingDaySummaryDto>> getSummary() {
        return ResponseEntity.ok(workingDayService.getUserSummaries());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<WorkingDayResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(workingDayService.getWorkingDaysByUser(userId));
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
