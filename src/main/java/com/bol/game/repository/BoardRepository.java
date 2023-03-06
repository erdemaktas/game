package com.bol.game.repository;

import com.bol.game.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardRepository extends MongoRepository<Board, UUID> {
}
