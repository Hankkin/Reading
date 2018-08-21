package com.hankkin.reading.utils;

import android.support.design.widget.AppBarLayout;

/**
 * AppBarLayout的状态监听
 * <p>
 * created by Exile
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }

        onAppBarOffsetChanged(mCurrentState, appBarLayout, -i);
    }

    public State getState() {
        return mCurrentState;
    }

    public void onAppBarOffsetChanged(State state, AppBarLayout appBarLayout, int i) {
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
