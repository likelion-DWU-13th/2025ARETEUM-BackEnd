package likelion.be.areteum.booth.dto;

import java.util.List;

public record BoothSearchRes(
        List<BoothCardRes> booths, // 검색된 항목 카드 리스트
        int totalCount // 검색된 항목 개수
) {}
