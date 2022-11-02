package colfume.common.converter.entity;

import colfume.api.dto.perfume.PerfumeRequestDto;
import colfume.domain.perfume.model.entity.Color;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.service.dto.ColorResponseDto;
import colfume.domain.perfume.service.dto.HashtagResponseDto;
import colfume.domain.perfume.service.dto.PerfumeResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PerfumeConverter implements EntityConverter<Perfume, PerfumeRequestDto, PerfumeResponseDto> {

    private List<Hashtag> hashtags;
    private List<Color> colors;

    @Override
    public Perfume convertToEntity(PerfumeRequestDto perfumeRequestDto) {
        return Perfume.create(
                perfumeRequestDto.getName(),
                perfumeRequestDto.getVolume(),
                perfumeRequestDto.getPrice(),
                perfumeRequestDto.getMoods(),
                perfumeRequestDto.getStyles(),
                perfumeRequestDto.getNotes(),
                perfumeRequestDto.getDescription(),
                perfumeRequestDto.getImageUrl(),
                hashtags,
                colors
        );
    }

    @Override
    public PerfumeResponseDto convertToDto(Perfume perfume) {
        List<HashtagResponseDto> hashtags = perfume.getHashtags().stream()
                .map(HashtagResponseDto::new).toList();
        List<ColorResponseDto> colors = perfume.getColors().stream()
                .map(ColorResponseDto::new).toList();

        return new PerfumeResponseDto(
                perfume.getId(),
                perfume.getName(),
                perfume.getVolume(),
                perfume.getPrice(),
                perfume.getMoods(),
                perfume.getStyles(),
                perfume.getNotes(),
                perfume.getDescription(),
                perfume.getImageUrl(),
                perfume.getCreatedDate(),
                perfume.getLastModifiedDate(),
                hashtags,
                colors
        );
    }

    public void update(List<Hashtag> hashtags, List<Color> colors) {
        this.hashtags = hashtags;
        this.colors = colors;
    }
}