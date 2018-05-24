package com.example.manol.chessgame.board;


import android.support.annotation.NonNull;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.pieces.Bishop;
import com.example.manol.chessgame.pieces.King;
import com.example.manol.chessgame.pieces.Knight;
import com.example.manol.chessgame.pieces.Pawn;
import com.example.manol.chessgame.pieces.Piece;
import com.example.manol.chessgame.pieces.Queen;
import com.example.manol.chessgame.pieces.Rook;
import com.example.manol.chessgame.player.BlackPlayer;
import com.example.manol.chessgame.player.Player;
import com.example.manol.chessgame.player.WhitePlayer;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.manol.chessgame.board.Tile.createTile;

public class Board {

    private List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard,Alliance.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }

    public Player currentPlayer() {
        return currentPlayer;
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; ++i ){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i + 1)%BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Collection<Piece> getBlackPieces() {
        return blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return whitePieces;
    }

    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece :pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        List<Piece> activePieces = new ArrayList<>();
        for(Tile tile:gameBoard){
            if(tile.isTileOcupied()){
                final Piece piece = tile.getPiece();
                if(piece.getPieceAllience() == alliance){
                    activePieces.add(piece);
                }
            }
        }

        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return this.gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for(int i = 0; i < BoardUtils.NUM_TILES; ++i){
            tiles[i] = createTile(i,builder.boardConfig.get(i));
        }

        final List<Tile> tile_list = new ArrayList<>();
        Collections.addAll(tile_list, tiles);
        return ImmutableList.copyOf(tile_list);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        //Black Pieces
        builder.setPiece(new Rook(0,Alliance.BLACK));
        builder.setPiece(new Knight(1,Alliance.BLACK));
        builder.setPiece(new Bishop(2,Alliance.BLACK));
        builder.setPiece(new Queen(3,Alliance.BLACK));
        builder.setPiece(new King(4,Alliance.BLACK));
        builder.setPiece(new Bishop(5,Alliance.BLACK));
        builder.setPiece(new Knight(6,Alliance.BLACK));
        builder.setPiece(new Rook(7,Alliance.BLACK));
        builder.setPiece(new Pawn(8,Alliance.BLACK));
        builder.setPiece(new Pawn(9,Alliance.BLACK));
        builder.setPiece(new Pawn(10,Alliance.BLACK));
        builder.setPiece(new Pawn(11,Alliance.BLACK));
        builder.setPiece(new Pawn(12,Alliance.BLACK));
        builder.setPiece(new Pawn(13,Alliance.BLACK));
        builder.setPiece(new Pawn(14,Alliance.BLACK));
        builder.setPiece(new Pawn(15,Alliance.BLACK));
        //White Pieces
        builder.setPiece(new Rook(63,Alliance.WHITE));
        builder.setPiece(new Knight(62,Alliance.WHITE));
        builder.setPiece(new Bishop(61,Alliance.WHITE));
        builder.setPiece(new King(60,Alliance.WHITE));
        builder.setPiece(new Queen(59,Alliance.WHITE));
        builder.setPiece(new Bishop(58,Alliance.WHITE));
        builder.setPiece(new Knight(57,Alliance.WHITE));
        builder.setPiece(new Rook(56,Alliance.WHITE));
        builder.setPiece(new Pawn(55,Alliance.WHITE));
        builder.setPiece(new Pawn(54,Alliance.WHITE));
        builder.setPiece(new Pawn(53,Alliance.WHITE));
        builder.setPiece(new Pawn(52,Alliance.WHITE));
        builder.setPiece(new Pawn(51,Alliance.WHITE));
        builder.setPiece(new Pawn(50,Alliance.WHITE));
        builder.setPiece(new Pawn(49,Alliance.WHITE));
        builder.setPiece(new Pawn(48,Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    public List<Move> getAllLegalMoves() {
        List<Move> legalMoves = new ArrayList<>();
        legalMoves.addAll(this.whitePlayer.getLegalMoves());
        legalMoves.addAll(this.blackPlayer.getLegalMoves());
        return ImmutableList.copyOf(legalMoves);
    }

    public Pawn getEnPanssant() {
        return this.enPassantPawn;
    }

    public Iterable<Piece> getAllPieces() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePieces, this.blackPieces));
    }

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn enPassantPawn;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }


}
