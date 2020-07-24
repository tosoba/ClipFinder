package com.example.core.android.view.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.coreandroid.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AppBarBoundFabBehavior extends FloatingActionButton.Behavior {

    public AppBarBoundFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        if (dependency instanceof AppBarLayout) {
            ((AppBarLayout) dependency).addOnOffsetChangedListener(new FabOffsetter(parent, child));
        }
        return dependency instanceof AppBarLayout || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        //noinspection SimplifiableIfStatement
        if (dependency instanceof AppBarLayout) {
            // if the dependency is an AppBarLayout, do not allow super to react on that
            // we don't want that behavior
            return true;
        }
        return super.onDependentViewChanged(parent, fab, dependency);
    }

    public static class FabOffsetter implements AppBarLayout.OnOffsetChangedListener {
        private final CoordinatorLayout parent;
        private final FloatingActionButton fab;

        FabOffsetter(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child) {
            this.parent = parent;
            this.fab = child;
        }

        float coalesce(Float first, float second) {
            return first == null ? second : first;
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            // fab should scroll out down in sync with the appBarLayout scrolling out up.
            // let's see how far along the way the appBarLayout is
            // (if displacementFraction == 0.0f then no displacement, appBar is fully expanded;
            //  if displacementFraction == 1.0f then full displacement, appBar is totally collapsed)
            float displacementFraction = -verticalOffset / (float) appBarLayout.getHeight();

            // need to separate translationY on the fab that comes from this behavior
            // and one that comes from other sources
            // translationY from this behavior is stored in a tag on the fab
            float translationYFromThis = coalesce((Float) fab.getTag(R.id.fab_translationY_from_AppBarBoundFabBehavior), 0f);

            // top position, accounting for translation not coming from this behavior
            float topUntranslatedFromThis = fab.getTop() + fab.getTranslationY() - translationYFromThis;

            // total length to displace by (from position uninfluenced by this behavior) for a full appBar collapse
            float fullDisplacement = parent.getBottom() - topUntranslatedFromThis;

            // calculate and store new value for displacement coming from this behavior
            float newTranslationYFromThis = fullDisplacement * displacementFraction;
            fab.setTag(R.id.fab_translationY_from_AppBarBoundFabBehavior, newTranslationYFromThis);

            // update translation value by difference found in this step
            fab.setTranslationY(newTranslationYFromThis - translationYFromThis + fab.getTranslationY());
        }
    }
}
