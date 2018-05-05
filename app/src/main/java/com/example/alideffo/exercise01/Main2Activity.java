package com.example.alideffo.exercise01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.rgb;

public class Main2Activity extends AppCompatActivity{

    private Button okAct2;
    private Button cancelAct2;

    private TextView message;

    private int red, green, blue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        okAct2 = findViewById(R.id.ok);
        cancelAct2 = findViewById(R.id.cancel);

        //Onclick Listener for the OK Button.
        okAct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToOne(v);
                Toast.makeText(v.getContext(), "About Activity was closed using OK", Toast.LENGTH_LONG).show();

            }
        });


        //Onclick Listener for the Cancel Button.
        cancelAct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToOne(v);
                Toast.makeText(v.getContext(), "About Activity was closed using CANCEL", Toast.LENGTH_LONG).show();

            }
        });

        message = findViewById(R.id.acTwoMsg);

        red = getIntent().getExtras().getInt("Red");
        green = getIntent().getExtras().getInt("Green");
        blue = getIntent().getExtras().getInt("Blue");

        message.setBackgroundColor(rgb(red,green,blue));
        message.setText("The value of the selected color is (R,G,B)=" + "("
                + Integer.toHexString(red).toUpperCase() + ","
                + Integer.toHexString(green).toUpperCase()  + ","
                + Integer.toHexString(blue).toUpperCase()  +")");
    }

    public void backToOne(View v) {
        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        Toast.makeText(this, "Close by pressing one of the buttons", Toast.LENGTH_LONG).show();
    }
}
