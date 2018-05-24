package com.example.manol.chessgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.gui.TileAdapter;
import com.example.manol.chessgame.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import static com.example.manol.chessgame.R.layout.activity_board;

@SuppressLint("Registered")
public class StartGameActivity extends Activity {

    private GridView chessboardGridView;
    private final int gameStatus = 0; //draw
    private int depth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extraInfo = getIntent().getExtras();
        this.depth = extraInfo.getInt("depth");
        startGame();
    }

    private void startGame() {
        setContentView(activity_board);
        Board board = Board.createStandardBoard();
        chessboardGridView = (GridView)findViewById (R.id.gridview);
        TileAdapter tileAdapter =  new TileAdapter(this,board,chessboardGridView,depth);
        chessboardGridView.setAdapter(tileAdapter);

    }


    @Override
    public void finish() {
        super.finish();
    }
}
