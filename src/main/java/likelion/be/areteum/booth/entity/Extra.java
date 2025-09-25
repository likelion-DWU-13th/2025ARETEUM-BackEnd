package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Extra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="booth_id", nullable=false)
    private Booth booth;

    @Column(nullable=false, length=255)
    private String content; // 예: "안주 2개 이상 주문 시 술 무제한"
}
