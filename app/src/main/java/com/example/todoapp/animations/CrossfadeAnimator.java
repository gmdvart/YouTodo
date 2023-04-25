package com.example.todoapp.animations;

import android.view.View;

public final class CrossfadeAnimator {
    public static void fadeIn(View view, Long duration) {
        float inAlpha = 1;
        float outAlpha = 1;

        view.setAlpha(outAlpha);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(inAlpha)
                .setDuration(duration)
                .setListener(null);
    }

}
