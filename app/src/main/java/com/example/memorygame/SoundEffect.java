package com.example.memorygame;

import static android.content.Context.AUDIO_SERVICE;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundEffect {
    private SoundPool soundPool;
    public int soundID;
    public boolean loaded = false;
    private Activity activity;

    public SoundEffect(Activity act, int sound){
        activity = act;
        // set hardware buttons to control volume
        act.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // load sound
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status){
                loaded = true;
            }
        });
        soundID = soundPool.load(act, sound, 1);
    }
    public void playSound(float volume){
        if(loaded){
            soundPool.play(soundID, volume, volume, 1,0,1f);
        }
    }
}
