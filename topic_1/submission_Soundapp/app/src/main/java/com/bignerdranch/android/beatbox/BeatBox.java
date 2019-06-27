package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox implements MediaController.MediaPlayerControl {
    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "my_songs";
    private static final int MAX_SOUNDS = 5;
    private MediaPlayer mPlayer;
    private MediaController mController;
    private Sound previous_song;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    private float speed;

    public BeatBox(Context context) {
        mAssets = context.getAssets();
        // This old constructor is deprecated, but we need it for
        // compatibility.
        //noinspection deprecation
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();


    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }

        speed = BeatBoxFragment.getSpeed();
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, speed);
    }

    public void release() {
        mSoundPool.release();
        stopBackgrndMusic();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load sound " + filename, ioe);
            }
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor assetFd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(assetFd, 1);
        sound.setSoundId(soundId);
    }

    public void inItController(Context context, View view){
        mController = new MediaController(context);
        mController.setAnchorView(view);
        mController.setMediaPlayer(this);
        mController.show(10000);
    }

    public Boolean playBackgrndMusic(Context context){
        if (mPlayer == null){
            mPlayer = MediaPlayer.create(context, R.raw.cartoon);
        }

        Boolean switchButton;

        if(mPlayer.isPlaying()){
            switchButton = false;
            pauseBackgrndMusic();
        } else{
            switchButton = true;
            mPlayer.start();
            mPlayer.setLooping(true);
            start();

        }
        return switchButton;}

    public void pauseBackgrndMusic(){
        if (mPlayer != null){
            mPlayer.pause();
        }
    }

    public void stopBackgrndMusic(){
        if (mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        } }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public static String getTAG() {
        return TAG;
    }

    public static String getSoundsFolder() {
        return SOUNDS_FOLDER;
    }

    public static int getMaxSounds() {
        return MAX_SOUNDS;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public AssetManager getmAssets() {
        return mAssets;
    }

    public List<Sound> getmSounds() {
        return mSounds;
    }

    public SoundPool getmSoundPool() {
        return mSoundPool;
    }

    public float getSpeed() {
        return speed;
    }

    @Override
    public void start() {
        mPlayer.start();

    }

    @Override
    public void pause() {
        mPlayer.pause();

    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);

    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
