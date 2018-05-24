package com.example.manol.chessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class OptionsActivity extends Activity {
    NumberPicker numberPicker;
    int numberDepth;
    boolean isWhite;
    int engineNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);
        this.numberPicker = (NumberPicker) findViewById(R.id.numberDepth);
        this.numberPicker.setMinValue(0);
        this.numberPicker.setMaxValue(10);
        this.numberPicker.setWrapSelectorWheel(false);
    }


    public void setOptions(View view) {
        numberDepth = this.numberPicker.getValue();
        isWhite= ((RadioButton) findViewById(R.id.white)).isChecked();
        engineNumber = 1; //doar 1 exista acum;
        System.out.println("depth:" + numberDepth+" ");
        if (isWhite) {
            System.out.println("Culoara jucatorului este alb");
        } else {
            System.out.println("Culoara jucatorului este negru");
        }
        finish();
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("depth", numberDepth);
        if (isWhite) {
            data.putExtra("color", 1);
        } else {
            data.putExtra("color", 0);
        }
        data.putExtra("engine",engineNumber);

        setResult(RESULT_OK, data);
        super.finish();
    }
}
