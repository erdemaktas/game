package com.bol.game.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PitDto {
    private Integer stones;
    private Integer index;
    private String playerBy;

}
