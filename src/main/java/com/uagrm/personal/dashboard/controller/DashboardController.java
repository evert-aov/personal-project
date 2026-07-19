package com.uagrm.personal.dashboard.controller;

import com.uagrm.personal.dashboard.dto.AdminUserWorkSummaryDto;
import com.uagrm.personal.dashboard.dto.PersonalDashboardDto;
import com.uagrm.personal.dashboard.service.DashboardService;
import com.uagrm.personal.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final UserService userService;

    @GetMapping("/personal/summary")
    public ResponseEntity<PersonalDashboardDto> getPersonalSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(dashboardService.getPersonalSummary(userService.getCurrentUser(), from, to));
    }

    @GetMapping("/admin/summary")
    public ResponseEntity<Iterable<AdminUserWorkSummaryDto>> getAdminSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(dashboardService.getAdminWorkSummaries(from, to));
    }
}
