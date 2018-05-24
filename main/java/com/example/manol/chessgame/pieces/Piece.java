package com.example.manol.chessgame.pieces;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.Move;

import java.util.Collection;

/**
 * Created by manol on 14.05.2018.
 */

public abstract class Piece {

    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAllience;
    protected boolean isFirstMove;
    private final int cashedHashCode ;

    Piece(final PieceType pieceType,
          final int piecePossition,
          final Alliance pieceAllience,
          final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.pieceAllience = pieceAllience;
        this.piecePosition = piecePossition;
        this.isFirstMove = isFirstMove;
        this.cashedHashCode = computeHashCode();
    }

    private  int computeHashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAllience.hashCode();
        result = 31 * result + piecePosition;
        result = 32 * result +(isFirstMove ? 1: 0);
        return  result;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return pieceType == otherPiece.getPiceType() && pieceAllience == otherPiece.getPieceAllience()&&
                piecePosition == otherPiece.getPiecePosition() && isFirstMove==otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
        return this.cashedHashCode;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public Alliance getPieceAllience() {
        return this.pieceAllience;
    }

    public PieceType getPiceType() {
        return this.pieceType;
    }

    public abstract int getRessource();
    public  void setFirstMove(){
        this.isFirstMove = false;
    }
    public int getPieceValue(){
        return pieceType.getPieceValue();
    }
    public enum PieceType {

        PAWN(100, "P") {
            @Override
            public boolean isPawn() {
                return true;
            }

            @Override
            public boolean isBishop() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        KINIGHT(320, "N") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isBishop() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP(350, "B") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isBishop() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isBishop() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN(900, "Q") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isBishop() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING(20000, "K") {
            @Override
            public boolean isPawn() {
                return false;
            }

            @Override
            public boolean isBishop() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }

            @Override
            public boolean isKing() {
                return true;
            }
        };

        private final int value;
        private final String pieceName;

        PieceType(final int val,
                  final String pieceName) {
            this.value = val;
            this.pieceName = pieceName;
        }
        public int getPieceValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }



        public abstract boolean isPawn();
        public abstract boolean isBishop();
        public abstract boolean isRook();
        public abstract boolean isKing();

    }

}

