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

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                 final Collection<Move> opponentsLegals) {
        final List<Move> kingCasltes = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //White King Side Castle
            if (!this.board.getTile(61).isTileOcupied() && !this.board.getTile(62).isTileOcupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOcupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAtackOnTile(61,opponentsLegals).isEmpty() &&
                            Player.calculateAtackOnTile(62,opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPiceType().isRook()) {
                        kingCasltes.add(new KingSideCastleMove(
                                board,
                                this.playerKing,
                                62,
                                (Rook) rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                61)
                        );
                    }
                }
            }
            //white King Qeen Side Caslte
            if (!this.board.getTile(57).isTileOcupied()
                    && !this.board.getTile(58).isTileOcupied()
                    && !this.board.getTile(59).isTileOcupied()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOcupied() && rookTile.getPiece().isFirstMove()) {
                    kingCasltes.add(new QueenSideCastleMove(
                            board,
                            this.playerKing,
                            58,
                            (Rook) rookTile.getPiece(),
                            rookTile.getTileCoordinate(),
                            59));
                }
            }
        }
        return (kingCasltes);
    }
}
