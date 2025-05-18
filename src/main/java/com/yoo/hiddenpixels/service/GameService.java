package com.yoo.hiddenpixels.service;

import com.yoo.hiddenpixels.model.game.Game;
import com.yoo.hiddenpixels.model.game.GameDTO;
import com.yoo.hiddenpixels.repository.game.GameRepository;
import com.yoo.hiddenpixels.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final SteamPriceService steamPriceService;

    // id로 하나 조회
    public GameDTO findGameById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow();
        return toDto(game);
    }

    // 전체 조회
    public Page<GameDTO> findGameAll(Pageable pageable) {
        return gameRepository.findAll(pageable).map(this::toDto);
    }

    // 이름 검색
    public Page<GameDTO> findByName(String name, Pageable pageable) {
        return gameRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::toDto);
    }

    // 개발자 검색
    public Page<GameDTO> findByDeveloper(String developer, Pageable pageable){
        return gameRepository.findByDeveloperContainingIgnoreCase(developer,pageable).map(this::toDto);
    }

    // 장르 검색
    public Page<GameDTO> findByGenre(String genre, Pageable pageable) {
        List<Game> games = gameRepository.findBygGenre(genre, pageable);
        List<GameDTO> dtos = games.stream().map(this::toDto).toList();
        return PageUtils.<GameDTO>toPage(pageable).apply(dtos);
    }

    // 태그 검색
    public Page<GameDTO> findByTag(String tag, Pageable pageable) {
        List<Game> games = gameRepository.findByTag(tag, pageable);
        List<GameDTO> dtos = games.stream().map(this::toDto).toList();
        return PageUtils.<GameDTO>toPage(pageable).apply(dtos);
    }

    // 지원 언어 검색 (List → Page 변환)
    public Page<GameDTO> findBySupportedLanguage(String language, Pageable pageable) {
        List<Game> games = gameRepository.findBySupportedLanguage(language, pageable);
        List<GameDTO> dtos = games.stream().map(this::toDto).toList();
        return PageUtils.<GameDTO>toPage(pageable).apply(dtos);
    }

    // 가격 이하 검색 (List → Page 변환)
    public Page<GameDTO> findByPriceLessThanEqual(double price, Pageable pageable) {
        List<Game> games = gameRepository.findByPriceLessThanEqual(price, pageable);
        List<GameDTO> dtos = games.stream().map(this::toDto).toList();
        return PageUtils.<GameDTO>toPage(pageable).apply(dtos);
    }


    // 가격 url 포함 dto 반환용 함수
    private GameDTO toDto(Game game) {
        SteamPriceService.SteamPriceInfo steamPriceInfo = steamPriceService.getSteamGamePrice(game.getAppId());

        return new GameDTO(
                game.getId(),
                game.getAppId(),
                game.getName(),
                game.getReleaseDate(),
                game.getPrice(), // 기존 달러 금액
                steamPriceInfo.getPrice(),  // 한화 금액
                steamPriceInfo.getStoreUrl(), //상점 url
                game.getPeakCCU(),
                game.getDeveloper(),
                game.getPublisher(),
                game.getHeaderImage(),
                game.getGenres(),
                game.getTags(),
                game.getSupportedLanguages()
        );
    }
}
