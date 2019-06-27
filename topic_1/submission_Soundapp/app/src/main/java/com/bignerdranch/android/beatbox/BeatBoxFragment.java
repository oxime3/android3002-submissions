package com.bignerdranch.android.beatbox;

import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import android.widget.VideoView;

import com.bignerdranch.android.beatbox.databinding.FragmentBeatBoxBinding;
import com.bignerdranch.android.beatbox.databinding.ListItemSoundBinding;

import java.util.List;


public class BeatBoxFragment extends Fragment {

    private BeatBox mBeatBox;
    private MediaPlayer mPlayer;
    private static MediaController mController;
    SoundViewModel viewModel;
    private SeekBar mSeekBar;
    private FloatingActionButton B_MUSIC_FAB;
    private Boolean button_switch;
    private static float speed_set;
    private View view;

    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        
        mBeatBox = new BeatBox(getActivity());
        mPlayer = mBeatBox.getmPlayer();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBeatBoxBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_beat_box, container, false);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));

        view = binding.getRoot();
//        VideoView vid_view;
//        vid_view.setMediaController(mController);
//        mController = new MediaController(getContext());
//        mController.setMediaPlayer(this);
//        mController.setAnchorView(view);
//        mController.setEnabled(true);
        inItSeekBar(view);

        return view;
    }

    public View getView(){
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        B_MUSIC_FAB = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        B_MUSIC_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_switch = mBeatBox.playBackgrndMusic(getContext());
                mBeatBox.inItController(getContext(), view);
                buttonSwitch(button_switch);
            }
        });
    }

    private void buttonSwitch(Boolean state){
        if (state){
            B_MUSIC_FAB.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        } else {
            B_MUSIC_FAB.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    //GET CURRENT SPEEDBAR SPEED SETTING
    public static float getSpeed(){
        return speed_set;
    }

    public static MediaController getController(){
        return mController;
    }

    // INITIALIZE SEEKBAR
    private void inItSeekBar(View view){
        mSeekBar = view.findViewById(R.id.sound_control);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Toast mToast = Toast.makeText(getContext(), Integer.toString(mSeekBar.getProgress()), Toast.LENGTH_LONG);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mToast.setText(Integer.toString(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mToast.show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mToast.cancel();
                speed_set = seekBar.getProgress();
            }

        });
    }

    private class SoundHolder extends RecyclerView.ViewHolder {
        private ListItemSoundBinding mBinding;

        private SoundHolder(ListItemSoundBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            viewModel = new SoundViewModel(mBeatBox);
            mBinding.setViewModel(viewModel);
        }

        public void bind(Sound sound) {
            mBinding.getViewModel().setSound(sound);
            mBinding.executePendingBindings();
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.list_item_sound, parent, false);
            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mSounds.get(position);
            holder.bind(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}
