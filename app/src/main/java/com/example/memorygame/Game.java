package com.example.memorygame;

import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Game {

    //region Initialization
    private MainActivity main;
    private Animation anim;
    public enum PlayState {learn, play, wait}
    public PlayState playState = PlayState.wait;
    private List<Integer> sequence = new ArrayList<>();
    private int currentInputIndex = 0;
    public int score =0;


    public Game(MainActivity main)
    {
        this.main=main;
        changeState(PlayState.wait);
    }
    //endregion

    //region Random number sequencing
    private void AddToSequence(){
        int i = RandomNumber();
        sequence.add(i);
        String s = main.tvSequence.getText().toString();
        s += i;
        main.tvSequence.setText(s);
    }
    private int RandomNumber(){
        return ((int)(Math.random() * 4));
    }
    //endregion

    //region Game logic
    public void newGame(){
        if(playState != PlayState.wait){
            return;}
        clearData();
        newRound();
    }
    private void newRound(){
        changeState(PlayState.learn);
        AddToSequence();
        AddToSequence();
        currentFlashIndex =0;
        buttonFlashHandler.post(r);
    }
    public void winRound(){
        if(playState!= PlayState.play){
            return;}

        main.toggleSensor(false);
        score+=100;
        main.tvScore.setText(String.valueOf(score));
        newRound();
    }
    public void checkInput(int i){
        // check if the input given by the player matches the number at the current index of the sequence
        Log.i("Game", "passed value i: "+String.valueOf(i));
        Log.i("Game", "currentInputIndex: "+String.valueOf(currentInputIndex));
        Log.i("Game", "sequence.size(): "+String.valueOf(sequence.size()));
        Log.i("Game", "sequence.get(currentInputIndex): "+String.valueOf(sequence.get(currentInputIndex)));
        if(currentInputIndex>=sequence.size()){
            return;}

        // if the input matches the current number in sequence
        if(i == sequence.get(currentInputIndex)){
            // pass
            Toast.makeText(main.getBaseContext(), "Correct!", Toast.LENGTH_LONG);
            FlashButton(currentInputIndex);
            currentInputIndex++;

            // if correct inputs were given for full sequence
            if(currentInputIndex>=sequence.size()) {
                winRound();
            }
        }
        // mismatch, lose
        else
            loseGame();

    }
    private void clearData(){
        sequence.clear();
        main.tvScore.setText("");
        main.tvSequence.setText("");
        score =0;
        currentInputIndex =0;
    }
    public void loseGame(){
        if(playState != PlayState.play){
            return;}
        main.toggleSensor(false);
        clearData();
        changeState(PlayState.wait);
    }
    private void changeState(PlayState newState){
        playState=newState;
        main.tvState.setText(playState.toString());
    }
    //endregion

    //region Button flashing
    int currentFlashIndex = 0; // used to track which button should flash in the sequence
    long flashDelay = 400; // delay between button flashes in ms
    Handler buttonFlashHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            // if the current index is within the bounds of the sequence
            if(currentFlashIndex <sequence.size())
            {
                FlashButton(sequence.get(currentFlashIndex)); // play the flash animation for the button at current index
                currentFlashIndex++;
                buttonFlashHandler.postDelayed(this,flashDelay); // call again after delay
            }
            else{
                changeState(PlayState.play);
                buttonFlashHandler.removeCallbacks(r);
                main.toggleSensor(true); // enable accelerometer
            }
        }
    };
    private void FlashButton(int i){
        switch(i){
            case 0:
                // north
                flashButton(main.btnNorth);
                break;
            case 1:
                // east
                flashButton(main.btnEast);
                break;
            case 2:
                // south
                flashButton(main.btnSouth);
                break;
            case 3:
                // west
                flashButton(main.btnWest);
                break;
        }

    }
    private void flashButton(ImageButton button) {

        anim = new AlphaAnimation(1,0);
        anim.setDuration(flashDelay); //You can manage the blinking time with this parameter

        anim.setRepeatCount(0);
        button.startAnimation(anim);
    }
    //endregion
}
