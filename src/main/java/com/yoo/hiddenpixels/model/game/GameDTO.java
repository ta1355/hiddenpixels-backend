package com.yoo.hiddenpixels.model.game;

import java.time.LocalDateTime;
import java.util.List;

public record GameDTO(
        Long id,
        Long appId,
        String name,
        LocalDateTime releaseDate,
        String price,  //달러 금액
        String priceInKRW,
        String storeURL,
        Integer peakCCU,
        String developer,
        String publisher,
        String headerImage,
        List<String> genres,
        List<String> tags,
        List<String> supportedLanguages
) {
}
