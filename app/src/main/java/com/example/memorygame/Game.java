package com.example.memorygame;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
    private String tag = "Game";
    public enum PlayState {learn, play, wait}
    public PlayState playState = PlayState.wait;
    private List<Integer> sequence = new ArrayList<>();
    private int currentInputIndex = 0;
    public int score =0;
    // Sound
    private SoundPool soundPool;
    int meow, boing, correct, incorrect, kicksnare, honk;

    public Game(MainActivity main) {
        this.main=main;
        changeState(PlayState.wait);
        // load sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        correct = soundPool.load(main, R.raw.correct,1);
        meow = soundPool.load(main, R.raw.meow,1);
        boing = soundPool.load(main, R.raw.boing,1);
        incorrect = soundPool.load(main, R.raw.incorrect,1);
        kicksnare = soundPool.load(main, R.raw.kicksnare,1);
        honk = soundPool.load(main, R.raw.honk,1);
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
    // calls newRound()
    public void newGame(){
        if(playState != PlayState.wait){
            return;}

        clearData();
        popupMessage("Get ready!");
        // add a delay before the new round starts
        final Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                newRound();}
        }, 1000);
    }

    // changes to learn state
    private void newRound(){
        changeState(PlayState.learn);
        AddToSequence();
        AddToSequence();
        currentFlashIndex =0;
        buttonFlashHandler.post(r);
    }

    // calls newRound()
    public void winRound(){
        if(playState!= PlayState.play){
            return;}

        playSound(correct);
        main.toggleSensor(false);
        score+=100;
        currentInputIndex=0;
        main.tvScore.setText(String.valueOf(score));
        // add a delay
        final Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                newRound();}
        }, 1000);
        //newRound();
    }

    // calls goToScoreScreenOrRestartGame()
    public void loseGame(){
        //if(playState != PlayState.play){
        //    return;}
        playSound(incorrect);
        main.toggleSensor(false);

        popupMessage("You lost..");

        // add a delay before calling the rest of the code
        final Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToScoreScreenOrRestartGame();
            }
        }, 900);
    }
    // changes to wait state
    private void goToScoreScreenOrRestartGame(){
        if(score>0){
            Intent A = new Intent(main, ScoreActivity.class);
            A.putExtra("score",score);
            clearData();
            main.startActivity(A); // move to score screen
            changeState(PlayState.wait);
        }
        else{
            clearData();
            changeState(PlayState.wait);
        }
    }

    private void clearData(){
        sequence.clear();
        main.tvScore.setText("");
        main.tvSequence.setText("");
        score =0;
        currentInputIndex =0;
    }
    //endregion

    //region States
    private void changeState(PlayState newState){
        playState=newState;
        main.tvState.setText(playState.toString());

        switch (playState) {
            case learn:
                enterLearnState();
                break;
            case play:
                enterPlayState();
                break;
            case wait:
                enterWaitState();
                break;
        }
    }
    private void enterLearnState(){
        Log.i(tag, "Entering learn state");
        main.showPlayButton(false);
        //main.showButtons(true);
    }
    private void enterPlayState(){
        Log.i(tag, "Entering play state");
        main.showPlayButton(false);
        //main.showButtons(true);
    }
    private void enterWaitState(){
        Log.i(tag, "Entering wait state");
        // hide everything button the play button
        main.showPlayButton(true);
    }
    //endregion

    private void popupMessage(String message){
        // hide buttons and show popup
        main.showButtons(false);
        main.showPlayButton(false);
        main.tvPopup.setText(message.toString());
        main.showPopupTextView(true);
        // after a delay, hide popup and show buttons
        final Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                main.showPopupTextView(false);
                main.showButtons(true);
                }
        }, 1000);
    }

    //region Sound
    private static MediaPlayer mp;
    private void playSong(int song) {
        mp = MediaPlayer.create(main, song);

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    private void playSound(int sound){
        soundPool.play(sound, 1,1,0,0,1);
        //soundPool.autoPause();
    }
    //endregion

    public void checkInput(int i){
        // check if the input given by the player matches the number at the current index of the sequence
        if(currentInputIndex>=sequence.size()){
            return;}

        Log.i("Game", "passed value i: "+String.valueOf(i));
        Log.i("Game", "sequence.get(currentInputIndex): "+String.valueOf(sequence.get(currentInputIndex)));
        Log.i("Game", "currentInputIndex: "+String.valueOf(currentInputIndex));
        Log.i("Game", "sequence.size(): "+String.valueOf(sequence.size()));
        Log.i("Game", "-----------------------------------------------------------------------------");

        // if the input matches the current number in sequence
        if(i == sequence.get(currentInputIndex)){
            // pass
            Toast.makeText(main, "Correct!", Toast.LENGTH_LONG);
            FlashButton(i);
            currentInputIndex++;

            // if correct inputs were given for full sequence
            if(currentInputIndex>=sequence.size()) {
                Log.i("Game", "-----------------------------------WON ROUND----------------------------------------");
                winRound();
            }
        }
        // mismatch, lose
        else{
            Log.i("Game", "-----------------------------------LOST ROUND----------------------------------------");
            loseGame();}

    }

    //region Button flashing
    int currentFlashIndex = 0; // used to track which button should flash in the sequence
    long flashDelay = 800; // delay between button flashes in ms
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
                playSound(meow);
                flashButton(main.btnNorth);
                break;
            case 1:
                // east
                playSound(boing);
                flashButton(main.btnEast);
                break;
            case 2:
                // south
                playSound(kicksnare);
                flashButton(main.btnSouth);
                break;
            case 3:
                // west
                playSound(honk);
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
