package com.example.memorygame;

public class HighScore {
    int _id;
    String _name;
    String _score;

    public HighScore() {
    }

    public HighScore(int id, String name, String _phone_number) {
        this._id = id;
        this._name = name;
        this._score = _phone_number;
    }

    public HighScore(String name, String score) {
        this._name = name;
        this._score = score;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getScore() {
        return this._score;
    }

    public void setScore(String score) {
        this._score = score;
    }
}