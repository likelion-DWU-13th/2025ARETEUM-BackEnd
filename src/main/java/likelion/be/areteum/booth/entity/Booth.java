package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "detail_location", length = 120)
    private String detailLocation;       // 상세위치

    @Column(length=120)
    private String organizer;            // 주최/담당

    @Column(length=255)
    private String mapImageUrl;          // 지도 이미지 URL

    @OneToMany(mappedBy="booth", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<BoothSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy="booth", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Menu> menus = new ArrayList<>(); // 주점일 경우만 사용

    @OneToMany(mappedBy="booth", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<SetMenu> setMenus = new ArrayList<>();

    @OneToMany(mappedBy="booth", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Product> products = new ArrayList<>(); // 마켓일 경우만 사용

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
