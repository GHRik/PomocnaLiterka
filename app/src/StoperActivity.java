package com.example.PomocnaLiterka;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;
import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class StoperActivity extends AppCompatActivity {

    public int counter;
    Button button, stopButton, addRowButton, newGame, deleteRow, count;
    TextView textView;
    String[] timeMin = {"30sec","1min", "2min", "3min", "4min", "5min"}, names={"","","",""};
    Integer[] timeSec = {30,60, 120, 180, 240, 300};
    Integer whichIndex = 0;
    Spinner spinner;
    CountDownTimer CountDownTimer;
    TableLayout t1, tableName;

    boolean isRuning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoper);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        textView.setText(String.valueOf("02:00"));
        spinner = findViewById(R.id.spinner3);
        stopButton = findViewById(R.id.button2);
        TableLayout t1 = findViewById(R.id.tablelayout);
        addRowButton = findViewById(R.id.button4);
        newGame = findViewById(R.id.button3);
        deleteRow = findViewById(R.id.button6);
        count = findViewById(R.id.button5);
        CustomDialog dialog;


        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                timeMin);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

        spinner.setSelection(2);

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableName = findViewById(R.id.tableLayout);
                TableRow row = (TableRow)tableName.getChildAt(0);
                for (int i =0; i < 4; i++) {
                    EditText text = (EditText) row.getChildAt(i);
                    names[i] = String.valueOf(text.getText());
                }
                openDialog(countCoumn());
            }
        });

        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastRows();
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRows();
                addRow();
                addRow();
            }
        });

        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRuning == true) {
                    CountDownTimer.cancel();
                    button.setEnabled(true);
                    isRuning = false;
                    counter = timeSec[whichIndex];
                    int minutes = (counter % 3600) / 60;
                    int seconds = (counter % 3600) % 60;
                    if (seconds == 0) {
                        textView.setText("0"+String.valueOf(Integer.toString(minutes) + ":" + Integer.toString(seconds) + "0"));
                    } else {
                        textView.setText("0"+String.valueOf(Integer.toString(minutes) + ":" + Integer.toString(seconds)));
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setTextColor(Color.WHITE);
                button.setEnabled(false);
                isRuning = true;

                whichIndex = 0;
                for (int i = 0; i < timeMin.length; i++) {
                    if (spinner.getSelectedItem().toString().equals(timeMin[i])) {
                        whichIndex = i;
                        break;
                    }
                }
                counter = timeSec[whichIndex];


                CountDownTimer = new CountDownTimer(timeSec[whichIndex] * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int minutes = (counter % 3600) / 60;
                        int seconds = (counter % 3600) % 60;
                        if (seconds < 10){
                            textView.setText(String.valueOf("0"+Integer.toString(minutes) + ":" + "0"+Integer.toString(seconds)));
                        }
                        else if (seconds == 0) {
                            textView.setText(String.valueOf("0"+Integer.toString(minutes) + ":" + Integer.toString(seconds) + "0"));
                        } else {
                            textView.setText(String.valueOf("0"+Integer.toString(minutes) + ":" + Integer.toString(seconds)));
                        }
                        counter--;
                    }

                    public void onFinish() {
                        isRuning = false;
                        button.setEnabled(true);
                        textView.setTextColor(Color.RED);
                        textView.setText("00:00");

                    }
                }.start();
            }
        });
    }

    public void deleteRows()
    {
        t1 = findViewById(R.id.tablelayout);
        t1.removeAllViews();
    }

    public int[] countCoumn()
    {
        int[] playerPoint = {0,0,0,0};
        t1 = findViewById(R.id.tablelayout);
        int childCount = t1.getChildCount();
        for(int i = 0; i < 4; ++i){
            for (int j = 0; j < childCount; ++j){
                TableRow row = (TableRow)t1.getChildAt(j);
                EditText text = (EditText)row.getChildAt(i);
                if(text.getText() != null && text.getText().length() != 0){
                    playerPoint[i] += Integer.parseInt(String.valueOf(text.getText()));
                }
            }
        }

        return playerPoint;
    }

    public void deleteLastRows()
    {
        t1 = findViewById(R.id.tablelayout);
        int childCount = t1.getChildCount();
        TableRow row = (TableRow)t1.getChildAt(childCount-1);
        t1.removeView(row);
    }

    public void addRow()
    {
        t1 = findViewById(R.id.tablelayout);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        EditText text = new EditText(this);
        text.setEms(4);
        text.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
        EditText text2 = new EditText(this);
        text2.setEms(4);
        text2.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
        EditText text3 = new EditText(this);
        text3.setEms(4);
        text3.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
        EditText text4 = new EditText(this);
        text4.setEms(4);
        text4.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_SIGNED);
        int maxLength = 3;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        text.setFilters(FilterArray);
        text2.setFilters(FilterArray);
        text3.setFilters(FilterArray);
        text4.setFilters(FilterArray);
        tr.addView(text);
        tr.addView(text2);
        tr.addView(text3);
        tr.addView(text4);
        t1.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        if (isRuning){
            CountDownTimer.cancel();
        }

        Intent intent = new Intent(StoperActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(intent,0);
    }

    @Override
    public void onResume() {

        super.onResume();
        if(isRuning){
            int valueToCount = timeSec[whichIndex] - counter;
            counter = timeSec[whichIndex] - valueToCount;
            CountDownTimer = new CountDownTimer(counter * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    int minutes = (counter % 3600) / 60;
                    int seconds = (counter % 3600) % 60;
                    if (seconds < 10){
                        textView.setText(String.valueOf("0"+Integer.toString(minutes) + ":" + "0"+Integer.toString(seconds)));
                    }
                    else if (seconds == 0) {
                        textView.setText(String.valueOf("0"+Integer.toString(minutes) + ":" + Integer.toString(seconds) + "0"));
                    } else {
                        textView.setText(String.valueOf("0"+Integer.toString(minutes) + ":" + Integer.toString(seconds)));
                    }
                    counter--;
                }

                public void onFinish() {
                    isRuning = false;
                    button.setEnabled(true);
                    textView.setTextColor(Color.RED);
                    textView.setText("00:00");
                }
            }.start();
        }
    }

    public void openDialog(int []pointsArray) {

        final Dialog dialog = new CustomDialog(this,pointsArray, names ); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Punkty");
        boolean isArrayEmpty = true;

        for(int i = 0; i < 4; i++){
            if(pointsArray[i] > 0) {
                isArrayEmpty = false;
                break;
            }
        }

        if(isArrayEmpty == false) {
            dialog.show();
        }
    }
}
