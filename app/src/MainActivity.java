package com.pomocnaliterka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjp.SjpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SjpAPI sjpApi = new SjpAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView meaningOfWordText = (TextView)findViewById(R.id.meaningOfWord);
        final Button goButton = (Button)findViewById(R.id.goButton);
        final EditText nameOfWord = (EditText)findViewById(R.id.nameOfWord);
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);


        goButton.setOnClickListener(v -> {
            try {
                String wordToFind = nameOfWord.getText().toString();
                String jsonOutput = sjpApi.getWord(wordToFind);
                try {
                    JSONObject jsonObject = new JSONObject(jsonOutput);
                    meaningOfWordText.setText(wordToFind);

                    int count = Integer.parseInt(jsonObject.get("count").toString());
                    linearLayout.removeAllViews();

                    if (count == -1){
                        TextView use=new TextView(this);
                        use.setText("Nie znaleziono w słowniku");
                        linearLayout.addView(use);
                    }

                    for (int i = 0; i < count; i++) {
                        String canBeUsed = jsonObject.get("canBeUsed"+"["+i+"]").toString();
                        String meaningOf = jsonObject.get("meaning"+"["+i+"]").toString();
                        String variant = jsonObject.get("variant"+"["+i+"]").toString();

                        if (canBeUsed == "true") {
                            canBeUsed = "Dopuszczalne";
                        }
                        else {
                            canBeUsed = "Niedopuszczalne";
                        }

                        TextView use=new TextView(this);
                        use.setText(canBeUsed);
                        use.setGravity(Gravity.CENTER_VERTICAL);
                        use.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        use.setTextSize(20);
                        linearLayout.addView(use);

                        TextView varia=new TextView(this);
                        varia.setText("Od słowa: "+variant);
                        varia.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        varia.setTextSize(15);
                        varia.setGravity(Gravity.CENTER_VERTICAL);
                        linearLayout.addView(varia);


                        TextView mean = new TextView(this);
                        if (meaningOf.contentEquals("BAD FORMAT")){
                            mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            mean.setTextSize(13);
                            mean.setGravity(Gravity.CENTER_VERTICAL);
                            mean.setText("\n"+"Po więcej informacji zapraszam na stronę: ");

                            TextView meanLink = new TextView(this);
                            meanLink.setMovementMethod(LinkMovementMethod.getInstance());
                            meanLink.setTextSize(15);
                            meanLink.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            meanLink.setText("https://www.sjp.pl/"+wordToFind+"\n");
                            meanLink.setGravity(Gravity.CENTER_VERTICAL);
                            linearLayout.addView(mean);
                            linearLayout.addView(meanLink);

                        }
                        else {
                            mean.setText(meaningOf + "\n");
                            mean.setGravity(Gravity.CENTER_VERTICAL);
                            linearLayout.addView(mean);
                        }
                    }


                } catch (JSONException err) {
                    Log.d("Error", err.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

}