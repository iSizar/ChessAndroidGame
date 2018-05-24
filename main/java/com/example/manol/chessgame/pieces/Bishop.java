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


public class Bishop extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    public Bishop(final int piecePossition,final Alliance pieceAllience) {
        super(PieceType.BISHOP,piecePossition, pieceAllience,true);
    }



    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtils.isTileValidCoordonate(candidateDestinationCoordinate)){
                if(isFirstColumnExclusion(candidateDestinationCoordinate , candidateCoordinateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset))
                    break;
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if(BoardUtils.isTileValidCoordonate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOcupied()) {
                        legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAllience();
                        if (this.pieceAllience != pieceAlliance) {
                            legalMoves.add(new MajorAttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                        }
                        break;
                    }

                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAllience());
    }

    @Override
    public final int getRessource() {
        if(this.pieceAllience == Alliance.WHITE){
            return R.drawable.whitebishop;
        }
         else{
            return R.drawable.blackbishop;
        }
    }


    @Override
    public String toString(){
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }

}
