package colfume.domain.perfume.model.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeSearchQueryDto {

    private Long id;
    private String name;
    private String description;
    private String tag;
}
