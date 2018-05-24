package com.example.manol.chessgame.pieces;

import android.icu.text.AlphabeticIndex;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.R;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.BoardUtils;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.board.Move.MajorMove;
import com.example.manol.chessgame.board.Move.PawnAtackMove;
import com.example.manol.chessgame.board.Move.PawnJump;
import com.example.manol.chessgame.board.Move.PawnMove;
import com.example.manol.chessgame.board.Move.PawnPromotion;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Pawn extends Piece {


    private final static int[] CANDIDATE_LEGAL_MOVES = {7, 8, 9, 16};

    public Pawn(final int piecePossition, final Alliance pieceAllience) {
        super(PieceType.PAWN, piecePossition, pieceAllience,true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_LEGAL_MOVES) {
            int candidateDestinationCoordinate = this.piecePosition +
                    currentCandidateOffset * this.getPieceAllience().getDirection();

            if (!BoardUtils.isTileValidCoordonate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOcupied()) {
                if (this.pieceAllience.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[piecePosition] && this.getPieceAllience().isBlack()) ||
                            (BoardUtils.SEVENTH_ROW[piecePosition] && this.getPieceAllience().isWhite()))) {
                final int behindCandidateDestinationCoordonate = this.piecePosition + (this.pieceAllience.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordonate).isTileOcupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOcupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 7 &&
                    !(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAllience.isWhite() ||
                            BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAllience.isBlack())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOcupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAllience != pieceOnCandidate.getPieceAllience()) {
                        if (this.pieceAllience.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAtackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAtackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPanssant() != null) {
                    if (board.getEnPanssant().getPiecePosition() == this.piecePosition + (this.pieceAllience.getOppositeDirection())) {
                        final Piece pieceOnCandidate = board.getEnPanssant();
                        if (this.pieceAllience != pieceOnCandidate.getPieceAllience()) {
                            legalMoves.add(new Move.PawnEnPansanAtackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            } else if (currentCandidateOffset == 9 &&
                    !(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAllience.isWhite() ||
                            BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAllience.isBlack())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOcupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAllience != pieceOnCandidate.getPieceAllience()) {
                        if (this.pieceAllience.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAtackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAtackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                } else if (board.getEnPanssant() != null) {
                    if (board.getEnPanssant().getPiecePosition() == this.piecePosition + (this.pieceAllience.getOppositeDirection())) {
                        final Piece pieceOnCandidate = board.getEnPanssant();
                        if (this.pieceAllience != pieceOnCandidate.getPieceAllience()) {
                            legalMoves.add(new Move.PawnEnPansanAtackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAllience());
    }

    @Override
    public final int getRessource() {
        if (this.pieceAllience == Alliance.WHITE) {
            return R.drawable.whitepawn;
        } else {
            return R.drawable.blackpawn;
        }
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition,this.pieceAllience);
    }
}
