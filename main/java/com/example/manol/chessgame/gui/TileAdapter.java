package com.example.manol.chessgame.gui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.manol.chessgame.R;
import com.example.manol.chessgame.StartGameActivity;
import com.example.manol.chessgame.board.Board;
import com.example.manol.chessgame.board.BoardUtils;
import com.example.manol.chessgame.board.Move;
import com.example.manol.chessgame.board.Tile;
import com.example.manol.chessgame.pieces.King;
import com.example.manol.chessgame.pieces.Piece;
import com.example.manol.chessgame.player.MoveTransition;
import com.example.manol.chessgame.player.ai.MinMax;
import com.example.manol.chessgame.player.ai.MoveStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TileAdapter extends BaseAdapter {

    private final int depth;
    private Board board;
    private Context mContext;
    private LayoutInflater mInflater;
    private GridView gridView;
    private boolean isPieceTouch;
    private TileAdapter mAdapter = this;
    private MoveStrategy minmax=new MinMax(1);
    private FrameLayout fl2;
    private ImageView imgv2;
    private Tile currentTile = null;

    private FrameLayout flcp;
    private ImageView imgvcp = null;

    // CHESSBOARD
// references to our images
    private Integer[] chessboardIds = {
            R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare,
            R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare,
            R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare,
            R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare,
            R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare,
            R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare,
            R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare,
            R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare, R.drawable.darksquare, R.drawable.lightsquare,
    };

    static class ViewHolder {
        public ImageView square;
        public ImageView piece;
    }


    public TileAdapter(Context c, Board board, GridView gridView,final int depth) {
        mContext = c;
        Context context = c.getApplicationContext();
        mInflater = LayoutInflater.from(context);
        this.board = board;
        this.gridView = gridView;
        this.depth = depth;
    }

    @Override
    public int getCount() {
        return BoardUtils.NUM_TILES;
    }

    @Override
    public Object getItem(int arg0) {
        return board.getTile(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {  // if it's not recycled, initialize some attributes
            rowView = mInflater.inflate(R.layout.square, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.square = (ImageView) rowView.findViewById(R.id.square_background);
            viewHolder.square.setImageResource(chessboardIds[position]);
            final Tile tile = board.getTile(position);
            viewHolder.piece = (ImageView) rowView.findViewById(R.id.piece);
            if (tile.isTileOcupied()) {
                viewHolder.piece.setImageResource(board.getTile(position).getPiece().getRessource());
            }
            // Assign the touch listener to your view which you want to move
            viewHolder.piece.setOnTouchListener(new MyTouchListener());
            viewHolder.piece.setOnDragListener(new MyDragListener());
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.piece.setTag(position);
        //System.out.println("tag pus aici:"+holder.piece.getTag());
        return rowView;
    }

    // This defines your touch listener
    private final class MyTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);

                //view.setVisibility(View.INVISIBLE);

                flcp = (FrameLayout) view.getParent();
                imgvcp = (ImageView) flcp.getChildAt(1);
                int tileNumber = (int) view.getTag();
                currentTile = board.getTile(tileNumber);
                if (currentTile.isTileOcupied()) {
                    isPieceTouch = true;
                }
                return true;
            } else {
                return false;
            }
        }

    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    if (isPieceTouch && currentTile.isTileOcupied()) {
                        final Piece currentPiece = currentTile.getPiece();
                        final int destinationCoordiante = (int)view.getTag();
                        Move newMove = Move.MoveFactory.createMove(board, currentPiece.getPiecePosition(), destinationCoordiante);
                        final MoveTransition transition = board.currentPlayer().makeMove(newMove);
                        if(transition.getMoveStatus().isDone()){
                            board = transition.getTransitionBoard();
                            mAdapter.notifyDataSetChanged();
                            gridView.setAdapter(mAdapter);
                            Move moveMadeAI =  minmax.execute(board);
                            final MoveTransition transition2 = board.currentPlayer().makeMove(moveMadeAI);
                            if(transition2.getMoveStatus().isDone()){
                                board = transition2.getTransitionBoard();
                            }
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}