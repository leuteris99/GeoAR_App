package com.lefalexiou.geoar_app.models;

import android.content.Context;
import android.media.MediaPlayer;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.lefalexiou.geoar_app.R;

public class VideoObject {
    ArFragment arFragment;
    Context context;
    private ModelRenderable videoRenderable;
    private float HEIGHT = 1.0f;

    public VideoObject(ArFragment arFragment, Context context, Anchor anchor) {
        this.arFragment = arFragment;
        this.context = context;

        ExternalTexture texture = new ExternalTexture();

        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.videoplayback);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);

        ModelRenderable.builder().setSource(context, R.raw.video_screen).build().thenAccept(modelRenderable -> {
            videoRenderable = modelRenderable;
            videoRenderable.getMaterial().setExternalTexture("videoTexture", texture);
            videoRenderable.getMaterial().setFloat4("keyColor", new Color(0.01843f, 1.0f, 0.098f));
        });

        AnchorNode anchorNode = new AnchorNode(anchor);

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            texture.getSurfaceTexture().setOnFrameAvailableListener(surfaceTexture -> {
                anchorNode.setRenderable(videoRenderable);
                texture.getSurfaceTexture().setOnFrameAvailableListener(null);
            });
        } else {
            anchorNode.setRenderable(videoRenderable);
        }

        float width = mediaPlayer.getVideoWidth();
        float height = mediaPlayer.getVideoHeight();

        anchorNode.setLocalScale(new Vector3(HEIGHT * (width / height), HEIGHT, 1.0f));

        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
}
