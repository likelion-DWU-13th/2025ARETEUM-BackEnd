package likelion.be.areteum.booth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Category {
    PERFORMANCE("공연"),        // 공연(버스킹/메인공연)
    BOOTH("부스"),              // 일반 부스
    EXPERIENCE("체험"),         // 체험
    MARKET("마켓"),             // 플리/일반 마켓
    PUB("주점"),                // 주점
    FOOD_TRUCK("푸드트럭");      // 푸드트럭

    private final String label;

    Category(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    private static final Map<String, Category> LABEL_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(Category::getLabel, e -> e));

    @JsonCreator
    public static Category from(String label) {
        Category category = LABEL_MAP.get(label);
        if (category == null) {
            throw new IllegalArgumentException("Unknown Category label: " + label);
        }
        return category;
    }
}

//public enum Category {
//    공연,   // PERFORMANCE 공연(버스킹/메인공연)
//    부스,         // BOOTH 일반 부스
//    체험,    // EXPERIENCE 체험
//    마켓,        // MARKET 마켓(플리/일반)
//    주점,           // PUB 주점
//    푸드트럭     // FOOD_TRUCK 푸드트럭
//}
