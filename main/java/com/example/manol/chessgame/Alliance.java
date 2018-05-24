package com.example.manol.chessgame;

import com.example.manol.chessgame.board.BoardUtils;
import com.example.manol.chessgame.player.BlackPlayer;
import com.example.manol.chessgame.player.Player;
import com.example.manol.chessgame.player.WhitePlayer; /**
 * Created by manol on 15.05.2018.
 */

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EGHTH_RANK[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(int position);


    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
