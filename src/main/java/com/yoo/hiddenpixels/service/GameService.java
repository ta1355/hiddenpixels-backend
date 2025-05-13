package com.yoo.hiddenpixels.service;

import com.yoo.hiddenpixels.model.game.Game;
import com.yoo.hiddenpixels.model.game.GameDTO;
import com.yoo.hiddenpixels.repository.game.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    // id로 하나 조회
    public GameDTO findGameById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow();
        return toDto(game);
    }

    // 전체 조회
    public Page<GameDTO> findGameAll(Pageable pageable) {
        return gameRepository.findAll(pageable).map(this::toDto);
    }

    // dto 반환용 함수
    private GameDTO toDto(Game game) {
        return new GameDTO(
                game.getId(),
                game.getAppId(),
                game.getName(),
                game.getReleaseDate(),
                game.getPrice(),
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
