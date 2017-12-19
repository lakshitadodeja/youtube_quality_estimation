package application_new.afinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    // Initializing variables
    EditText input;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.editText);
        Button btnNextScreen = (Button) findViewById(R.id.button);
        Button b =(Button) findViewById(R.id.button2);

        //Listening to button event
        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), Main2Activity.class);

                //Sending data to another Activity
                nextScreen.putExtra("VideoId",input.getText().toString());

                startActivity(nextScreen);

            }
        });
        btnNextScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), Main4Activity.class);

                //Sending data to another Activity
                nextScreen.putExtra("VideoId",input.getText().toString());

                startActivity(nextScreen);

            }
        });

    }
}