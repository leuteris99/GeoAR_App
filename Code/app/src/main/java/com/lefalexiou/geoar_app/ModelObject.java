package com.lefalexiou.geoar_app;

import android.app.AlertDialog;
import android.content.Context;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ModelObject {
    private static final String TAG = "viewObject";
    ArFragment arFragment;
    Context context;

    public ModelObject(ArFragment arFragment, Context context) {
        this.arFragment = arFragment;
        this.context = context;
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
    }
}
