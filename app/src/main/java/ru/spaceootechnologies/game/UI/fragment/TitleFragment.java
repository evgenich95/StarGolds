package ru.spaceootechnologies.game.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.spaceootechnologies.game.ui.GameActivity;
import ru.spaceootechnologies.game.R;

/**
 * Created by Anton on 23.12.2015.
 */
public class TitleFragment extends Fragment {

    private int defaultGoldCount;
    private int defaultPitCount;
    private int defaultRobotCount;
    private int defaultMapSize;

    public static final String SIZE_MAP_KEY = "sizeMap";
    public static final String AMOUNT_ROBOTS_KEY = " amountRobots";
    public static final String AMOUNT_GOLD_KEY = "amountGold";
    public static final String AMOUNT_PIT_KEY = "amountPit";

    private ViewFragmentHolder viewFragmentHolder;

    static class ViewFragmentHolder {

        @Bind(R.id.numberPickerRobot) NumberPicker numberPickerRobot;
        @Bind(R.id.numberPickerGold) NumberPicker numberPickerGold;
        @Bind(R.id.numberPickerPit) NumberPicker numberPickerPit;
        @Bind(R.id.numberPickerMapSize) NumberPicker numberPickerMapSize;
        @Bind(R.id.buttonStartGame) Button buttonStartGame;


        public ViewFragmentHolder(View view) {

            ButterKnife.bind(this, view);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultMapSize = 10;
        defaultGoldCount = 10;
        defaultRobotCount = 3;
        defaultPitCount = 10;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        View v = layoutInflater.inflate(R.layout.title_fragment, container, false);

        viewFragmentHolder = new ViewFragmentHolder(v);
        v.setTag(viewFragmentHolder);

        getActivity().setTitle(R.string.set_map_properties);

        viewFragmentHolder.numberPickerGold.setMinValue(1);
        viewFragmentHolder.numberPickerGold.setMaxValue(100);
        viewFragmentHolder.numberPickerGold.setWrapSelectorWheel(false);
        viewFragmentHolder.numberPickerGold.setValue(defaultGoldCount);

        viewFragmentHolder.numberPickerPit.setMinValue(0);
        viewFragmentHolder.numberPickerPit.setMaxValue(100);
        viewFragmentHolder.numberPickerPit.setWrapSelectorWheel(false);
        viewFragmentHolder.numberPickerPit.setValue(defaultPitCount);

        viewFragmentHolder.numberPickerRobot.setMinValue(0);
        viewFragmentHolder.numberPickerRobot.setMaxValue(100);
        viewFragmentHolder.numberPickerRobot.setWrapSelectorWheel(false);
        viewFragmentHolder.numberPickerRobot.setValue(defaultRobotCount);

        viewFragmentHolder.numberPickerMapSize.setMinValue(2);
        viewFragmentHolder.numberPickerMapSize.setMaxValue(100);
        viewFragmentHolder.numberPickerMapSize.setWrapSelectorWheel(false);
        viewFragmentHolder.numberPickerMapSize.setValue(defaultMapSize);

        viewFragmentHolder.buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GameActivity.class);

                intent.putExtra(SIZE_MAP_KEY, viewFragmentHolder.numberPickerMapSize.getValue());
                intent.putExtra(AMOUNT_PIT_KEY, viewFragmentHolder.numberPickerPit.getValue());
                intent.putExtra(AMOUNT_GOLD_KEY, viewFragmentHolder.numberPickerGold.getValue());
                intent.putExtra(AMOUNT_ROBOTS_KEY, viewFragmentHolder.numberPickerRobot.getValue());

                startActivity(intent);
            }
        });

        return v;
    }
}
