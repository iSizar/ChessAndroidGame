package com.example.manol.chessgame.player;

import com.example.manol.chessgame.Alliance;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.board.Move.KingSideCastleMove;
import com.example.manol.chessgame.board.Move.QueenSideCastleMove;
import com.example.manol.chessgame.board.Tile;
import com.example.manol.chessgame.pieces.Piece;
import com.example.manol.chessgame.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                 final Collection<Move> opponentsLegals) {
        final List<Move> kingCasltes = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //Black King Side Castle
            if (!this.board.getTile(5).isTileOcupied() && !this.board.getTile(6).isTileOcupied()) {
                final Tile rookTile = this.board.getTile(7);
                if (rookTile.isTileOcupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAtackOnTile(5,opponentsLegals).isEmpty() &&
                            Player.calculateAtackOnTile(6,opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPiceType().isRook()) {
                        kingCasltes.add(new KingSideCastleMove(
                                board,
                                this.playerKing,
                                6,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                5));
                    }
                }
            }
            //Black King Qeen Side Caslte
            if (!this.board.getTile(1).isTileOcupied()
                    && !this.board.getTile(2).isTileOcupied()
                    && !this.board.getTile(3).isTileOcupied()) {
                final Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOcupied() && rookTile.getPiece().isFirstMove()) {
                    kingCasltes.add(new QueenSideCastleMove(
                            board,
                            this.playerKing,
                            2,
                            (Rook) rookTile.getPiece(),
                            rookTile.getTileCoordinate(),
                            3)
                    );
                }
            }
        }
        return (kingCasltes);
    }
}
