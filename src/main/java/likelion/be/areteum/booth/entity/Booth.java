package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booth")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Booth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true, length=150) // 이름 중복 방지 권장
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private SubCategory subCategory = SubCategory.NONE;

    @Column(length=500)
    private String description;          // 부스 소개

    @Column(length=60)
    private String location;             // 장소

    @Column(length=120)
    private String organizer;            // 주최/담당

    @Column(length=255)
    private String mapImageUrl;          // 지도 이미지 URL

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist void prePersist() {
        var now = LocalDateTime.now();
        createdAt = now; updatedAt = now;
        if (subCategory == null) subCategory = SubCategory.NONE;
    }
    @PreUpdate void preUpdate() { updatedAt = LocalDateTime.now(); }
}
