package com.lefalexiou.geoar_app;

import android.content.Context;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;


class ViewObject {
    private static final String TAG = "viewObject";
    ArFragment arFragment;
    Context context;
    int layoutId;

    public ViewObject(ArFragment arFragment, Context context, int layoutId) {
        this.arFragment = arFragment;
        this.context = context;
        this.layoutId = layoutId;
    }

    public void createViewRenderable(Anchor anchor) {
        ViewRenderable.builder().setView(context, layoutId).build().thenAccept(viewRenderable -> {
            addToScene(viewRenderable, anchor);
        });
    }

    private void addToScene(ViewRenderable viewRenderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(viewRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        View view = viewRenderable.getView();

        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com");
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
}
