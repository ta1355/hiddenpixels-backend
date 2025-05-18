package com.yoo.hiddenpixels.repository.game;

import com.yoo.hiddenpixels.model.game.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    // 이름으로 검색(부분 일치 (Like 절 사용))
    Page<Game> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /* 가격으로 검색하는 기능(가격 string 이어서 변환 필요함)
    얼마 이하 이런식으로 되어 있어서 파람에 담는거 가격으로 작성 그리고 현재 db에 있는 가격이 달러로 되어 있음 한화로 변경하는 기능이 필요함
    */
    @Query("SELECT g FROM Game g WHERE FUNCTION('CAST', REPLACE(g.price, ',', ''), double) <= :price")
    List<Game> findByPriceLessThanEqual(@Param("price") double price, Pageable pageable);

    // 개발자로 검색(부분 일지 (Like 절 사용))
    Page<Game> findByDeveloperContainingIgnoreCase(String developer, Pageable pageable);

    //장르로 검색
    @Query("SELECT g FROM Game g JOIN g.genres genre WHERE genre = :genre")
    List<Game> findBygGenre(@Param("genre") String genre, Pageable pageable);

    // 태그로 검색
    @Query("SELECT g FROM Game g JOIN g.tags tag WHERE tag = :tag")
    List<Game> findByTag(@Param("tag") String tag, Pageable pageable);

    // 지원 언어로 검색
    @Query("SELECT g FROM Game g JOIN g.supportedLanguages lang WHERE lang = :language")
    List<Game> findBySupportedLanguage(@Param("language") String language, Pageable pageable);
}
