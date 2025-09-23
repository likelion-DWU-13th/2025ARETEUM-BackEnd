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
    ANJU("안주"),
    DRINK("음료"),
    ALCOHOL("주류"),   // ← 추가
    EVENT("이벤트");   // ← 추가

    private final String label;

    // JSON 직렬화 시 "안주/음료/주류/이벤트" 문자열로 나가게
    @Override
    @JsonValue
    public String toString() {
        return label;
    }

    // 라벨 -> enum 캐시
    private static final Map<String, MenuCategory> BY_LABEL =
            Arrays.stream(values()).collect(Collectors.toMap(MenuCategory::toString, e -> e));

    // "안주/음료/주류/이벤트" 뿐 아니라 "ANJU/DRINK/ALCOHOL/EVENT"도 허용 (대소문자 무시)
    @JsonCreator
    public static MenuCategory from(String value) {
        if (value == null) throw new IllegalArgumentException("MenuCategory label is null");
        String key = value.trim();
        MenuCategory byLabel = BY_LABEL.get(key);
        if (byLabel != null) return byLabel;

        try {
            return MenuCategory.valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown MenuCategory: " + value + " (허용값: " + Arrays.toString(values()) + ")"
            );
        }
    }
}
