package com.example.surface4pro.bakingtime;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.surface4pro.bakingtime.data.Step;
import com.example.surface4pro.bakingtime.databinding.FragmentStepDetailBinding;
import com.example.surface4pro.bakingtime.utilities.GlideApp;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A simple {@link Fragment} subclass.
 * This Fragment displays the detail of a single recipe step.
 * Use the {@link StepDetailFragment#newStepDetailFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment {

    private static final String ARG_STEP = "step";
    private static final String POSITION_KEY = "position";

    private Step mStep;
    private FragmentStepDetailBinding mBinding;
    private PlayerView mPlayerView;
    private SimpleExoPlayer player;

    private long mCurrentPosition = 0;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * This factory method creates a new instance of
     * this fragment using the provided parameters.
     *
     * @param step A Step object that the Fragment should display.
     * @return A new instance of fragment StepDetailFragment.
     */
    public static StepDetailFragment newStepDetailFragmentInstance(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        // Bundle the passed in step to the Fragment
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        stepDetailFragment.setArguments(args);
        return stepDetailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        // If the video position was saved after a configuration change, retrieve it now.
        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mCurrentPosition = savedInstanceState.getLong(POSITION_KEY);
        }

        // If the Fragment was created with arguments, retrieve them and set up the Fragment.
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_STEP);

            if (mStep != null) {
                mBinding.stepDetailDescriptionTextView.setText(mStep.getDescription());

                // If there is no video, but an image url, then load the image
                if (mStep.getVideoURL().isEmpty() && !mStep.getThumbnailURL().isEmpty()) {
                    GlideApp.with(this)
                            .load(mStep.getThumbnailURL())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ic_cupcake)
                            .into(mBinding.stepImageView);
                    mBinding.stepImageView.setVisibility(View.VISIBLE);
                }
            }
            mPlayerView = mBinding.videoView;
        } else {
            // TODO add error message TextView
        }

        return mBinding.getRoot();
    }

    /**
     * This method initializes the video player
     */
    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(player);

            Uri uri = Uri.parse(mStep.getVideoURL());
            MediaSource mediaSource = buildMediaSource(uri);

            player.prepare(mediaSource);

            if (mCurrentPosition != 0) {
                player.seekTo(mCurrentPosition);
            }

            player.setPlayWhenReady(true);
            mBinding.videoView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method builds a MediaSource from an URI
     *
     * @param uri URI that should be used to build the MediaSource
     * @return The new MediaSource object
     */
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakingTime")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (!mStep.getVideoURL().isEmpty()) {
                initializePlayer();
            } else { // In landscape view when there is no video set the text description visible
                mBinding.instructionsScrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (!mStep.getVideoURL().isEmpty()) {
                initializePlayer();
            } else { // In landscape view when there is no video set the text description visible
                mBinding.instructionsScrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            mCurrentPosition = player.getCurrentPosition();
            outState.putLong(POSITION_KEY, mCurrentPosition);
        }
    }
}