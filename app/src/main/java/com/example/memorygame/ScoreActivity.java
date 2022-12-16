package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    String tag = "ScoreActivity";
    private DatabaseHandler db;
    EditText etName;
    int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        db = new DatabaseHandler(this);

        etName = findViewById(R.id.etName);
        score = getIntent().getIntExtra("score",-1);
        TextView tvScore = findViewById(R.id.tvFinalScore);
        tvScore.setText(String.valueOf(score));
    }

    public void doPlayAgain(View view) {
        finish();
    }

    public void doEnterScore(View view) {
        String _name = String.valueOf(etName.getText());

        if(_name.isEmpty()){
            Log.e(tag, "Tried to enter score but name was empty.");
            Toast.makeText(view.getContext(), "Please enter your name.", Toast.LENGTH_SHORT);
            return;
        }
        if(score==-1){
            Log.e(tag, "Tried to enter score but score was null.");
            Toast.makeText(view.getContext(), "Score error.", Toast.LENGTH_SHORT);
            return;
        }
        db.addHighScore(new HighScore(_name, String.valueOf(score)));
        Intent A = new Intent(view.getContext(), ScoreBoardActivity.class);
        startActivity(A);
        finish();
    }
}