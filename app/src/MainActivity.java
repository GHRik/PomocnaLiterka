package com.pomocnaliterka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        finder.setEnabled(false);
        stoper.setEnabled(false);
        rules.setEnabled(false);

        arbiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ArbiterActivity.class);
                startActivity(intent);
            }
        });
    }
}