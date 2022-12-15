package com.example.memorygame;

import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class Game {

    //region Initialization
    private MainActivity main;
    private Animation anim;
    private enum PlayState {learn, play, wait}
    private PlayState playState = PlayState.wait;
    private List<Integer> sequence = new ArrayList<>();
    private int score =0;

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
        if(playState != PlayState.wait)
            return;

        sequence.clear();
        main.tvScore.setText("");
        main.tvSequence.setText("");
        score =0;
        newRound();
    }
    private void newRound(){
        changeState(PlayState.learn);
        AddToSequence();
        AddToSequence();
        currentIndex=0;
        buttonFlashHandler.post(r);
    }
    public void winRound(){
        if(playState!= Game.PlayState.play)
            return;

            score+=100;
            main.tvScore.setText(String.valueOf(score));
            newRound();
    }
    public void loseGame(){
        if(playState == Game.PlayState.play)
            changeState(Game.PlayState.wait);
    }
    private void changeState(PlayState newState){
        playState=newState;
        main.tvState.setText(playState.toString());
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
        anim.setDuration(1000); //You can manage the blinking time with this parameter

        anim.setRepeatCount(0);
        button.startAnimation(anim);
    }
    //endregion
}
