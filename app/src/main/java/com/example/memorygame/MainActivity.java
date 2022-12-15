package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //region Initialization
    public TextView tvSequence, tvState, tvScore;
    public ImageButton btnNorth, btnEast, btnSouth, btnWest;
    private Game game;

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

        game = new Game(this);

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
            game.loseGame();
    }
    //endregion
}