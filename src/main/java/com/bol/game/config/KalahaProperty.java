package com.bol.game.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kalaha.game")
public class KalahaProperty {
    private Integer pitCountPerUser;
    private Integer stoneCount;
}
