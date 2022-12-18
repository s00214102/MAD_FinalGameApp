package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //region Initialization
    public TextView tvSequence, tvState, tvScore;
    public ImageButton btnNorth, btnEast, btnSouth, btnWest;
    private Game game;

    // tilt controls
    private int maxTilt = 3; // how much you need to tilt to register up/down/left/right
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isFlat = false; // set to true when the phone is flat, false when tilted

    public AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvState = findViewById(R.id.tvPlayState);
        tvScore = findViewById(R.id.tvScore);
        btnNorth = findViewById(R.id.btnNorth);
        btnEast = findViewById(R.id.btnEast);
        btnSouth = findViewById(R.id.btnSouth);
        btnWest = findViewById(R.id.btnWest);
        tvSequence = findViewById(R.id.tvSequence);

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        game = new Game(this);
        // sensor setup
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    //endregion

    //region Button on click events
    public void doStart(View view) {
        game.newGame();
    }
    public void doWin(View view) {
            game.winRound();
    }
    public void doLose(View view) {
        if(game.playState != Game.PlayState.play){
            return;}
        // create intent and pass score
        Intent A = new Intent(view.getContext(), ScoreActivity.class);
        A.putExtra("score",game.score);

        game.loseGame(); // lose the round, resets play data

        startActivity(A); // move to score screen
    }
    //endregion

    //region Sensor
    @Override
    public void onSensorChanged(SensorEvent event) {
        // called byt the system every x ms
        // test values for debugging, can be deleted
        float x, y;
        x = event.values[0];
        y = event.values[1];
        // note: Z isn't used

        // checks if the phone is flat
        // not using the maxTilt value here because it was resetting to flat too easily otherwise
        if (x < 1 && x > -1 && y < 1 && y > -1) {
            if (isFlat == false) {
                isFlat = true;
            }
        }
        // up tilt
        if (x < -maxTilt) {
            if (isFlat) {
                isFlat = false;
                // add your own outcomes here
                game.checkInput(0);
            }
        }
        // right tilt
        if (y > maxTilt) {
            if (isFlat) {
                isFlat = false;
                // add your own outcomes here
                game.checkInput(1);
            }
        }
        // down tilt
        if (x > maxTilt) {
            if (isFlat) {
                isFlat = false;
                // add your own outcomes here
                game.checkInput(2);
            }
        }
        // left tilt
        if (y < -maxTilt) {
            if (isFlat) {
                isFlat = false;
                // add your own outcomes here
                game.checkInput(3);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        if(game.playState== Game.PlayState.play){
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);}
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        // turn off listener to save power
    }
    public void toggleSensor(boolean value){
        if(value){
            if(game.playState== Game.PlayState.play){
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);}
        }
        else{
            mSensorManager.unregisterListener(this);
        }
    }
    //endregion

}