package ru.spaceootechnologies.game;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    public static final String FragmentTag = "MainFragment";

    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance(getIntent());
    }

    @Override
    protected String getTag() {
        return FragmentTag;
    }
}
