package com.example.manol.chessgame.board;

import com.example.manol.chessgame.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {

        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; ++i) {
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }

    private Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public static Tile createTile(int tileCoordinate, Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate,piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    public abstract boolean isTileOcupied();

    public abstract Piece getPiece();


    public int getTileCoordinate(){
        return  this.tileCoordinate;
    }

    public static final class EmptyTile extends Tile {

        EmptyTile(final int tileCoordinate) {
            super(tileCoordinate);
        }

        @Override
        public String toString(){
            return"-";
        }

        @Override
        public boolean isTileOcupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

    }

    public static final class OccupiedTile extends Tile {

        private final Piece pieceOnTile;

        OccupiedTile(int tileCoordinate,final Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public String toString(){
            return pieceOnTile.getPieceAllience().isBlack()?this.pieceOnTile.toString().toLowerCase():
                    this.pieceOnTile.toString();
        }

        @Override
        public boolean isTileOcupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}
