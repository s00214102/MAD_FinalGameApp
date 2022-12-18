package com.example.memorygame;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import androidx.loader.content.Loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GameSounds {

    public Dictionary<String, SoundEffect> soundEffects;

    public GameSounds(Activity act){
        soundEffects = new Hashtable<>();
        soundEffects.put("incorrect",new SoundEffect(act, R.raw.incorrect));
        soundEffects.put("honk",new SoundEffect(act, R.raw.honk));
        soundEffects.put("meow",new SoundEffect(act, R.raw.meow));
        soundEffects.put("",new SoundEffect(act, R.raw.boing));
    }
    // play a sound by name from the SoundEffects collection
    public void findAndPlaySoundByName(String name, float volume){
        try{
            soundEffects.get(name).playSound(volume);
        }
        catch(Exception e){
            Log.e("GameSounds","Could not find sound "+name);
        }
    }
}
