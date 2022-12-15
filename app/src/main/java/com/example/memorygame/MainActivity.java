package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //region Initialization
    private List<Integer> sequence = new ArrayList<>();
    private TextView tvSequence;
    TextView tvState;
    TextView tvScore;
    int score =0;
    Animation anim;
    ImageButton btnNorth, btnEast, btnSouth, btnWest;

    private enum PlayState {learn, play, wait}
    PlayState playState = PlayState.wait;
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
        changeState(PlayState.wait);
    }
    //endregion

    //region Random number sequencing
    private void AddToSequence(){
        int i = RandomNumber();
        sequence.add(i);
        String s = tvSequence.getText().toString();
        s += i;
        tvSequence.setText(s);
    }
    private int RandomNumber(){
        return ((int)(Math.random() * 4));
    }
    //endregion

    //region Button on click events
    public void doStart(View view) {
        if(playState==PlayState.wait)
            newGame();
    }
    public void doWin(View view) {
        if(playState==PlayState.play){
            score+=100;
            tvScore.setText(String.valueOf(score));
            NewRound();}
    }
    public void doLose(View view) {
        if(playState==PlayState.play)
            changeState(PlayState.wait);
    }
    //endregion

    //region Game logic
    private void newGame(){
        sequence.clear();
        tvScore.setText("");
        tvSequence.setText("");
        score =0;
        NewRound();
    }
    private void NewRound(){
        changeState(PlayState.learn);
        AddToSequence();
        AddToSequence();
        currentIndex=0;
        buttonFlashHandler.post(r);
    }
    private void changeState(PlayState newState){
        playState=newState;
        tvState.setText(playState.toString());
    }
    //endregion

    //region Button flashing
    int currentIndex = 0; // used to track which button should flash in the sequence
    long flashDelay = 1000; // delay between button flashes
    Handler buttonFlashHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            // if the current index is within the bounds of the sequence
            if(currentIndex<sequence.size())
            {
                FlashButton(sequence.get(currentIndex)); // play the flash animation for the button at current index
                currentIndex++;
                buttonFlashHandler.postDelayed(this,flashDelay); // call again after delay
            }
            else{
                changeState(PlayState.play);
                buttonFlashHandler.removeCallbacks(r);}
        }
    };
    private void FlashButton(int i){
        switch(i){
            case 0:
                // north
                flashButton(btnNorth);
                break;
            case 1:
                // east
                flashButton(btnEast);
                break;
            case 2:
                // south
                flashButton(btnSouth);
                break;
            case 3:
                // west
                flashButton(btnWest);
                break;
        }

    }
    private void flashButton(ImageButton button) {

        anim = new AlphaAnimation(1,0);
        anim.setDuration(1000); //You can manage the blinking time with this parameter

        anim.setRepeatCount(0);
        button.startAnimation(anim);
    }
    //endregion
}