package com.example.surface4pro.bakingtime;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.surface4pro.bakingtime.data.Step;
import com.example.surface4pro.bakingtime.databinding.FragmentStepDetailBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    private static final String ARG_STEP = "step";
    private Step mStep;

    private FragmentStepDetailBinding mBinding;

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
    public static StepDetailFragment newStepListFragmentInstance(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        stepDetailFragment.setArguments(args);
        return stepDetailFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_detail, container, false);

        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_STEP);
            mBinding.stepDetailDescriptionTextView.setText(mStep.getDescription());
        } else {
            // TODO add error message TextView
        }

        return mBinding.getRoot();
    }
}