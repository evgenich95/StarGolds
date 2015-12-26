package ru.spaceootechnologies.game;

import android.support.v4.app.Fragment;

public class GameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance(getIntent());
    }
}

