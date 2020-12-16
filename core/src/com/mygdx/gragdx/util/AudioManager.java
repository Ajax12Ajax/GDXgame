package com.mygdx.gragdx.util;

import com.badlogic.gdx.audio.Music;

public class AudioManager {
    public static final AudioManager instance = new AudioManager();

    private Music playingMusic;


    // singleton: prevent instantiation from other classes
    private AudioManager () { }

    public void play (Music music) {
        stopMusic();
        playingMusic = music;
        if (Preferences.instance.music) {
            music.setLooping(true);
            music.setVolume(Preferences.instance.volMusic);
            music.play();
        }
    }

    public void stopMusic () {
        if (playingMusic != null) playingMusic.stop();
    }

    public void onSettingsUpdated () {
        if (playingMusic == null) return;
        playingMusic.setVolume(Preferences.instance.volMusic);
        if (!Preferences.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}