package com.yoo.hiddenpixels.controller;

import com.yoo.hiddenpixels.model.game.GameDTO;
import com.yoo.hiddenpixels.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    //id로 조회
    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> findGameById(@PathVariable Long id){
        try {
            GameDTO dto = gameService.findGameById(id);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    // 전체 조회(페이지 처리)
    @GetMapping
    public ResponseEntity<Page<GameDTO>> getAllGames(Pageable pageable) {
        Page<GameDTO> gameAll = gameService.findGameAll(pageable);
        if (gameAll.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameAll);
    }

    // 이름 검색
    @GetMapping("/search/name")
    public ResponseEntity<Page<GameDTO>> searchByName(@RequestParam String name, Pageable pageable) {
        Page<GameDTO> result = gameService.findByName(name, pageable);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 가격 이하 검색
    @GetMapping("/search/price")
    public ResponseEntity<Page<GameDTO>> searchByPrice(@RequestParam double price, Pageable pageable) {
        Page<GameDTO> result = gameService.findByPriceLessThanEqual(price, pageable);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 개발자 검색
    @GetMapping("/search/developer")
    public ResponseEntity<Page<GameDTO>> searchByDeveloper(@RequestParam String developer, Pageable pageable) {
        Page<GameDTO> result = gameService.findByDeveloper(developer, pageable);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 장르 검색
    @GetMapping("/search/genre")
    public ResponseEntity<Page<GameDTO>> searchByGenre(@RequestParam String genre, Pageable pageable) {
        Page<GameDTO> result = gameService.findByGenre(genre, pageable);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 태그 검색
    @GetMapping("/search/tag")
    public ResponseEntity<Page<GameDTO>> searchByTag(@RequestParam String tag, Pageable pageable) {
        Page<GameDTO> result = gameService.findByTag(tag, pageable);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 지원 언어 검색
    @GetMapping("/search/language")
    public ResponseEntity<Page<GameDTO>> searchBySupportedLanguage(@RequestParam String language, Pageable pageable) {
        Page<GameDTO> result = gameService.findBySupportedLanguage(language, pageable);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

}
