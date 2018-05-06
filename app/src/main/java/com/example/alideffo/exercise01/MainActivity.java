package com.example.alideffo.exercise01;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.widget.SeekBar;
import android.widget.TextView;

import static android.graphics.Color.rgb;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        TextWatcher, View.OnClickListener, SensorEventListener{

    private SeekBar barRed, barGreen, barBlue;
    private EditText redValue, greenValue, blueValue;
    private TextView preview;
    private int red, green, blue;
    private RadioGroup radioGroup;
    private RadioButton btnColor, backColor, txtColor, fColor; //Differents Radiobuttons IDs.
    private Button button; // The confirm Button
    private FloatingActionButton fab; // The Floating button
    private CheckBox checkBox;

    private CheckBox cbText;
    private RadioButton txtText;
    private RadioButton backText;
    private RadioButton btnText;
    private RadioButton fBtnText;
    private int btnChoice;

    SensorManager sensorManager;
    Sensor sensor;
    boolean start = true;
    int xPos, yPos;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int btnRed, btnGreen, btnBlue;
    private int backRed, backGreen, backBlue;
    private int txtRed, txtGreen, txtBlue;
    private int fRed, fGreen, fBlue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initComponent();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Information: Wipe to the right to dismiss...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        button.setOnClickListener(this);

        retrieveData();

    }

    /**
     * This just update the data when our onCreate funtion is called again. It used the data from
     * the retrieveData Function.
     */
    private void updateData() {
        redValue.setText(String.valueOf(red));
        greenValue.setText(String.valueOf(green));
        blueValue.setText(String.valueOf(blue));

        getWindow().getDecorView().setBackgroundColor(rgb(backRed, backGreen, backBlue));
        button.setBackgroundColor(rgb(btnRed, btnGreen, btnBlue));

        cbText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
        txtText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
        backText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
        btnText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
        fBtnText.setTextColor(rgb(txtRed,txtGreen,txtBlue));

        fab.setBackgroundTintList(ColorStateList.valueOf(rgb(fRed,fGreen,fBlue)));

        radioGroup.check(btnChoice);
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
        preview.setTextColor(rgb(255-red, 255-green, 255-blue/2)); //Invert the RGB
    }



    @Override
    public void onProgressChanged(SeekBar bar, int progress, boolean fromUser){
        if(bar.equals(barRed)){
            while(fromUser) {
                redValue.setText(String.valueOf(progress));
                fromUser = !fromUser;   //in other to set the cursor at the end of the last entered int
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
                saveData();
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


    /**
     * To handle the Confirm button click. The id of the buttons are knowned.
     * @param v The view of the application.
     */
    @Override
    public void onClick(View v) {
        int id = btnClicked(v);             //See the id of the selected radio button. (1 - 4)
        btnChoice = id;                     //Save the current id in the variable btnChoice.
        if(id == btnColor.getId()){
            btnRed = red; btnGreen = green; btnBlue = blue;
            button.setBackgroundColor(rgb(btnRed,btnGreen,btnBlue));
        }
        if(id == backColor.getId()){
            backRed = red; backGreen = green; backBlue = blue;
            getWindow().getDecorView().setBackgroundColor(rgb(backRed,backGreen,backBlue));   //https://stackoverflow.com/questions/4761686/how-to-set-background-color-of-an-activity-to-white-programmatically
        }
        if(id == txtColor.getId()){
            txtRed = red; txtGreen = green; txtBlue = blue;
            cbText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
            txtText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
            backText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
            btnText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
            fBtnText.setTextColor(rgb(txtRed,txtGreen,txtBlue));
        }
        if(id == fColor.getId()){
            fRed = red; fGreen = green; fBlue = blue;
            fab.setBackgroundTintList(ColorStateList.valueOf(rgb(fRed,fGreen,fBlue)));
        }
        saveData();
    }

    /**
     * The sensor Manager Handeling
     * @param event The position/move of the phone
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        float xCurrent = event.values[0];
        float yCurrent = event.values[1];

        xPos += xCurrent;
        yPos += yCurrent;

        checkBox = findViewById(R.id.checkbox);
        if (checkBox.isChecked()) {
            if (yPos >= 0 && yPos < 2800) {
                if (yPos >= 0 && yPos < 1000) {
                    if (xPos >= 0 && xPos <= 255) {
                        System.out.println("X: " + xPos);
                        System.out.println("Y: " + yPos);
                        //System.out.println("Z: " + zPos);
                        barRed.setProgress(xPos);
                        redValue.setText(String.valueOf(xPos));
                    }
                }
                if (yPos >= 1000 && yPos < 1900) {
                    if (xPos >= 0 && xPos <= 255) {
                        System.out.println("X: " + xPos);
                        System.out.println("Y: " + yPos);
                        //System.out.println("Z: " + zPos);
                        barGreen.setProgress(xPos);
                        greenValue.setText(String.valueOf(xPos));
                    }
                }
                if (yPos >= 1900 && yPos < 2800) {
                    if (xPos >= 0 && xPos <= 255) {
                        System.out.println("X: " + xPos);
                        System.out.println("Y: " + yPos);
                        //System.out.println("Z: " + zPos);
                        barBlue.setProgress(xPos);
                        blueValue.setText(String.valueOf(xPos));
                    }
                }
            } else {
                yPos = 0;
            }
        }
    }



    /**
     * All the Data/Values of the application are saved here. This Function is usefull for our
     * SharedPreferences to retrieve our data. Even if the application is closed.
     */
    private void saveData() {
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //The main color (Values in the EditText)
        editor.putInt("red", red);
        editor.putInt("green", green);
        editor.putInt("blue", blue);

        //The buttons Color values
        editor.putInt("redBtn", btnRed);
        editor.putInt("greenBtn", btnGreen);
        editor.putInt("blueBtn", btnBlue);

        //The Background color values
        editor.putInt("redBack", backRed);
        editor.putInt("greenBack", backGreen);
        editor.putInt("blueBack", backBlue);

        //The Text color values
        editor.putInt("redTxt", txtRed);
        editor.putInt("greenTxt", txtGreen);
        editor.putInt("blueTxt", txtBlue);

        //The floating Button color values
        editor.putInt("redF", fRed);
        editor.putInt("greenF", fGreen);
        editor.putInt("blueF", fBlue);

        //The choice of the radio buttons
        editor.putInt("choice", btnChoice);

        editor.commit();
    }


    /**
     * To retrieve all the data, which are stored in the saveData function.
     */
    private void retrieveData() {
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

        red = sharedPreferences.getInt("red", 20);
        green = sharedPreferences.getInt("green", 89);
        blue = sharedPreferences.getInt("blue", 200);

        btnBlue = sharedPreferences.getInt("blueBtn", btnBlue);
        btnRed = sharedPreferences.getInt("redBtn", btnRed);
        btnGreen = sharedPreferences.getInt("greenBtn", btnGreen);

        backRed = sharedPreferences.getInt("redBack", backRed);
        backGreen = sharedPreferences.getInt("greenBack", backGreen);
        backBlue = sharedPreferences.getInt("blueBack", backBlue);

        txtRed = sharedPreferences.getInt("redTxt", 0);
        txtGreen = sharedPreferences.getInt("greenTxt", 0);
        txtBlue = sharedPreferences.getInt("blueTxt", 0);

        fRed = sharedPreferences.getInt("redF", 0);
        fGreen = sharedPreferences.getInt("greenF", 0);
        fBlue = sharedPreferences.getInt("blueF", 0);

        btnChoice = sharedPreferences.getInt("choice", 1);

        updateData();
    }



    /**
     * Here are all the components which are used in our software. They are all initialised at the
     * beginning of our program.
     */
    private void initComponent() {
        //All components for the red Color (SeekBar and Value)
        barRed = findViewById(R.id.barRed);
        redValue = findViewById(R.id.valueRed);
        barRed.setOnSeekBarChangeListener(this );
        redValue.addTextChangedListener(this);

        //All components for the green Color (SeekBar and Value)
        barGreen = findViewById(R.id.barGreen);
        greenValue = findViewById(R.id.valueGreen);
        barGreen.setOnSeekBarChangeListener(this);
        greenValue.addTextChangedListener(this);

        //All components for the blue Color (SeekBar and Value)
        barBlue = findViewById(R.id.barBlue);
        blueValue = findViewById(R.id.valueBlue);
        barBlue.setOnSeekBarChangeListener(this);
        blueValue.addTextChangedListener(this);

        //Other components
        preview = (TextView) findViewById(R.id.preview);        // For our 'Preview Color Textview'
        radioGroup = (RadioGroup) findViewById(R.id.btnGroup);  // The group of the radiobuttons
        button = (Button) findViewById(R.id.confirmBtn);        // The confirm button

        btnColor = (RadioButton) findViewById(R.id.btnColor);   // id of the CONFIRM BUTTON COLOR
        backColor = (RadioButton) findViewById(R.id.backColor); // id of the BACKGROUND COLOR
        txtColor = (RadioButton) findViewById(R.id.txtColor);   // id of the TEXT COLOR
        fColor = findViewById(R.id.actionBtnColor);             // id of the FLOATINGBUTTON COLOR

        //The id of all the text, which can be colored.
        cbText = findViewById(R.id.checkbox);
        txtText = (RadioButton) findViewById(R.id.txtColor);
        backText = (RadioButton) findViewById(R.id.backColor);
        btnText = (RadioButton) findViewById(R.id.btnColor);
        fBtnText = (RadioButton) findViewById(R.id.actionBtnColor);

    }

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
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
}
