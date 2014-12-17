package com.joe.orangee.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by qiaorongzhu on 2014/12/17.
 */
public class ImageAutoPlayUtil {

    private static ImageView[] imageViewArray;
    private static Handler mHandler= new Handler();
    private static int mActiveImageIndex = -1;
    private static float maxRotationFactor = 3F;
    private static float minRotationFactor = -3F;
    private static float maxScaleFactor = 1.5F;
    private static float minScaleFactor = 1.0F;
    private static final Random random = new Random();
    private static int mSwapMs = 10000;
    private static int mFadeInOutMs = 800;
    private static float toRotation;
    private static float toScale;
    private static float toTranslationX;
    private static float toTranslationY;

    private static Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            swapImage();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs);
        }
    };

    public static void startAnimation(ImageView... imageViews){
        imageViewArray=imageViews;
        mHandler.post(mSwapImageRunnable);
    };

    public static void stopAnimation(){
        mHandler.removeCallbacks(mSwapImageRunnable);
    }

    private static void swapImage() {
        if(mActiveImageIndex == -1) {
            mActiveImageIndex = 1;
            animate(imageViewArray[mActiveImageIndex]);
            return;
        }

        int inactiveIndex = mActiveImageIndex;
        mActiveImageIndex = (1 + mActiveImageIndex) % imageViewArray.length;

        final ImageView activeImageView = imageViewArray[mActiveImageIndex];
//        activeImageView.setAlpha(0.0f);
        ImageView inactiveImageView = imageViewArray[inactiveIndex];

        animate(activeImageView);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mFadeInOutMs);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f).setDuration(400),
                ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f).setDuration(800)
        );
//        animatorSet.start();
    }

    private static void start(View view, long duration, float fromRotation, float toRotation, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {
        view.setRotation(fromRotation);
        view.setScaleX(fromScale);
        view.setScaleY(fromScale);
        view.setTranslationX(fromTranslationX);
        view.setTranslationY(fromTranslationY);
        ViewPropertyAnimator propertyAnimator = view.animate().rotation(toRotation).translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
        propertyAnimator.start();
    }

    private static float pickRotation() {
        return minRotationFactor + random.nextFloat() * (maxRotationFactor - minRotationFactor);
    }

    private static float pickScale() {
        return minScaleFactor + random.nextFloat() * (maxScaleFactor - minScaleFactor);
    }

    private static float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (random.nextFloat() - 0.5f);
    }

    private static void animate(View view) {
        float fromRotation=toRotation!=0.0f? toRotation: pickRotation();
        toRotation=pickRotation();
        float fromScale = toScale!=0.0f? toScale: pickScale();
        toScale = pickScale();
        float fromTranslationX = toTranslationX!=0.0f? toTranslationX: pickTranslation(view.getWidth(), fromScale);
        float fromTranslationY = toTranslationY!=0.0f? toTranslationY: pickTranslation(view.getHeight(), fromScale);
        toTranslationX = pickTranslation(view.getWidth(), toScale);
        toTranslationY = pickTranslation(view.getHeight(), toScale);
        start(view, mSwapMs, fromRotation, toRotation, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
    }

}
