package colfume.domain.perfume.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note {

    @Enumerated(EnumType.STRING)
    private Stage stage;
    private String noteValue;

    public Note(Stage stage, String noteValue) {
        this.stage = stage;
        this.noteValue = noteValue;
    }

    public enum Stage {
        TOP, MIDDLE, BOTTOM;
    }
}