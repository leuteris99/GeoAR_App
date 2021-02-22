package com.lefalexiou.geoar_app.models;

import android.animation.ObjectAnimator;
import android.content.Context;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.lefalexiou.geoar_app.R;
import com.squareup.picasso.Picasso;


public class ViewObject {
    private static final String TAG = "viewObject";
    ArFragment arFragment;
    Context context;
    int layoutId;
    int checked = -1;

    public ViewObject(ArFragment arFragment, Context context, int layoutId) {
        this.arFragment = arFragment;
        this.context = context;
        this.layoutId = layoutId;
    }

    public void createViewRenderable(Anchor anchor, Hologram hologram, ArModel arModel, Uri fileUri) {
        ViewRenderable.builder().setView(context, layoutId).build().thenAccept(viewRenderable -> {
            AnchorNode anchorNode = addToScene(viewRenderable, anchor);
            View v = viewRenderable.getView();
            if (!hologram.getDescription().equals("")) {
                setText(viewRenderable, R.id.textEx, hologram.getDescription());
            }
            setText(viewRenderable, R.id.holo_title, hologram.getTitle());
            if (hologram.getImageURL().equals("")) {
                hideView(viewRenderable, R.id.logo, "image");
            } else {
                ImageView imageView = (ImageView) v.findViewById(R.id.logo);
                Picasso.get().load(hologram.getImageURL()).placeholder(R.drawable.ionian_university).into(imageView);
            }
            if (hologram.getQuestion().equals("")) {
                hideView(viewRenderable, R.id.question, "text");
                hideView(viewRenderable, R.id.radio_group, "radio");
                hideView(viewRenderable, R.id.submit_button, "submit");
//                addOnButtonClickListener(viewRenderable,R.id.submit_button,"submit");
            } else {
                setText(viewRenderable, R.id.question, hologram.getQuestion());
                View view = viewRenderable.getView();
                RadioButton radioButton1 = (RadioButton) view.findViewById(R.id.first_choice);
                RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.second_choice);
                radioButton1.setText(hologram.getAnswerArray().get(0));
                radioButton2.setText(hologram.getAnswerArray().get(1));
                Button button = v.findViewById(R.id.submit_button);

                radioButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checked = 0;
                    }
                });
                radioButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checked = 1;
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (checked) {
                            case -1:
                                break;
                            case 0:
                            case 1:
                                button.setText(hologram.getAnswerArray().get(checked));
                                radioButton1.setEnabled(false);
                                radioButton2.setEnabled(false);
                                break;
                        }
                    }
                });
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // todo: den litourgi
//                        RadioGroup rg = (RadioGroup) view.findViewById(R.id.radio_group);
//                        int rbID = rg.getCheckedRadioButtonId();
//                        RadioButton rb = (RadioButton) view.findViewById(rbID);
//                        button.setText(rb.getText());
//                    }
//                });
            }
            if (hologram.getWebURL().equals("")) {
                hideView(viewRenderable, R.id.webview, "web");
            } else {
                View view = viewRenderable.getView();

                WebView webView = (WebView) view.findViewById(R.id.webview);
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(hologram.getWebURL());
            }
            if (fileUri != null) {
                ModelRenderable.builder().setSource(context, fileUri).build().thenAccept(modelRenderable -> {
                    Node node = new Node();
                    node.setRenderable(modelRenderable);
                    node.setParent(anchorNode);
                    node.setLocalPosition(new Vector3(-(arModel.getDistFromAnchor()), 0.8f, 0));
                    node.setLocalScale(new Vector3(arModel.getScale(), arModel.getScale(), arModel.getScale()));
                    anchorNode.addChild(node);
                    modelRenderable.setShadowCaster(false);
                    modelRenderable.setShadowReceiver(false);

                    ObjectAnimator objectAnimator = createAnimator();
                    objectAnimator.setTarget(node);
                    objectAnimator.setDuration(arModel.getAnimationSpeed()); // the time need for the animation to complete / make one rotation in milli sec.
                    objectAnimator.start();

                });
            }
        });
    }

    private void setText(ViewRenderable viewRenderable, int viewID, String text) {
        View view = viewRenderable.getView();

        TextView textView = (TextView) view.findViewById(viewID);
        textView.setText(text);
    }

    private void hideView(ViewRenderable viewRenderable, int viewID, String viewType) {
        View view = viewRenderable.getView();

        switch (viewType) {
            case "image":
            case "imageView":
                ImageView imageView = (ImageView) view.findViewById(viewID);
                imageView.setVisibility(ImageView.GONE);
                break;
            case "string":
            case "text":
                TextView textView = (TextView) view.findViewById(viewID);
                textView.setVisibility(TextView.GONE);
                break;
            case "radio":
                RadioGroup radioGroup = (RadioGroup) view.findViewById(viewID);
                radioGroup.setVisibility(RadioGroup.GONE);
                break;
            case "button":
            case "submit":
                Button button = (Button) view.findViewById(viewID);
                button.setVisibility(Button.GONE);
                break;
            case "web":
                WebView webView = (WebView) view.findViewById(viewID);
                webView.setVisibility(WebView.GONE);
                break;

        }
    }

    private void addOnButtonClickListener(ViewRenderable viewRenderable, int viewID, String viewType) {
        View view = viewRenderable.getView();

        switch (viewType) {
            case "radio":
            case "submit":
            case "button":
                Button button = view.findViewById(viewID);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioGroup rg = view.findViewById(R.id.radio_group);
                        RadioButton rb = view.findViewById(rg.getCheckedRadioButtonId());
                        button.setText(rb.getText());
                    }
                });
                break;
        }
    }

    private AnchorNode addToScene(ViewRenderable viewRenderable, Anchor anchor) {
        viewRenderable.setShadowCaster(false);
        viewRenderable.setShadowReceiver(false);

        AnchorNode anchorNode = new AnchorNode(anchor);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        Node node = new Node();
        node.setRenderable(viewRenderable);
        node.setParent(anchorNode);
        node.setLocalPosition(new Vector3(0, 0.6f, 0));
        anchorNode.addChild(node);

        return anchorNode;

//        View view = viewRenderable.getView();

//        WebView webView = (WebView) view.findViewById(R.id.webview);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://www.google.com");
//        https://di.ionio.gr/en/
    }

    public void setVideoPlayer(int res, VideoView videoView, String packageName) {
        Log.d(TAG, "setVideoPlayer: res=" + res + ", package=" + packageName);
//        Uri uri = Uri.parse(String.valueOf(res));
        videoView.setVideoPath("./res/raw/crab_rave.mp4");

        MediaController mediaController = new MediaController(context);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    private static ObjectAnimator createAnimator() {
        // Node's setLocalRotation method accepts Quaternions as parameters.
        // First, set up orientations that will animate a circle.
        Quaternion orientation1 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 0);
        Quaternion orientation2 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 120);
        Quaternion orientation3 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 240);
        Quaternion orientation4 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 360);

        ObjectAnimator orbitAnimation = new ObjectAnimator();
        orbitAnimation.setObjectValues(orientation1, orientation2, orientation3, orientation4);

        // Next, give it the localRotation property.
        orbitAnimation.setPropertyName("localRotation");

        // Use Sceneform's QuaternionEvaluator.
        orbitAnimation.setEvaluator(new QuaternionEvaluator());

        //  Allow orbitAnimation to repeat forever
        orbitAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        orbitAnimation.setRepeatMode(ObjectAnimator.RESTART);
        orbitAnimation.setInterpolator(new LinearInterpolator());
        orbitAnimation.setAutoCancel(true);

        return orbitAnimation;
    }
}
