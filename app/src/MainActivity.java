package com.example.PomocnaLiterka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button finder = (Button)findViewById(R.id.finder);
        final Button stoper = (Button)findViewById(R.id.stoper);
        final Button rules = (Button)findViewById(R.id.rules);
        final Button arbiter = (Button)findViewById(R.id.arbiter);
        final Button points = (Button)findViewById(R.id.stoper2);

        finder.setEnabled(false);
        rules.setEnabled(false);
        points.setEnabled(false);

        arbiter.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ArbiterActivity.class);
            startActivity(intent);
        });
        stoper.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StoperActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent,0);
        });
    }
}
