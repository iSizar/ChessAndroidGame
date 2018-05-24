package com.example.manol.chessgame.player;

import android.text.TextUtils;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.pieces.King;
import com.example.manol.chessgame.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           Collection<Move> playerLegals,
           Collection<Move> opponentLegals) {
        this.board = board;
        this.playerKing = establishKing();
        this.isInCheck = !Player.calculateAtackOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
        playerLegals.addAll(calculateKingCastles(playerLegals, opponentLegals));
        this.legalMoves = playerLegals;
    }

    public King getPlayerKing(){
        return playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    protected static Collection<Move> calculateAtackOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> atackMoves = new ArrayList<>();
        for(final Move move : opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                atackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(atackMoves);
    }

    private King establishKing() {
        for(final Piece piece: getActivePieces()){
            if(piece.getPiceType().isKing()){
                return (King) piece;
            }
        }
        throw  new RuntimeException("should not reach here! Not a valid board");
    }

    public boolean isLegalMove(Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return isInCheck;
    }

    public boolean isInCheckMate(){
        return isInCheck && !hasEscapeMoves();
    }

    public  boolean isInStaleMate(){
        return !this.isInCheck &&!hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    protected boolean hasEscapeMoves(){
        for(final Move move: legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public MoveTransition makeMove(final Move move){

        if(!isLegalMove(move)){
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        final Collection<Move> kingsAtack = Player.calculateAtackOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if(!kingsAtack.isEmpty()){
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,Collection<Move> opponentsLegals);

    public enum PlayerType{
        HUMAN_PLAYER("HUMAN_PLAYER"),
        COMPUTER_PLAYER("COMPUTER_PLAYER");

        private final String playerType;
        PlayerType(final String playerType){
            this.playerType = playerType;
        }
    }
}
