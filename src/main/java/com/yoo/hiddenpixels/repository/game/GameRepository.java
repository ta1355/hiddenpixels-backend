package com.yoo.hiddenpixels.repository.game;

import com.yoo.hiddenpixels.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
