package com.example.manol.chessgame.pieces;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.R;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.BoardUtils;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.board.Move.AttackMove;
import com.example.manol.chessgame.board.Move.MajorAttackMove;
import com.example.manol.chessgame.board.Move.MajorMove;
import com.example.manol.chessgame.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class King extends Piece {

    private final static int[] CANDIDATE_LEGAL_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final int piecePossition,final Alliance pieceAllience) {
        super(PieceType.KING,piecePossition, pieceAllience,true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_LEGAL_MOVES) {
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isTileValidCoordonate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOcupied() ) {
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
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAllience());
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    @Override
    public final int getRessource() {
        if(this.pieceAllience == Alliance.WHITE){
            return R.drawable.whiteking;
        }
        else{
            return R.drawable.blackking;
        }
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9
                || candidateOffset == -1 || candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == 1
                || candidateOffset == -7 || candidateOffset == 9);
    }

}
