package com.example.manol.chessgame.player.ai;

import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.player.MoveTransition;

public class MinMax implements MoveStrategy {

    private final BoardEvalulator boardEvalulator;
    private final int depth;

    public MinMax(final int depth) {
        this.boardEvalulator = new StandardBoardEvaluator();
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "MinMax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int hiestSeenValue = Integer.MIN_VALUE;
        int loestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + " THINCKING WITH DEPTH: " + depth);

        int numMoves = board.currentPlayer().getLegalMoves().size();

        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), depth-1) :
                        max(moveTransition.getTransitionBoard(), depth-1);

                if (board.currentPlayer().getAlliance().isWhite() && currentValue > hiestSeenValue) {
                    hiestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getAlliance().isBlack() && currentValue < loestSeenValue) {
                    loestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;


        return bestMove;
    }

    private static boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }

    public int min(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvalulator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }



    public int max(final Board board, final int depth) {
        if (depth == 0/*or game over*/) {
            return this.boardEvalulator.evaluate(board, depth);
        }
        int hiestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue > hiestSeenValue) {
                    hiestSeenValue = currentValue;
                }
            }
        }
        return hiestSeenValue;
    }

}
