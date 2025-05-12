package com.yoo.hiddenpixels.model.game;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id", unique = true, nullable = false)
    private Long appId;

    @Column(nullable = false)
    private String name;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    private String price;

    @Column(name = "peak_ccu")
    private Integer peakCCU;   //최대 동시 접속자 수

    @Column(nullable = false, columnDefinition = "TEXT")
    private String developer;

    private String publisher;

    @Column(name = "header_image", columnDefinition = "TEXT")
    private String headerImage;

    @ElementCollection
    @CollectionTable(name = "game_genre", joinColumns = @JoinColumn(name = "game_id"))
    private List<String> genres;

    @ElementCollection
    @CollectionTable(name = "game_tag", joinColumns = @JoinColumn(name = "game_id"))
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "game_language", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "language", nullable = false)
    private List<String> supportedLanguages;

}
