package colfume.domain.perfume.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note {

    // TODO: 탑, 미들, 바텀 노트 구분

    private String noteValue;

    public Note(String noteValue) {
        this.noteValue = noteValue;
    }
}