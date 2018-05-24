package com.example.manol.chessgame.player.ai;

import com.example.manol.chessgame.board.Board;

public interface BoardEvalulator {
    int evaluate(Board board , int depth);
}
