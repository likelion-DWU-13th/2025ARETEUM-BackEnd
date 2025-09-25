package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "booth_schedule",
        indexes = { @Index(name="idx_schedule_date", columnList="event_date") },
        uniqueConstraints = @UniqueConstraint(
                name = "uk_booth_date_time",
                columnNames = {"booth_id","event_date","start_time","end_time"}
        )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BoothSchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="booth_id", nullable=false)
    private Booth booth;

    @Column(name="event_date", nullable=false)
    private LocalDate eventDate;

    @Column(name="start_time", nullable=false)
    private LocalTime startTime;

    @Column(name="end_time", nullable=false)
    private LocalTime endTime;
}
