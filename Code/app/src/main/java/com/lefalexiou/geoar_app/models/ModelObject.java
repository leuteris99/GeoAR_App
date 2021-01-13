package com.lefalexiou.geoar_app.models;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.view.animation.LinearInterpolator;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.lefalexiou.geoar_app.R;

public class ModelObject {
    private static final String TAG = "viewObject";
    private ArFragment arFragment;
    private Context context;
    private int itemSelector;

    public ModelObject(ArFragment arFragment, Context context, int itemSelector) {
        this.arFragment = arFragment;
        this.context = context;
        this.itemSelector = itemSelector;
    }

    public void createViewRenderable(Anchor anchor) {
        ModelRenderable.builder().setSource(context, R.raw.andy).build().thenAccept(modelRenderable -> {
            addToScene(anchor, modelRenderable);
        }).exceptionally(throwable -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(throwable.getMessage()).show();
            return null;
        });
    }

    private void addToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();

        /*if (itemSelector == 3) {
            ObjectAnimator orbitAnimation = createAnimator();
            orbitAnimation.setTarget(this);
            orbitAnimation.setDuration(1000);
            orbitAnimation.start();
        }*/
    }

    /*private static ObjectAnimator createAnimator() {
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
    }*/
}
