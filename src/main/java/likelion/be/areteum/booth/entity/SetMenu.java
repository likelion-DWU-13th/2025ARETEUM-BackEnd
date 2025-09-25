package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "set_menu")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SetMenu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="booth_id", nullable=false)
    private Booth booth;

    @Column(nullable=false, length=100)
    private String name;  // "맥주set", "하이볼set"

    @Column(nullable=false)
    private Integer price;

    @Column(length=200)
    private String benefit; // 예: "주문 시 맥주 2잔 증정"

    @ElementCollection
    @CollectionTable(name="set_menu_items", joinColumns=@JoinColumn(name="set_menu_id"))
    @Column(name="item_name")
    private List<String> items; // 세트에 포함된 메뉴 이름
}
