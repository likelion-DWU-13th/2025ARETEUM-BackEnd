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

    @OneToMany(mappedBy="setMenu", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<SetMenuVariant> variants;
}
