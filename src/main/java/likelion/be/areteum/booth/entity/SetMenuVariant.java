package likelion.be.areteum.booth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "set_menu_variant")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SetMenuVariant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="set_menu_id", nullable=false)
    private SetMenu setMenu;

    @Column(nullable=false)
    private Integer price;

    @Column(length=200)
    private String note; // "주문 시 맥주 2잔 증정"

    @ElementCollection
    @CollectionTable(name="set_menu_variant_items", joinColumns=@JoinColumn(name="variant_id"))
    @Column(name="item_name")
    private List<String> items = new ArrayList<>(); // ["타코야끼", "교자"]
}
