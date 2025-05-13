package com.yoo.hiddenpixels.controller;

import com.yoo.hiddenpixels.model.game.GameDTO;
import com.yoo.hiddenpixels.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    //id로 조회
    public ResponseEntity<GameDTO> findGameById(@PathVariable Long id){
        try {
            GameDTO dto = gameService.findGameById(id);
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

}
