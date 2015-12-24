package ru.spaceootechnologies.game;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import ru.spaceootechnologies.game.Helpers.Helper;

/**
 * Created by Anton on 17.12.2015.
 */
public class MainFragment extends Fragment {

    public static final int RobotId = 4;
    public static final int RobotISFreezed = 5;
    public static final int PlayerId = 1;
    public static final int GoldID = 2;
    public static final int PitID = 3;
    public static final int EmptyId = 0;

    public static final int REQUEST_GameDialog = 0;
    public static final String AFTER_GAME_DIALOG_Fragment = "AfterGameDialogFragment";

    private static final int n = 5;
    private Map mMap;

    private MapAdapter mAdapter;

    private TextView goldCounter;

    private int mapSize;
    private int robotAmount;
    private int goldAmount;
    private int pitAmount;


    public static MainFragment newInstance(Intent data) {

        Bundle args = new Bundle();

        int mapSize = (int) data.getSerializableExtra(TitleFragment.SIZE_MAP_KEY);
        int robotAmount = (int) data.getSerializableExtra(TitleFragment.AMOUNT_ROBOTS_KEY);
        int goldAmount = (int) data.getSerializableExtra(TitleFragment.AMOUNT_GOLD_KEY);
        int pitAmount = (int) data.getSerializableExtra(TitleFragment.AMOUNT_PIT_KEY);

        args.putInt(TitleFragment.SIZE_MAP_KEY, mapSize);
        args.putInt(TitleFragment.AMOUNT_ROBOTS_KEY, robotAmount);
        args.putInt(TitleFragment.AMOUNT_GOLD_KEY, goldAmount);
        args.putInt(TitleFragment.AMOUNT_PIT_KEY, pitAmount);


        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private ViewFragmentHolder viewFragmentHolder;

    static class ViewFragmentHolder {

        @Bind(R.id.buttonDown)
        ImageButton ButtonDown;
        @Bind(R.id.buttonUp)
        ImageButton ButtonUp;
        @Bind(R.id.buttonLeft)
        ImageButton ButtonLeft;
        @Bind(R.id.buttonRight)
        ImageButton ButtonRight;
        @Bind(R.id.recycler_view)
        RecyclerView mRecyclerView;
        @Bind(R.id.imageViewBluster)
        ImageView imageViewBluster;
        @BindDrawable(R.drawable.enable_blaster)
        Drawable enable_blaster_false;
        @BindDrawable(R.drawable.blaster)
        Drawable enable_blaster_true;
        @Bind(R.id.imageViewShape1)
        ImageView imageViewShape1;
        @Bind(R.id.imageViewShape2)
        ImageView imageViewShape2;
        @Bind(R.id.imageViewShape3)
        ImageView imageViewShape3;
        @Bind(R.id.relativeBlasterBlock)
        RelativeLayout relativeBlasterBlock;
        List<View> listPatron;
        List<View> listButtons;


        public ViewFragmentHolder(View view) {

            ButterKnife.bind(this, view);
            listButtons = new ArrayList<>();
            listButtons.add(ButtonUp);
            listButtons.add(ButtonDown);
            listButtons.add(ButtonLeft);
            listButtons.add(ButtonRight);
            listButtons.add(relativeBlasterBlock);

        }

        public void UpdateListPotron() {

            listPatron = new ArrayList<>();
            listPatron.add(imageViewShape1);
            listPatron.add(imageViewShape2);
            listPatron.add(imageViewShape3);
            for (View patron : listPatron)
                patron.setVisibility(View.VISIBLE);
            imageViewBluster.setImageDrawable(enable_blaster_true);
            imageViewBluster.setEnabled(true);
            relativeBlasterBlock.setEnabled(true);

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();

        this.mapSize = (int) args.getInt(TitleFragment.SIZE_MAP_KEY);
        this.robotAmount = (int) args.getInt(TitleFragment.AMOUNT_ROBOTS_KEY);
        this.goldAmount = (int) args.getInt(TitleFragment.AMOUNT_GOLD_KEY);
        this.pitAmount = (int) args.getInt(TitleFragment.AMOUNT_PIT_KEY);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_menu, menu);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.gold_counter, null);


        goldCounter = (TextView) v.findViewById(R.id.textView_goldCounter);
        UpdateGoldStatus();

        MenuItem goldMenuIcon = (MenuItem) menu.findItem(R.id.gold_counter);

        goldMenuIcon.setActionView(v);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK)
            return;
        if (requestCode == REQUEST_GameDialog) {
            boolean playAgain = (Boolean) data.getSerializableExtra(GameDialogFragment.PLAY_AGAIN);
            if (playAgain)
                updateUI();
            else
                getActivity().finish();

        }

    }

    private void UpdateGameStatus() {
        UpdateGoldStatus();


        if (mMap.getAmoutGold() != mMap.getGoldFound() && mMap.isGameOver() != true)
            return;

        String notifyMessage = new String();
        boolean DidWin = false;

        if (mMap.getAmoutGold() == mMap.getGoldFound()) {
            DidWin = true;
            notifyMessage = (" Вы выйграли!!! Вы сделали "
                    + String.valueOf(mMap.getAmountPlayerSteps()) + " шагов и собрали "
                    + String.valueOf(mMap.getGoldFound()) + " золота");
        }

        if (mMap.isGameOver() == true) {
            DidWin = false;
            notifyMessage = " Вы проиграли!!! :(";

        }

        FragmentManager manager = getFragmentManager();
        GameDialogFragment dialog = GameDialogFragment.newInstance(notifyMessage, DidWin);
        dialog.setTargetFragment(MainFragment.this, REQUEST_GameDialog);
        dialog.show(manager, AFTER_GAME_DIALOG_Fragment);

    }

    @NonNull
    private void UpdateGoldStatus() {
        String goldStatus =
                String.valueOf(mMap.getGoldFound()) + " / " + String.valueOf(mMap.getAmoutGold());
        if (goldCounter == null)
            return;
        goldCounter.setText(goldStatus);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        View v = layoutInflater.inflate(R.layout.text, container, false);

        viewFragmentHolder = new ViewFragmentHolder(v);
        v.setTag(viewFragmentHolder);


        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), mapSize);
        viewFragmentHolder.mRecyclerView.setLayoutManager(manager);


        View.OnClickListener onNavigateClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Coordinate newPosition = (Coordinate) mMap.getPlayerPosition().clone();
                int[][] oldMap = Helper.CopyArray(mMap.getArrayMap());

                switch (v.getId()) {
                    case R.id.buttonUp:
                        newPosition.setRow(newPosition.getRow() - 1);
                        break;
                    case R.id.buttonDown:
                        newPosition.setRow(newPosition.getRow() + 1);
                        break;
                    case R.id.buttonLeft:
                        newPosition.setColumn(newPosition.getColumn() - 1);
                        break;
                    case R.id.buttonRight:
                        newPosition.setColumn(newPosition.getColumn() + 1);
                        break;
                    case R.id.relativeBlasterBlock:

                        if (viewFragmentHolder.listPatron.size() > 0) {

                            mMap.FreezeAllRobots();
                            viewFragmentHolder.listPatron.get(0).setVisibility(View.INVISIBLE);
                            viewFragmentHolder.listPatron.remove(0);
                        }

                        if (viewFragmentHolder.listPatron.size() == 0) {

                            viewFragmentHolder.imageViewBluster.setEnabled(false);
                            viewFragmentHolder.relativeBlasterBlock.setEnabled(false);
                            viewFragmentHolder.imageViewBluster
                                    .setImageDrawable(viewFragmentHolder.enable_blaster_false);
                        }

                        break;
                    default:
                        break;
                }


                if (mMap.MovePlayer(newPosition))
                    mMap.MoveAllRobots();

                List listUpdates = Helper.DetectUpdates(oldMap, mMap.getArrayMap());
                mAdapter.UpdateMap(listUpdates, mMap.getArrayMap());

                UpdateGameStatus();

            }


        };


        // Упростить в конце если не понадобится
        for (View button : viewFragmentHolder.listButtons

        ) {
            button.setOnClickListener(onNavigateClick);

        }

        updateUI();


        return v;
    }

    public void updateUI() {

        mMap = new MapGenerator(mapSize, robotAmount, goldAmount, pitAmount).getMap();
        if (mMap == null) {
            // вывести диалог --> либо карта создастся с новыми характеристиками либо ещё раз
            // распределится по старой
        }
        mAdapter = new MapAdapter(getActivity(), mMap.getArrayMap());
        viewFragmentHolder.mRecyclerView.setAdapter(mAdapter);

        viewFragmentHolder.UpdateListPotron();
        UpdateGameStatus();

    }


}

