package com.example.PomocnaLiterka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import sjpapi.api.SjpAPI;

public class ArbiterActivity extends AppCompatActivity {


    private Boolean tournamentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.arbiter_activity);
        final TextView meaningOfWordText = findViewById(R.id.meaningOfWord);
        final Button goButton = findViewById(R.id.goButton);
        final EditText nameOfWord = findViewById(R.id.nameOfWord);
        final LinearLayout linearLayout = findViewById(R.id.linear);
        final ImageButton settings = findViewById(R.id.action_settings_image);

        goButton.setEnabled(false);

        nameOfWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                goButton.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        goButton.setOnClickListener(v -> {
            final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean tournamentMode=(mSharedPreference.getBoolean("tournamentMode", true));
            try {
                linearLayout.removeAllViews();
                meaningOfWordText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String wordToFind = nameOfWord.getText().toString();
                String jsonOutput = SjpAPI.getWord(wordToFind);


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
                        if(tournamentMode == true) {
                            use.setGravity(Gravity.CENTER_VERTICAL);
                            use.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            use.setTextSize(20);
                            use.setText("Niedopuszczalne");
                        }
                        else
                            use.setText("Nie znaleziono w słowniku");
                        linearLayout.addView(use);
                    }
                    if (tournamentMode == true && count > 1)
                        count = 1;

                    for (int i = 0; i < count; i++) {
                        String canBeUsed = jsonObject.get("canBeUsed"+"["+i+"]").toString();
                        String meaningOf = jsonObject.get("meaning"+"["+i+"]").toString();
                        String variant = jsonObject.get("variant"+"["+i+"]").toString();

                        if (canBeUsed.equals("true")) {
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

                        if (tournamentMode == false) {
                            TextView varia = new TextView(this);
                            varia.setText("Od słowa: " + variant);
                            varia.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            varia.setTextSize(15);
                            varia.setGravity(Gravity.CENTER_VERTICAL);
                            linearLayout.addView(varia);


                            TextView mean = new TextView(this);
                            if (meaningOf.contentEquals("BAD FORMAT")) {
                                mean.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                mean.setTextSize(13);
                                mean.setGravity(Gravity.CENTER_VERTICAL);
                                mean.setText("\n" + "Po więcej informacji zapraszam na stronę: ");
                                linearLayout.addView(mean);
                                TextView meanLink = new TextView(this);

                                meanLink.setTextSize(15);
                                meanLink.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                meanLink.setTextColor(Color.BLUE);
                                String url = "https://www.sjp.pl/" + wordToFind.toLowerCase();
                                SpannableString content = new SpannableString(url + "\n");
                                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                meanLink.setText(content);
                                meanLink.setMovementMethod(LinkMovementMethod.getInstance());
                                meanLink.setGravity(Gravity.CENTER_VERTICAL);
                                meanLink.setOnClickListener(v1 -> {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                    browserIntent.setData(Uri.parse(url));
                                    startActivity(browserIntent);
                                });
                                linearLayout.addView(meanLink);
                            } else {
                                mean.setText(meaningOf + "\n");
                                mean.setGravity(Gravity.CENTER_VERTICAL);
                                linearLayout.addView(mean);
                            }
                        }
                    }
                } catch (JSONException err) {
                    Log.d("Error", err.toString());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        });

        settings.setOnClickListener(v -> {Intent intent = new Intent(ArbiterActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

}
