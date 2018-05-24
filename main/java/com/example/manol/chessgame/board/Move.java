package com.example.manol.chessgame.board;


import com.example.manol.chessgame.board.Board.Builder;
import com.example.manol.chessgame.pieces.Pawn;
import com.example.manol.chessgame.pieces.Piece;
import com.example.manol.chessgame.pieces.Rook;

import java.util.List;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board,
                 final Piece movedPiece,
                 final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public int getCurreentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    @Override
    public int hashCode(){
        int prime = 31;
        int result = 1;
        result = result * prime + this.movedPiece.getPiecePosition();
        result = result * prime + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other)
            return true;
        if(!(other instanceof Move))
            return false;
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public  boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute()
    {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        //set the moved piece
        Piece newPiece = this.movedPiece.movePiece(this);
        newPiece.setFirstMove();
        builder.setPiece(newPiece);
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public Board getBoard() {
        return this.board;
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board,
                         final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public String toString(){
            return movedPiece.getPiceType().toString()+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if(this == other)
                return true;
            if(!(other instanceof AttackMove))
                return false;
            final AttackMove otherMove = (AttackMove) other;
            return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

    }

    public static class MajorAttackMove extends  AttackMove{

        public MajorAttackMove(final Board board,
                                     final Piece movedPiece,
                                     final int destinationCoordinate,
                                     final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public  boolean equals(Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPiceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static final class PawnMove extends Move {

        public PawnMove(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnPromotion extends Move{

        final Move decoratedMove;
        final Pawn promotedPawn;

        public PawnPromotion(final Move decoretedMove) {
            super(decoretedMove.getBoard(),decoretedMove.getMovedPiece(),decoretedMove.getDestinationCoordinate());
            this.decoratedMove = decoretedMove;
            this.promotedPawn = (Pawn) decoretedMove.getMovedPiece();
        }

        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for(final Piece piece : board.currentPlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            Piece newPiece = this.promotedPawn.getPromotionPiece().movePiece(this);
            builder.setPiece(newPiece);
            newPiece.setFirstMove();
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getOpponent().getAlliance());
            return  builder.build();
        }

        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece(){
            return decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString(){
            return "";
        }

        @Override
        public int hashCode(){
            return decoratedMove.hashCode()+(31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }

    }

    public static class PawnAtackMove extends AttackMove {

        public PawnAtackMove(final Board board,
                             final Piece movedPiece,
                             final int destinationCoordinate,
                             final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnAtackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) +
                    "x"+BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnEnPansanAtackMove extends PawnAtackMove {

        public PawnEnPansanAtackMove(final Board board,
                                     final Piece movedPiece,
                                     final int destinationCoordinate,
                                     final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other){
            return (this == other) || ((other instanceof PawnEnPansanAtackMove) && super.equals(other));
        }

        @Override
        public Board execute(){
            final Builder  builder = new Builder();

            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!(this.movedPiece.equals(piece))){
                    builder.setPiece(piece);
                }
            }

            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!(this.attackedPiece.equals(piece))){
                    builder.setPiece(piece);
                }
            }
            Piece newPiece = this.movedPiece.movePiece(this);
            builder.setPiece(newPiece);
            newPiece.setFirstMove();
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }


    }

    public static final class PawnJump extends Move {

        public PawnJump(final Board board,
                        final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                 builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            movedPawn.setFirstMove();
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    static abstract class CastleMove extends Move {

        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;


        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook(){
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) &&!piece.equals(castleRook)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            Piece newPiece = movedPiece.movePiece(this);
            newPiece.setFirstMove();
            builder.setPiece(newPiece);
            Piece newRook = new Rook(this.castleRookDestination,this.castleRook.getPieceAllience());
            newRook.setFirstMove();
            builder.setPiece(newRook);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other){
           if(other == this ){
               return  true;
           }
           if(!(other instanceof CastleMove))
               return false;
           final CastleMove castleMove = (CastleMove) other;
           return super.equals(castleMove) && this.castleRook.equals(castleMove.getCastleRook());
        }

    }

    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board,
                                  final Piece movedPiece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int castleRookStart,
                                  final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }

        @Override
        public boolean equals(Object other){
            return (this == other) || ((other instanceof KingSideCastleMove) && super.equals(other));
        }

        @Override
        public String toString(){
            return "0-0";
        }

    }

    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board,
                                   final Piece movedPiece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int castleRookStart,
                                   final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate,castleRook,castleRookStart,castleRookDestination);
        }
        @Override
        public boolean equals(Object other){
            return (this == other) || ((other instanceof QueenSideCastleMove) && super.equals(other));
        }

        @Override
        public String toString(){
            return "0-0-0";
        }
    }

    public static final class NullMove extends Move {

        public NullMove() {
            super(null,null,-1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Null Move cann't be done!!");
        }

        @Override
        public int getDestinationCoordinate(){
            return -1;
        }
    }

    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            List<Move> list_move = board.getAllLegalMoves();
            for (Move move : list_move) {
                if (move.getCurreentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
