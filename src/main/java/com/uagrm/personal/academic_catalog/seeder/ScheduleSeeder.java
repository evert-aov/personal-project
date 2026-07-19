package com.uagrm.personal.academic_catalog.seeder;

import com.uagrm.personal.academic_catalog.entity.Schedule;
import com.uagrm.personal.academic_catalog.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleSeeder implements CommandLineRunner {

    private final ScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (scheduleRepository.count() > 0) {
            System.out.println("Los horarios ya están creados. Omitiendo ScheduleSeeder...");
            return;
        }

        System.out.println("Ejecutando ScheduleSeeder: Creando bloques de 45 minutos...");

        List<Schedule> schedules = new ArrayList<>();
        LocalTime current = LocalTime.of(7, 0);
        LocalTime end = LocalTime.of(23, 0);

        while (current.isBefore(end)) {
            LocalTime next = current.plusMinutes(45);
            if (next.isAfter(end))
                next = end;

            schedules.add(Schedule.builder()
                    .startTime(current)
                    .endTime(next)
                    .build());

            current = next;
        }

        scheduleRepository.saveAll(schedules);

        System.out.println("¡ScheduleSeeder finalizado! " + schedules.size() + " bloques de horario creados.");
    }
}