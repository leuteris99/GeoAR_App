package com.lefalexiou.geoar_app.layout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.ar.sceneform.ux.ArFragment;
import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.models.ModelObject;
import com.lefalexiou.geoar_app.models.Place;
import com.lefalexiou.geoar_app.models.Route;
import com.lefalexiou.geoar_app.models.VideoObject;
import com.lefalexiou.geoar_app.models.ViewObject;

import io.opencensus.tags.Tag;


public class LiveFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LiveFragment";
    private ArFragment arFragment;
    private int itemSelector;
    private String answer;
    private Context context;
    private TextView currentPlaceTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live, container, false);

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        RadioButton viewButton = v.findViewById(R.id.radio_view);
        viewButton.setOnClickListener(this);

        viewButton.setChecked(true);
        itemSelector = 0;

        RadioButton video = v.findViewById(R.id.video);
        video.setOnClickListener(this);
        RadioButton model = v.findViewById(R.id.model);
        model.setOnClickListener(this);
        RadioButton animatedModel = v.findViewById(R.id.animated_model);
        animatedModel.setOnClickListener(this);

        currentPlaceTextView = v.findViewById(R.id.current_place_text_view);
        gettingAwayFromPlaces();

        assert arFragment != null;
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            switch (itemSelector) {
                case 0:
                    ViewObject viewObject = new ViewObject(arFragment, context, R.layout.placeholderview);
                    viewObject.createViewRenderable(hitResult.createAnchor());
                    break;
                case 1:
                    VideoObject videoObject = new VideoObject(arFragment, context, hitResult.createAnchor());
                    break;
                case 2:
                case 3:
                    ModelObject modelObject = new ModelObject(arFragment, context, itemSelector);
                    modelObject.createViewRenderable(hitResult.createAnchor());
                    break;
            }
        }));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static LiveFragment newInstance() {
        LiveFragment lf = new LiveFragment();
        return lf;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_view:
                if (checked)
                    // View selected
                    itemSelector = 0;
                break;
            case R.id.video:
                if (checked)
                    // Video model selected
                    itemSelector = 1;
                break;
            case R.id.model:
                if (checked)
                    // 3d transformable model selected
                    itemSelector = 2;
                break;
            case R.id.animated_model:
                if (checked)
                    // animated model selected
                    itemSelector = 3;
        }
    }

    public void onSubmitClick(View view) {
        Button b = (Button) view;
        b.setText(answer);
    }

    public void onQuestionChoiceClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.first_choice:
                if (checked)
                    answer = "you choose 1";
                break;
            case R.id.second_choice:
                if (checked)
                    answer = "second it is!";
                break;
        }
    }

    @Override
    public void onClick(View view) {
        onRadioButtonClicked(view);
    }

    public void getNearbyPlace(Place nearbyPlace) {
        Log.d(TAG, "getNearbyPlace: " + nearbyPlace.getTitle() + ", aoe: " + nearbyPlace.getAOE());
        currentPlaceTextView.setText("Current place: " + nearbyPlace.getTitle());
    }

    public void gettingAwayFromPlaces(){
        currentPlaceTextView.setText("Current place: None");
    }
}