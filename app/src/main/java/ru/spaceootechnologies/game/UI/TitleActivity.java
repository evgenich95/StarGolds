package ru.spaceootechnologies.game.ui;

import android.support.v4.app.Fragment;

import ru.spaceootechnologies.game.ui.fragment.TitleFragment;

/**
 * Created by Anton on 23.12.2015.
 */
public class TitleActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new TitleFragment();
    }


}
