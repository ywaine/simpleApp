package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public final class FadeProvider implements VisibilityAnimatorProvider {
    private float incomingEndThreshold = 1.0f;

    public float getIncomingEndThreshold() {
        return this.incomingEndThreshold;
    }

    public void setIncomingEndThreshold(float incomingEndThreshold2) {
        this.incomingEndThreshold = incomingEndThreshold2;
    }

    public Animator createAppear(ViewGroup sceneRoot, View view) {
        return createFadeAnimator(view, 0.0f, 1.0f, 0.0f, this.incomingEndThreshold);
    }

    public Animator createDisappear(ViewGroup sceneRoot, View view) {
        return createFadeAnimator(view, 1.0f, 0.0f, 0.0f, 1.0f);
    }

    private static Animator createFadeAnimator(View view, float startValue, float endValue, float startFraction, float endFraction) {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        final View view2 = view;
        final float f = startValue;
        final float f2 = endValue;
        final float f3 = startFraction;
        final float f4 = endFraction;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view2.setAlpha(TransitionUtils.lerp(f, f2, f3, f4, ((Float) animation.getAnimatedValue()).floatValue()));
            }
        });
        return animator;
    }
}
