package ru.spaceootechnologies.game.ui;

import android.support.v4.app.Fragment;

import ru.spaceootechnologies.game.ui.fragment.GameFragment;

public class GameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance(getIntent());
    }
}

