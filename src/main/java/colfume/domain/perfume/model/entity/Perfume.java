package colfume.domain.perfume.model.entity;

import colfume.domain.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    private List<Color> colors = new ArrayList<>(); // 색깔

    @Column(length = 30)
    private String name; // 이름

    private int volume; // 용량

    private int price; // 가격

    @ElementCollection
    private List<String> moods = new ArrayList<>(); // 무드

    @ElementCollection
    private List<String> styles = new ArrayList<>(); // 스타일

    @ElementCollection
    private List<String> notes = new ArrayList<>(); // 노트

    @Column(length = 500)
    private String description; // 설명

    private String imageUrl; // 이미지 url

    private int numOfLikes; // 좋아요 수 (북마크)

    private int evaluationCount; // 평가 수

    private double score; // 평가 점수

    private Perfume(String name, int volume, int price, List<String> moods, List<String> styles, List<String> notes, String description, String imageUrl) {
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.moods = moods;
        this.styles = styles;
        this.notes = notes;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Perfume create(String name, int volume, int price, List<String> moods, List<String> styles, List<String> notes, String description, String imageUrl, List<Hashtag> hashtags, List<Color> colors) {
        Perfume perfume = new Perfume(name, volume, price, moods, styles, notes, description, imageUrl);
        hashtags.forEach(perfume::addHashtag);
        colors.forEach(perfume::addColor);

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

    public void addLikes() {
        numOfLikes++;
    }

    public void subtractLikes() {
        numOfLikes--;
    }

    public void addEvaluationCount() {
        evaluationCount++;
    }

    public void subtractEvaluationCount() {
        evaluationCount--;
    }

    public void scoreToZero() {
        score = 0;
    }

    // 연관관계 편의 메소드
    private void addHashtag(Hashtag hashtag) {
        hashtags.add(hashtag);
        hashtag.updatePerfume(this);
    }

    // 연관관계 편의 메소드
    private void addColor(Color color) {
        colors.add(color);
        color.updatePerfume(this);
    }
}