package com.bol.game.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ResponseGame extends RepresentationModel<ResponseGame> {
    private UUID id;
    private List<PitDto> board;
    private String playerTurn;
}
