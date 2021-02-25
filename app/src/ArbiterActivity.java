package com.pomocnaliterka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjp.SjpAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ArbiterActivity extends AppCompatActivity {

    private SjpAPI sjpApi = new SjpAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arbiter_activity);
        final TextView meaningOfWordText = (TextView)findViewById(R.id.meaningOfWord);
        final Button goButton = (Button)findViewById(R.id.goButton);
        final EditText nameOfWord = (EditText)findViewById(R.id.nameOfWord);
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);


        nameOfWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    goButton.setEnabled(false);
                } else {
                    goButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        goButton.setOnClickListener(v -> {
            try {
                linearLayout.removeAllViews();
                meaningOfWordText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String wordToFind = nameOfWord.getText().toString();
                String jsonOutput = sjpApi.getWord(wordToFind);


                try {
                    meaningOfWordText.setText(wordToFind);

                    if (jsonOutput.contentEquals("EMPTY")) {
                        TextView mean = new TextView(this);
                        mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        mean.setTextSize(25);
                        mean.setTextColor(Color.RED);
                        mean.setGravity(Gravity.CENTER_VERTICAL);
                        mean.setText("\n"+"BRAK POŁĄCZENIA Z SIECIĄ" +"\n");
                        linearLayout.addView(mean);
                    }
                    JSONObject jsonObject = new JSONObject(jsonOutput);

                    int count = Integer.parseInt(jsonObject.get("count").toString());


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
                            linearLayout.addView(mean);
                            TextView meanLink = new TextView(this);

                            meanLink.setTextSize(15);
                            meanLink.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            meanLink.setTextColor(Color.BLUE);
                            String url = "https://www.sjp.pl/"+wordToFind.toLowerCase();
                            SpannableString content = new SpannableString(url+"\n");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            meanLink.setText(content);
                            meanLink.setMovementMethod(LinkMovementMethod.getInstance());
                            meanLink.setGravity(Gravity.CENTER_VERTICAL);
                            meanLink.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                    browserIntent.setData(Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            });
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