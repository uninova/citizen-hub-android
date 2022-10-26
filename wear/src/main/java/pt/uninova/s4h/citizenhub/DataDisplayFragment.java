package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

public class DataDisplayFragment extends Fragment {

    LinearLayout heartRateDataLayout, stepsDataLayout;
    TextView heartRateDataTextView, stepsDataTextView;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(
                R.layout.fragment_main, container, false);

        heartRateDataLayout = view.findViewById(R.id.heartRateDataLayout);
        heartRateDataTextView = view.findViewById(R.id.textViewHeartRateValue);
        stepsDataLayout = view.findViewById(R.id.stepsDataLayout);
        stepsDataTextView = view.findViewById(R.id.textViewStepsValue);

        initialAnimation();
        enableObservers();

        return view;
    }

    private void enableObservers(){
        MainActivity.listenHeartRateAverage.observe((LifecycleOwner) view.getContext(), s -> heartRateDataTextView.setText(s));
        MainActivity.listenSteps.observe((LifecycleOwner) view.getContext(), s -> stepsDataTextView.setText(s));
        MainActivity.protocolHeartRate.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            if (aBoolean) {
                heartRateDataLayout.setVisibility(View.VISIBLE);
            } else {
                heartRateDataLayout.setVisibility(View.GONE);
            }
        });
        MainActivity.protocolSteps.observe((LifecycleOwner) view.getContext(), aBoolean -> {
            if (aBoolean) {
                stepsDataLayout.setVisibility(View.VISIBLE);
            } else {
                stepsDataLayout.setVisibility(View.GONE);
            }
        });
    }

    private void initialAnimation(){
        TextView swipeText = view.findViewById(R.id.textViewSwipe);
        ImageView imageCitizen = view.findViewById(R.id.imageViewCitizenHub);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.setStartOffset(0);
        AlphaAnimation fadeInImage = new AlphaAnimation(0.0f, 1.0f);
        fadeInImage.setDuration(2500);
        fadeInImage.setStartOffset(0);
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(0);

        imageCitizen.startAnimation(fadeInImage);
        swipeText.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                swipeText.startAnimation(fadeOut);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                swipeText.startAnimation(fadeIn);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}