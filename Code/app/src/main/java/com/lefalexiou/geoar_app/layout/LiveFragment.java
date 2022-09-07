package com.lefalexiou.geoar_app.layout;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Sun;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.models.ArModel;
import com.lefalexiou.geoar_app.models.Hologram;
import com.lefalexiou.geoar_app.models.Place;
import com.lefalexiou.geoar_app.models.ViewObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LiveFragment extends Fragment  {
    private static final String TAG = "LiveFragment";
    private ArFragment arFragment;
    private String answer;
    private Context context;
    private TextView currentPlaceTextView;
    private StorageReference storageReference;
    private StorageReference modelPathReference;
    private StorageReference fileReference;
    private File file = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        storageReference = FirebaseStorage.getInstance().getReference();
        modelPathReference = storageReference.child("ar_models");

        View v = inflater.inflate(R.layout.fragment_live, container, false);

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.fragment);

        currentPlaceTextView = v.findViewById(R.id.current_place_text_view);
        gettingAwayFromPlaces();

//        assert arFragment != null;
//        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
//            switch (itemSelector) {
//                case 0:
//                    ViewObject viewObject = new ViewObject(arFragment, context, R.layout.placeholderview);
//                    viewObject.createViewRenderable(hitResult.createAnchor(),
//                            new Hologram("HoloTest",
//                                    "https://www.edarabia.com/wp-content/uploads/2018/02/lonian-university-corfu-greece.png",
//                                    "This is a holo description here is where I will describe the interest point.",
//                                    new ArrayList<>(),
//                                    "www.google.com"));
//                    break;
//                case 1:
//                    VideoObject videoObject = new VideoObject(arFragment, context, hitResult.createAnchor());
//                    break;
//                case 2:
//                case 3:
//                    ModelObject modelObject = new ModelObject(arFragment, context, itemSelector);
//                    modelObject.createViewRenderable(hitResult.createAnchor());
//                    break;
//            }
//        }));

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

    @Override
    public void onPause() {
        super.onPause();
        clearTheARScene();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearTheARScene();

        if (file.exists()) {
            if (file.delete()) {
                Log.d(TAG, "onDestroy: tmp model file destroyed.");
            } else {
                Log.d(TAG, "onDestroy: error: tmp model file didn't get destroyed.");
            }
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

    private void clearTheARScene() {
        List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
        for (Node node : children) {
            if (node instanceof AnchorNode) {
                if (((AnchorNode) node).getAnchor() != null) {
                    ((AnchorNode) node).getAnchor().detach();
                }
            }
            if (!(node instanceof Camera) && !(node instanceof Sun)) {
                node.setParent(null);
            }
        }
    }

    // setup the hologram based on a place
    public void getNearbyPlace(Place nearbyPlace, Hologram hologram, ArModel arModel) {
        Log.d(TAG, "getNearbyPlace: " + nearbyPlace.getTitle() + ", aoe: " + nearbyPlace.getAOE());
        currentPlaceTextView.setText("Current place: " + nearbyPlace.getTitle());


        if (arModel != null) {
            fileReference = modelPathReference.child(arModel.getModelURL());
            try {
                file = File.createTempFile("tmp", "sfb");
                fileReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "file: onSuccess: file created");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "file: onFailure: error: " + e.getMessage());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            file = null;
        }

        File finalFile = file;
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            clearTheARScene();
            ViewObject viewObject = new ViewObject(arFragment, context, R.layout.placeholderview);
            viewObject.createViewRenderable(hitResult.createAnchor(), hologram, arModel, Uri.fromFile(finalFile));
        }));
    }

    public void gettingAwayFromPlaces() {
        currentPlaceTextView.setText("Current place: None");
        Log.d(TAG, "gettingAwayFromPlaces: run none");

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            clearTheARScene();

            Toast.makeText(context, "You aren't near any point of interest.", Toast.LENGTH_SHORT).show();
        });
    }
}