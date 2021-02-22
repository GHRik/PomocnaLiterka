package scrabble.helper;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import com.sjp.sjpapi.SjpAPI;

public class MainActivity extends AppCompatActivity {

    private SjpAPI sjpApi = new SjpAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView meaningOfWordText = (TextView)findViewById(R.id.meaningOfWord);
        final Button goButton = (Button)findViewById(R.id.goButton);
        final EditText nameOfWord = (EditText)findViewById(R.id.nameOfWord);

        goButton.setOnClickListener(v -> {
            try {
                meaningOfWordText.setText(sjpApi.getWord(nameOfWord.getText().toString()));
                Log.i("meaning",meaningOfWordText.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

}