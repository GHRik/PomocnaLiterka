package com.example.PomocnaLiterka;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CustomDialog extends Dialog {


    int[] points = {0,0,0,0};
    String[] names ={"g1","g2","g3","g4"};
    TextView g1,g2,g3,g4;

    OnMyDialogResult mDialogResult;

    public CustomDialog(Context context, int[] points, String[] names) {
        super(context);
        this.points = points;
        this.names = names;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        g1 = findViewById(R.id.textView2);
        g2 = findViewById(R.id.textView3);
        g3 = findViewById(R.id.textView4);
        g4 = findViewById(R.id.textView6);

        if(points[0] == 0)
        {
            g1.setVisibility(View.INVISIBLE);
        }
        if (points[1] == 0){
            g2.setVisibility(View.INVISIBLE);
        }
        if(points[2] == 0){
            g3.setVisibility(View.INVISIBLE);
        }
        if(points[3] == 0){
            g4.setVisibility(View.INVISIBLE);
        }

        g1.setText(names[0]+": "+Integer.toString(points[0]));
        g2.setText(names[1]+": "+Integer.toString(points[1]));
        g3.setText(names[2]+": "+Integer.toString(points[2]));
        g4.setText(names[3]+": "+Integer.toString(points[3]));
    }

    private class OKListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            CustomDialog.this.dismiss();
        }
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String result);
    }
}
