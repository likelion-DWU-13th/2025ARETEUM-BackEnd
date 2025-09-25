package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="booth_id", nullable=false)
    private Booth booth;

    @Column(nullable=false, length=100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private MenuCategory category;

    @Column(nullable=false)
    private Integer price;

    @Column(length=200)
    private String note; // 분홍색 안내 문구
}