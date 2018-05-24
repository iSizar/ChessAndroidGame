package com.example.manol.chessgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int numberDepth;
    boolean isWhite;
    int engineNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnStartGameBtnClick(View view) {
        Intent start_game = new Intent(this, StartGameActivity.class);
        start_game.putExtra("depth", numberDepth);
        if (isWhite) {
            start_game.putExtra("color", 1);
        } else {
            start_game.putExtra("color", 0);
        }
        start_game.putExtra("engine",engineNumber);
        startActivity(start_game);
    }

    public void OnOptionsBtnClick(View view) {
        Intent options = new Intent(this, OptionsActivity.class);
        startActivityForResult(options, 123);
    }

    public void OnExitBtnClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure that you want to exit?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == RESULT_OK) && (requestCode == 123)){
            numberDepth = data.getIntExtra("depth",1);
            this.isWhite = data.getIntExtra("color", 1) == 1;
            this.engineNumber = data.getIntExtra("engine",1);
        }
    }
}
