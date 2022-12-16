package com.example.memorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "highscoreManager";
    private static final String TABLE_HIGHSCORES = "highscores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + TABLE_HIGHSCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SCORE + " TEXT" + ")";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);

        // Create tables again
        onCreate(db);
    }

    public void emptyHighScores() {
        // Drop older table if existed
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new highscore
    void addHighScore(HighScore highScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highScore.getName()); // User Name
        values.put(KEY_SCORE, highScore.getScore()); // User Score

        // Inserting Row
        db.insert(TABLE_HIGHSCORES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single highscore
    HighScore getHighScore(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HIGHSCORES, new String[] { KEY_ID,
                        KEY_NAME, KEY_SCORE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HighScore highScore = new HighScore(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return highscore
        return highScore;
    }

    // code to get the top five high scores in a list view
    public ArrayList<HighScore> getTopFiveHighScores() {
        ArrayList<HighScore> highScores = new ArrayList<HighScore>();
        // Select All Query
        String selectQuery = "SELECT id, name, score FROM " + TABLE_HIGHSCORES
                + " ORDER BY CAST(score as INTEGER) DESC LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.setID(Integer.parseInt(cursor.getString(0)));
                highScore.setName(cursor.getString(1));
                highScore.setScore(cursor.getString(2));
                // Adding highscore to list
                highScores.add(highScore);
            } while (cursor.moveToNext());
        }

        // return highscore list
        return highScores;
    }

    // code to get all high scores in a list view
    public List<HighScore> getAllHighScores() {
        List<HighScore> highScores = new ArrayList<HighScore>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.setID(Integer.parseInt(cursor.getString(0)));
                highScore.setName(cursor.getString(1));
                highScore.setScore(cursor.getString(2));
                // Adding highscore to list
                highScores.add(highScore);
            } while (cursor.moveToNext());
        }

        // return highscore list
        return highScores;
    }

    // code to get all high scores with passed name in a list view
    public List<HighScore> getAllHighScoresWithName(String name) {
        List<HighScore> highScores = new ArrayList<HighScore>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HIGHSCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(1).contains(name)){
                    HighScore highScore = new HighScore();
                    highScore.setID(Integer.parseInt(cursor.getString(0)));
                    highScore.setName(cursor.getString(1));
                    highScore.setScore(cursor.getString(2));
                    // Adding highscore to list
                    highScores.add(highScore);
                }
            } while (cursor.moveToNext());
        }
        // return highscore list
        return highScores;
    }

    // code to update the single highscore
    public int updateHighScore(HighScore highScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highScore.getName());
        values.put(KEY_SCORE, highScore.getScore());

        // updating row
        return db.update(TABLE_HIGHSCORES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(highScore.getID()) });
    }

    // Deleting single highscore
    public void deleteHighScore(HighScore highScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIGHSCORES, KEY_ID + " = ?",
                new String[] { String.valueOf(highScore.getID()) });
        db.close();
    }

    // Getting highscore Count
    public int getHighScoreCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_HIGHSCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}
