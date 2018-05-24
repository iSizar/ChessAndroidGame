package com.example.manol.chessgame.player.ai;

import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.pieces.Piece;
import com.example.manol.chessgame.player.Player;

public final class StandardBoardEvaluator implements BoardEvalulator {
    private static final int CHECK_BONUS = 50;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLED_BONUS = 60;

    @Override
    public int evaluate(final Board board,
                        final int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) -
                scorePlayer(board, board.whitePlayer(), depth);
    }

    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth) {
        return pieceValue(player) +
                mobility(player) +
                check(player) +
                checkMate(player, depth) +
                castle(player);
    }

    private static int castle(final Player player) {
        return player.isCastled() ? CASTLED_BONUS : 0;
    }

    private static int checkMate(final Player player, final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
