package likelion.be.areteum.booth.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record BoothScheduleCreateReq(
        @NotNull LocalDate eventDate,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime
) {}
