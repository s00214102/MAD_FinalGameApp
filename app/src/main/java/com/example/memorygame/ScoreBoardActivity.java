package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreBoardActivity extends AppCompatActivity {

    private ListView _lvScores;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        _lvScores = findViewById(R.id.lvScores);

        // get top 5 scores from db
        db = new DatabaseHandler(this);

        ArrayList<HighScore>topFiveScores = new ArrayList<>();
        topFiveScores = db.getTopFiveHighScores();

        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(this, topFiveScores);
        _lvScores.setAdapter(highScoreAdapter);
    }

    public void doGoBack(View view) {
        finish();
    }
}