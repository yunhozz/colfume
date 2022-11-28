package colfume.domain.perfume.service.converter;

import colfume.common.EntityConverter;
import colfume.domain.perfume.dto.request.PerfumeRequestDto;
import colfume.domain.perfume.dto.response.ColorResponseDto;
import colfume.domain.perfume.dto.response.HashtagResponseDto;
import colfume.domain.perfume.dto.response.PerfumeResponseDto;
import colfume.domain.perfume.model.entity.Color;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Mood;
import colfume.domain.perfume.model.entity.Note;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.entity.Style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfumeConverter implements EntityConverter<Perfume, PerfumeRequestDto, PerfumeResponseDto> {

    @Override
    public Perfume convertToEntity(PerfumeRequestDto perfumeRequestDto) {
        List<Mood> moods = new ArrayList<>() {{
            perfumeRequestDto.getMoods().forEach(mood -> add(new Mood(mood)));
        }};

        List<Style> styles = new ArrayList<>() {{
            perfumeRequestDto.getStyles().forEach(style -> add(new Style(style)));
        }};

        List<Note> notes = new ArrayList<>() {{
            perfumeRequestDto.getNotes().forEach((stage, note) -> add(new Note(stage, note)));
        }};

        List<Hashtag> hashtags = new ArrayList<>() {{
            perfumeRequestDto.getTags().forEach(tag -> add(new Hashtag(tag)));
        }};

        List<Color> colors = new ArrayList<>() {{
            perfumeRequestDto.getColorType().forEach(colorType -> add(new Color(colorType)));
        }};

        return Perfume.create(
                perfumeRequestDto.getName(),
                perfumeRequestDto.getVolume(),
                perfumeRequestDto.getPrice(),
                moods,
                styles,
                notes,
                perfumeRequestDto.getDescription(),
                perfumeRequestDto.getImageUrl(),
                hashtags,
                colors
        );
    }

    @Override
    public PerfumeResponseDto convertToDto(Perfume perfume) {
        List<String> moods = new ArrayList<>() {{
            perfume.getMoods().forEach(mood -> add(mood.getMoodValue()));
        }};

        List<String> styles = new ArrayList<>() {{
            perfume.getStyles().forEach(style -> add(style.getStyleValue()));
        }};

        Map<String, String> notes = new HashMap<>() {{
            perfume.getNotes().forEach(note -> put(note.getStage().name(), note.getNoteValue()));
        }};

        List<HashtagResponseDto> hashtags = perfume.getHashtags().stream()
                .map(HashtagResponseDto::new).toList();

        List<ColorResponseDto> colors = perfume.getColors().stream()
                .map(ColorResponseDto::new).toList();

        return new PerfumeResponseDto(
                perfume.getId(),
                perfume.getName(),
                perfume.getVolume(),
                perfume.getPrice(),
                moods,
                styles,
                notes,
                perfume.getDescription(),
                perfume.getImageUrl(),
                perfume.getNumOfLikes(),
                perfume.getEvaluationCount(),
                perfume.getScore(),
                perfume.getCreatedDate(),
                perfume.getLastModifiedDate(),
                hashtags,
                colors
        );
    }
}