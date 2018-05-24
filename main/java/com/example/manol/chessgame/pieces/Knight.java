package com.example.manol.chessgame.pieces;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.R;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.BoardUtils;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.board.Move.MajorAttackMove;
import com.example.manol.chessgame.board.Move.MajorMove;
import com.example.manol.chessgame.board.Move.AttackMove;
import com.example.manol.chessgame.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Knight extends Piece {

    private final static int[] CANDIDATE_LEGAL_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePossition,final Alliance pieceAllience) {
        super(PieceType.KINIGHT,piecePossition, pieceAllience, true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_LEGAL_MOVES) {
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isTileValidCoordonate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOcupied()) {
                    legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAllience();
                    if (this.pieceAllience != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAllience());
    }

    @Override
    public final int getRessource() {
        if(this.pieceAllience == Alliance.WHITE){
            return R.drawable.whitehorse;
        }
        else{
            return R.drawable.blackknight;
        }
    }

    @Override
    public String toString(){
        return PieceType.KINIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10
                || candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 17 || candidateOffset == 10
                || candidateOffset == -6 || candidateOffset == -15);
    }

}
