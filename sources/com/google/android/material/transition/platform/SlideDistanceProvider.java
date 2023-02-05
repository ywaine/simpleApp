package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.C0077R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SlideDistanceProvider implements VisibilityAnimatorProvider {
    private static final int DEFAULT_DISTANCE = -1;
    private int slideDistance = -1;
    private int slideEdge;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityFlag {
    }

    public SlideDistanceProvider(int slideEdge2) {
        this.slideEdge = slideEdge2;
    }

    public int getSlideEdge() {
        return this.slideEdge;
    }

    public void setSlideEdge(int slideEdge2) {
        this.slideEdge = slideEdge2;
    }

    public int getSlideDistance() {
        return this.slideDistance;
    }

    public void setSlideDistance(int slideDistance2) {
        if (slideDistance2 >= 0) {
            this.slideDistance = slideDistance2;
            return;
        }
        throw new IllegalArgumentException("Slide distance must be positive. If attempting to reverse the direction of the slide, use setSlideEdge(int) instead.");
    }

    public Animator createAppear(ViewGroup sceneRoot, View view) {
        return createTranslationAppearAnimator(sceneRoot, view, this.slideEdge, getSlideDistanceOrDefault(view.getContext()));
    }

    public Animator createDisappear(ViewGroup sceneRoot, View view) {
        return createTranslationDisappearAnimator(sceneRoot, view, this.slideEdge, getSlideDistanceOrDefault(view.getContext()));
    }

    private int getSlideDistanceOrDefault(Context context) {
        int i = this.slideDistance;
        if (i != -1) {
            return i;
        }
        return context.getResources().getDimensionPixelSize(C0077R.dimen.mtrl_transition_shared_axis_slide_distance);
    }

    private static Animator createTranslationAppearAnimator(View sceneRoot, View view, int slideEdge2, int slideDistance2) {
        switch (slideEdge2) {
            case 3:
                return createTranslationXAnimator(view, (float) slideDistance2, 0.0f);
            case 5:
                return createTranslationXAnimator(view, (float) (-slideDistance2), 0.0f);
            case 48:
                return createTranslationYAnimator(view, (float) (-slideDistance2), 0.0f);
            case 80:
                return createTranslationYAnimator(view, (float) slideDistance2, 0.0f);
            case GravityCompat.START:
                return createTranslationXAnimator(view, isRtl(sceneRoot) ? (float) slideDistance2 : (float) (-slideDistance2), 0.0f);
            case GravityCompat.END:
                return createTranslationXAnimator(view, isRtl(sceneRoot) ? (float) (-slideDistance2) : (float) slideDistance2, 0.0f);
            default:
                throw new IllegalArgumentException("Invalid slide direction: " + slideEdge2);
        }
    }

    private static Animator createTranslationDisappearAnimator(View sceneRoot, View view, int slideEdge2, int slideDistance2) {
        switch (slideEdge2) {
            case 3:
                return createTranslationXAnimator(view, 0.0f, (float) (-slideDistance2));
            case 5:
                return createTranslationXAnimator(view, 0.0f, (float) slideDistance2);
            case 48:
                return createTranslationYAnimator(view, 0.0f, (float) slideDistance2);
            case 80:
                return createTranslationYAnimator(view, 0.0f, (float) (-slideDistance2));
            case GravityCompat.START:
                return createTranslationXAnimator(view, 0.0f, isRtl(sceneRoot) ? (float) (-slideDistance2) : (float) slideDistance2);
            case GravityCompat.END:
                return createTranslationXAnimator(view, 0.0f, isRtl(sceneRoot) ? (float) slideDistance2 : (float) (-slideDistance2));
            default:
                throw new IllegalArgumentException("Invalid slide direction: " + slideEdge2);
        }
    }

    private static Animator createTranslationXAnimator(View view, float startTranslation, float endTranslation) {
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{startTranslation, endTranslation})});
    }

    private static Animator createTranslationYAnimator(View view, float startTranslation, float endTranslation) {
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{startTranslation, endTranslation})});
    }

    private static boolean isRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }
}
