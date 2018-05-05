package com.example.alideffo.exercise01;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.graphics.Color.rgb;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        TextWatcher, View.OnClickListener, SensorEventListener{

    private SeekBar barRed, barGreen, barBlue;
    private EditText redValue, greenValue, blueValue;
    private TextView preview;
    private int red, green, blue;
    private RadioGroup radioGroup;
    private RadioButton btnColor, backColor, txtColor, fColor;
    private Button button;
    private FloatingActionButton fab;
    //private Context context;
    private CheckBox checkBox;

    SensorManager sensorManager;
    Sensor sensor;
    boolean start = true;
    int xPos, yPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //context = getApplicationContext();



        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);




        red = green = blue = 0;

        //Red
        barRed = findViewById(R.id.barRed);
        redValue = findViewById(R.id.valueRed);
        barRed.setOnSeekBarChangeListener(this );
        redValue.addTextChangedListener(this);

        //Green
        barGreen = findViewById(R.id.barGreen);
        greenValue = findViewById(R.id.valueGreen);
        barGreen.setOnSeekBarChangeListener(this);
        greenValue.addTextChangedListener(this);

        //Blue
        barBlue = findViewById(R.id.barBlue);
        blueValue = findViewById(R.id.valueBlue);
        barBlue.setOnSeekBarChangeListener(this);
        blueValue.addTextChangedListener(this);

        preview = (TextView) findViewById(R.id.preview);
        radioGroup = (RadioGroup) findViewById(R.id.btnGroup);
        button = (Button) findViewById(R.id.confirmBtn);

        btnColor = (RadioButton) findViewById(R.id.btnColor);
        backColor = (RadioButton) findViewById(R.id.backColor);
        txtColor = (RadioButton) findViewById(R.id.txtColor);
        fColor = findViewById(R.id.actionBtnColor);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Information: Wipe to the right to dismiss...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button.setOnClickListener(this);
    }



    /**
     * Check whether a Button has been clicked or not.
     * @param v The view of our application
     * @return The id of the button, to let us know which button has been clicked.
     */
    public int btnClicked(View v){
        int id = radioGroup.getCheckedRadioButtonId();
        return id;
    }



    /**
     * Set the Color of our TextView "Preview Color"
     */
    public void previewColorChange() {
        preview.setBackgroundColor(rgb(red, green, blue));
        preview.setTextColor(rgb(255-red, 255-green, 255-blue)); //Invert the RGB
    }



    @Override
    public void onProgressChanged(SeekBar bar, int progress, boolean fromUser){
        if(bar.equals(barRed)){
            while(fromUser) {
                redValue.setText(String.valueOf(progress));
                fromUser = !fromUser;
            }
        }
        if(bar.equals(barGreen)){
            while(fromUser) {
                greenValue.setText(String.valueOf(progress));
                fromUser = !fromUser;
            }
        }
        if(bar.equals(barBlue)){
            while(fromUser) {
                blueValue.setText(String.valueOf(progress));
                fromUser = !fromUser;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        try{
            int value = Integer.parseInt(s.toString());

            if(value >= 0 && value <= 255){
                if(redValue.getText().toString().equals(s.toString())){
                    barRed.setProgress(value);
                    red = value;
                    previewColorChange();
                }
                if(greenValue.getText().toString().equals(s.toString())){
                    barGreen.setProgress(value);
                    green = value;
                    previewColorChange();
                }
                if(blueValue.getText().toString().equals(s.toString())){
                    barBlue.setProgress(value);
                    blue = value;
                    previewColorChange();
                }
            }
        } catch (Exception e){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            changeToAct2();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * Function gives us the possibility to change to the second Activity. Where the selected color
     * is shown in Hex. And the value of our red,green,blue are also store in this function.
     */
    private void changeToAct2() {
        Intent intent = new Intent(this,Main2Activity.class);
        intent.putExtra("Red", red);
        intent.putExtra("Green", green);
        intent.putExtra("Blue", blue);
        startActivity(intent);
    }



    @Override
    public void onClick(View v) {
        int id = btnClicked(v);

        if(id == btnColor.getId()){
            button.setBackgroundColor(rgb(red,green,blue));
        }
        if(id == backColor.getId()){
            getWindow().getDecorView().setBackgroundColor(rgb(red,green,blue));   //https://stackoverflow.com/questions/4761686/how-to-set-background-color-of-an-activity-to-white-programmatically
        }
        if(id == txtColor.getId()){
            final CheckBox cbText = findViewById(R.id.checkbox);
            final RadioButton txtText = (RadioButton) findViewById(R.id.txtColor);
            final RadioButton backText = (RadioButton) findViewById(R.id.backColor);
            final RadioButton btnText = (RadioButton) findViewById(R.id.btnColor);
            final RadioButton fBtnText = (RadioButton) findViewById(R.id.actionBtnColor);

            cbText.setTextColor(rgb(red,green,blue));
            txtText.setTextColor(rgb(red,green,blue));
            backText.setTextColor(rgb(red,green,blue));
            btnText.setTextColor(rgb(red,green,blue));
            fBtnText.setTextColor(rgb(red,green,blue));
        }
        if(id == fColor.getId()){
            fab.setBackgroundTintList(ColorStateList.valueOf(rgb(red,green,blue)));
        }
    }

    /*public Context getContext() {
        return context;
    }
    */

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }





    @Override
    protected void onResume() {
    super.onResume();
    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
}

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, sensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xCurrent = event.values[0];
        float yCurrent = event.values[1];

        xPos += xCurrent;
        yPos += yCurrent;

        checkBox = findViewById(R.id.checkbox);
        if (checkBox.isChecked()) {
            if (yPos >= 0 && yPos < 900) {
                if (yPos >= 0 && yPos < 300) {
                    if (xPos >= 0 && xPos <= 255) {
                        System.out.println("yCurrent1: " + yCurrent);
                        System.out.println("yPos1: " + yPos);
                        barRed.setProgress(xPos);
                        redValue.setText(String.valueOf(xPos));
                    }
                }
                if (yPos >= 300 && yPos < 600) {
                    if (xPos >= 0 && xPos <= 255) {
                        System.out.println("yCurrent1: " + yCurrent);
                        System.out.println("yPos1: " + yPos);
                        barGreen.setProgress(xPos);
                        greenValue.setText(String.valueOf(xPos));
                    }
                }
                if (yPos >= 600 && yPos < 900) {
                    if (xPos >= 0 && xPos <= 255) {
                        System.out.println("yCurrent1: " + yCurrent);
                        System.out.println("yPos1: " + yPos);
                        barBlue.setProgress(xPos);
                        blueValue.setText(String.valueOf(xPos));
                    }
                }
            } else {
                yPos = 0;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);
    }

}
