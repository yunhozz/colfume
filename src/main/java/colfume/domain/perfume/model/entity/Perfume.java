package colfume.domain.perfume.model.entity;

import colfume.domain.BaseTime;
import colfume.enums.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Perfume extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    private List<Hashtag> hashtags = new ArrayList<>(); // 해시태그

    @Column(length = 30)
    private String name; // 이름

    private int volume; // 용량

    private int price; // 가격

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Color> colors = new ArrayList<>(); // 색깔

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> moods = new ArrayList<>(); // 무드

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> styles = new ArrayList<>(); // 스타일

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> notes = new ArrayList<>(); // 노트

    @Column(length = 500)
    private String description; // 설명

    private String imageUrl; // 이미지 url

    private Perfume(String name, int volume, int price, List<Color> colors, List<String> moods, List<String> styles, List<String> notes, String description, String imageUrl) {
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.colors = colors;
        this.moods = moods;
        this.styles = styles;
        this.notes = notes;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Perfume create(String name, int volume, int price, List<Color> colors, List<String> moods, List<String> styles, List<String> notes, String description, String imageUrl, List<Hashtag> hashtags) {
        Perfume perfume = new Perfume(name, volume, price, colors, moods, styles, notes, description, imageUrl);
        hashtags.forEach(perfume::addHashtag);

        return perfume;
    }

    public void updateInfo(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 연관관계 편의 메소드
    private void addHashtag(Hashtag hashtag) {
        hashtags.add(hashtag);
        hashtag.updatePerfume(this);
    }
}
