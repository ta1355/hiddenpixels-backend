package com.yoo.hiddenpixels.initializer;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.yoo.hiddenpixels.model.game.Game;
import com.yoo.hiddenpixels.repository.game.GameRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class GameCsvImporter {

    private static final String CSV_FILE_PATH = "data/93182_steam_games_filtered.csv";
    private final GameRepository gameRepository;

    @PostConstruct
    public void init() {
        long count = gameRepository.count();
        log.info("Current game_list record count: {}", count);
        if (count > 0) {
            log.info("✅ Database already has data. Skipping CSV import.");
        } else {
            log.info("⚡️ Starting CSV import...");
            importGamesFromCsv();
        }
    }

    @Transactional
    public void importGamesFromCsv() {
        try (
                InputStreamReader isr = new InputStreamReader(
                        new ClassPathResource(CSV_FILE_PATH).getInputStream());
                CSVReader reader = new CSVReader(isr)
        ) {
            String[] line;
            boolean isFirst = true;
            int importedCount = 0;
            int skippedCount = 0;

            while ((line = reader.readNext()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                Game game = parseGameFromCsvLine(line);
                if (game == null) {
                    skippedCount++;
                    continue;
                }

                gameRepository.save(game);
                importedCount++;

                if (importedCount % 100 == 0) {
                    log.info("📥 Imported {} records...", importedCount);
                }
            }

            log.info("===========================================");
            log.info("✅ CSV import completed!");
            log.info("👉 Total imported: {}", importedCount);
            log.info("👉 Total skipped: {}", skippedCount);
            log.info("===========================================");

        } catch (Exception e) {
            log.error("❌ CSV import failed!", e);
        }
    }

    private Game parseGameFromCsvLine(String[] line) {
        try {
            for (int i = 0; i < line.length; i++) {
                line[i] = (line[i] == null) ? "" : line[i].trim();
            }

            Long appId = parseLong(line[0]);
            String name = cleanString(line[1]);
            LocalDateTime releaseDate = parseReleaseDate(line[2]);
            String price = cleanPrice(line[3]);
            Integer peakCCU = parseInteger(line[4]);
            List<String> genres = parseList(line[5]);
            List<String> tags = parseList(line[6]);
            String developer = cleanString(line[7]);
            String publisher = cleanString(line[8]);
            List<String> supportedLanguages = parseSupportedLanguages(line[9]);
            String headerImage = cleanString(line[10]);

            if (appId == null || name.isEmpty() || developer.isEmpty()) {
                log.warn("⚠️ Invalid data - AppID: {}, Name: {}", appId, name);
                return null;
            }

            return Game.builder()
                    .appId(appId)
                    .name(name)
                    .releaseDate(releaseDate)
                    .price(price)
                    .peakCCU(peakCCU)
                    .genres(genres)
                    .tags(tags)
                    .developer(developer)
                    .publisher(publisher)
                    .supportedLanguages(supportedLanguages)
                    .headerImage(headerImage)
                    .build();

        } catch (Exception e) {
            log.error("🚨 Error parsing CSV line: {}", Arrays.toString(line), e);
            return null;
        }
    }

    // ===== 전처리 함수들 =====
    private String cleanString(String str) {
        return (str == null) ? "" : str.trim().replaceAll("\\s+", " ");
    }

    private String cleanPrice(String str) {
        return (str == null) ? "" : str.trim().replaceAll("[^\\d.\\w]", "");
    }

    private Long parseLong(String str) {
        try {
            return Long.parseLong(str.replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInteger(String str) {
        try {
            return Integer.parseInt(str.replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parseReleaseDate(String dateStr) {
        if (dateStr.isEmpty()) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return localDate.atStartOfDay();
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> parseList(String str) {
        if (str.isEmpty()) return Collections.emptyList();
        return Arrays.stream(str.split(","))
                .map(this::cleanString)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private List<String> parseSupportedLanguages(String str) {
        if (str.isEmpty()) return Collections.emptyList();
        str = str.replaceAll("[\\[\\]'\" ]", "");
        return Arrays.stream(str.split(","))
                .map(this::cleanString)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}