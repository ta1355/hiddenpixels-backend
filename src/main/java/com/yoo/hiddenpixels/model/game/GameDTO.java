package com.yoo.hiddenpixels.model.game;

import java.time.LocalDateTime;
import java.util.List;

public record GameDTO(
        Long id,
        Long appId,
        String name,
        LocalDateTime releaseDate,
        String price,
        Integer peakCCU,
        String developer,
        String publisher,
        String headerImage,
        List<String> genres,
        List<String> tags,
        List<String> supportedLanguages
) {
}
