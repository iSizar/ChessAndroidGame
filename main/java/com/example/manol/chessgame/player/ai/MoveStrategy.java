package com.example.manol.chessgame.player.ai;

import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
