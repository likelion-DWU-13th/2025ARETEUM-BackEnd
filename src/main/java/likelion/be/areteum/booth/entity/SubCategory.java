package likelion.be.areteum.booth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubCategory {
    NONE("없음"),
    BUSKING("버스킹"),
    MAIN_PERFORMANCE("메인공연"),
    FLEA_MARKET("플리마켓"),
    GENERAL_MARKET("일반마켓");

    private final String label;

    SubCategory(String label) {
        this.label = label;
    }

    @JsonValue // JSON 직렬화 시 이 값으로 변환됨
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static SubCategory from(String label) {
        for (SubCategory subCategory : SubCategory.values()) {
            if (subCategory.label.equals(label)) {
                return subCategory;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + label);
    }
}

//public enum SubCategory {
//    NONE,
//    버스킹, // BUSKING
//    메인공연, // MAIN_PERFORMANCE
//    플리마켓, // FLEA_MARKET
//    일반마켓 // GENERAL_MARKET
//}
