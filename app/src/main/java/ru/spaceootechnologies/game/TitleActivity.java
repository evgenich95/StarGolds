package ru.spaceootechnologies.game;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Anton on 23.12.2015.
 */
public class TitleActivity extends SingleFragmentActivity {

    private static final String GragmentTag = "TitleFragment";

    @Override
    protected Fragment createFragment() {
        return new TitleFragment();
    }

    @Override
    protected String getTag() {
        return GragmentTag;
    }
}
