package likelion.be.areteum.booth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum MenuCategory {
    ANJU("안주"),     // 주점 안주류
    DRINK("음료");    // 주점 음료류

    private final String label;
    MenuCategory(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    private static final Map<String, MenuCategory> LABEL_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(MenuCategory::getLabel, e -> e));

    @JsonCreator
    public static MenuCategory from(String label) {
        MenuCategory category = LABEL_MAP.get(label);
        if (category == null) {
            throw new IllegalArgumentException("Unknown MenuCategory label: " + label);
        }
        return category;
    }
}
