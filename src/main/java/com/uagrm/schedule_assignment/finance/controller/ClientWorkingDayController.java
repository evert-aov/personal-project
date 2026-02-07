package com.uagrm.schedule_assignment.finance.controller;

import com.uagrm.schedule_assignment.finance.dto.WorkingDayRequestDto;
import com.uagrm.schedule_assignment.finance.dto.WorkingDayResponseDto;
import com.uagrm.schedule_assignment.finance.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/client/working-days")
@RequiredArgsConstructor
public class ClientWorkingDayController {

    private final WorkingDayService workingDayService;

    @PostMapping
    public ResponseEntity<WorkingDayResponseDto> createWorkingDay(@RequestBody WorkingDayRequestDto requestDto) {
        return new ResponseEntity<>(workingDayService.createWorkingDay(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkingDayResponseDto> getWorkingDayById(@PathVariable Long id) {
        return ResponseEntity.ok(workingDayService.getWorkingDayById(id));
    }

    @GetMapping("/my-working-days")
    public ResponseEntity<Iterable<WorkingDayResponseDto>> myWorkingDays() {
        return ResponseEntity.ok(workingDayService.myWorkingDays());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkingDayResponseDto> updateWorkingDay(@PathVariable Long id, @RequestBody WorkingDayRequestDto requestDto) {
        return ResponseEntity.ok(workingDayService.updateWorkingDay(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkingDay(@PathVariable Long id) {
        workingDayService.deleteWorkingDay(id);
        return ResponseEntity.noContent().build();
    }
}
