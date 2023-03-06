package com.bol.game.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Data
@Builder
public class ResponseCreate extends RepresentationModel<ResponseCreate> {
    private UUID id;
}
