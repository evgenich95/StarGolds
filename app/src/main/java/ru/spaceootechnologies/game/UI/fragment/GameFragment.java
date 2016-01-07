package ru.spaceootechnologies.game.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import ru.spaceootechnologies.game.entity.Coordinate;
import ru.spaceootechnologies.game.helper.Helper;
import ru.spaceootechnologies.game.entity.Map;
import ru.spaceootechnologies.game.adapter.MapAdapter;
import ru.spaceootechnologies.game.helper.MapGenerator;
import ru.spaceootechnologies.game.R;
import ru.spaceootechnologies.game.ui.dialog.GameDialog;
import ru.spaceootechnologies.game.ui.dialog.MapResizeDialogFragment;

/**
 * Created by Anton on 17.12.2015.
 */
public class GameFragment extends Fragment {



    public static final int REQUEST_GameDialog = 0;
    public static final String AFTER_GAME_DIALOG_Fragment = "AfterGameDialogFragment";

    public static final String MAP_RESIZE_DOALOG_FRAGMENT = "MapResizeDialogFragment";
    public static final int REQUEST_MapResizeDialogFragment = 1;

    private static final String Key_mMap_For_Parceble = "mMap in Parceble";
    private static final String Key_arrayIdsPatrons_For_Serializable = "ListPotron in Serializable";

    private int[] savedIdsPatrons;
    private boolean restartGame;

    private Map mMap;

    RecyclerView.LayoutManager manager;

    private MapAdapter mAdapter;

    private TextView goldCounter;

    private int mapSize;
    private int robotAmount;
    private int goldAmount;
    private int pitAmount;

    public static GameFragment newInstance(Intent data) {

        Bundle args = new Bundle();

        int mapSize = (int) data.getSerializableExtra(TitleFragment.SIZE_MAP_KEY);
        int robotAmount = (int) data.getSerializableExtra(TitleFragment.AMOUNT_ROBOTS_KEY);
        int goldAmount = (int) data.getSerializableExtra(TitleFragment.AMOUNT_GOLD_KEY);
        int pitAmount = (int) data.getSerializableExtra(TitleFragment.AMOUNT_PIT_KEY);

        args.putInt(TitleFragment.SIZE_MAP_KEY, mapSize);
        args.putInt(TitleFragment.AMOUNT_ROBOTS_KEY, robotAmount);
        args.putInt(TitleFragment.AMOUNT_GOLD_KEY, goldAmount);
        args.putInt(TitleFragment.AMOUNT_PIT_KEY, pitAmount);


        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private ViewFragmentHolder viewFragmentHolder;

    static class ViewFragmentHolder {

        @Bind(R.id.buttonDown) ImageButton ButtonDown;
        @Bind(R.id.buttonUp) ImageButton ButtonUp;
        @Bind(R.id.buttonLeft) ImageButton ButtonLeft;
        @Bind(R.id.buttonRight) ImageButton ButtonRight;
        @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
        @Bind(R.id.imageViewBluster) ImageView imageViewBluster;
            @BindDrawable(R.drawable.enable_blaster) Drawable enable_blaster_false;
            @BindDrawable(R.drawable.blaster) Drawable enable_blaster_true;
        @Bind(R.id.imageViewShape1) ImageView imageViewShape1;
        @Bind(R.id.imageViewShape2) ImageView imageViewShape2;
        @Bind(R.id.imageViewShape3) ImageView imageViewShape3;
        @Bind(R.id.relativeBlasterBlock)
        RelativeLayout relativeBlasterBlock;
        ArrayList<View> listPatron;
        ArrayList<View> listButtons;


        public ViewFragmentHolder(View view) {

            ButterKnife.bind(this, view);
            listButtons = new ArrayList<>();
            listButtons.add(ButtonUp);
            listButtons.add(ButtonDown);
            listButtons.add(ButtonLeft);
            listButtons.add(ButtonRight);
            listButtons.add(relativeBlasterBlock);


        }

        public void getReadyForNewGame() {
            listPatron = new ArrayList<>();

            listPatron.add(imageViewShape1);
            listPatron.add(imageViewShape2);
            listPatron.add(imageViewShape3);

            for (View patron : listPatron) {
                patron.setVisibility(View.VISIBLE);
            }

            imageViewBluster.setEnabled(true);
            relativeBlasterBlock.setEnabled(true);
            imageViewBluster.setImageDrawable(enable_blaster_true);

        }

        public void updateBlasterState(int[] idsPotrons) {

            if (idsPotrons == null)
                return;

            imageViewShape1.setVisibility(View.INVISIBLE);
            imageViewShape2.setVisibility(View.INVISIBLE);
            imageViewShape3.setVisibility(View.INVISIBLE);

            listPatron = new ArrayList<>();

            for (int i = 0; i < idsPotrons.length; i++) {
                if (imageViewShape1.getId() == idsPotrons[i])
                    listPatron.add(imageViewShape1);
                if (imageViewShape2.getId() == idsPotrons[i])
                    listPatron.add(imageViewShape2);
                if (imageViewShape3.getId() == idsPotrons[i])
                    listPatron.add(imageViewShape3);
            }

            for (View patron : listPatron) {
                patron.setVisibility(View.VISIBLE);
            }

            if (idsPotrons.length == 0) {
                imageViewBluster.setEnabled(false);
                relativeBlasterBlock.setEnabled(false);
                imageViewBluster.setImageDrawable(enable_blaster_false);
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle args = this.getArguments();

        this.mapSize =  args.getInt(TitleFragment.SIZE_MAP_KEY);
        this.robotAmount = args.getInt(TitleFragment.AMOUNT_ROBOTS_KEY);
        this.goldAmount = args.getInt(TitleFragment.AMOUNT_GOLD_KEY);
        this.pitAmount = args.getInt(TitleFragment.AMOUNT_PIT_KEY);

        getActivity().setTitle("");
        restartGame = false;


        if (savedInstanceState != null) {
            mMap = savedInstanceState.getParcelable(Key_mMap_For_Parceble);
            savedIdsPatrons = savedInstanceState.getIntArray(Key_arrayIdsPatrons_For_Serializable);
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        int[] arrayIdsPatrons = new int[viewFragmentHolder.listPatron.size()];

        for (int i = 0; i < viewFragmentHolder.listPatron.size(); i++) {
            arrayIdsPatrons[i] = viewFragmentHolder.listPatron.get(i).getId();
        }

        outState.putParcelable(Key_mMap_For_Parceble, mMap);
        // необходимо передавать именно массив ids, т.к. при серилизации списка объекты дублируются
        // --> не работает equals()
        // частые ошибки при Серилизации объекта List
        outState.putIntArray(Key_arrayIdsPatrons_For_Serializable, arrayIdsPatrons);

        super.onSaveInstanceState(outState);


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK)
            return;
        if (requestCode == REQUEST_GameDialog) {
            boolean playAgain = (Boolean) data.getSerializableExtra(GameDialog.PLAY_AGAIN);
            if (playAgain) {
                restartGame = true;
                updateUI();
            } else
                getActivity().finish();
        }
        if (requestCode == REQUEST_MapResizeDialogFragment) {
            boolean reCreateMap =
                    (Boolean) data.getSerializableExtra(MapResizeDialogFragment.RECREATE_MAP);
            if (reCreateMap)
                updateUI();
            else
                getActivity().finish();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_menu, menu);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.gold_counter, null);


        goldCounter = (TextView) v.findViewById(R.id.textView_goldCounter);
        updateGoldStatus();

        MenuItem goldMenuIcon = menu.findItem(R.id.gold_counter);

        goldMenuIcon.setActionView(v);

        MenuItem giveUp = menu.findItem(R.id.give_up);

        giveUp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String notifyMessage;
                notifyMessage = getString(R.string.lost);
                notifyMessage += getString(R.string.you_did) + String.valueOf(mMap.getAmountPlayerSteps())
                        + getString(R.string.steps_and_find) + String.valueOf(mMap.getGoldFound()) + "/"
                        + String.valueOf(mMap.getAmoutGold());

                FragmentManager manager = getFragmentManager();
                GameDialog dialog = GameDialog.newInstance(notifyMessage, false);
                dialog.setTargetFragment(GameFragment.this, REQUEST_GameDialog);
                dialog.show(manager, AFTER_GAME_DIALOG_Fragment);

                return false;
            }
        });

    }

    private void updateGameStatus() {
        updateGoldStatus();

        if (mMap.getAmoutGold() != mMap.getGoldFound() && !mMap.isGameOver())
            return;

        String notifyMessage = new String();
        boolean DidWin = false;

        if (mMap.getAmoutGold() == mMap.getGoldFound()) {
            DidWin = true;
            notifyMessage = getString(R.string.win);
        }

        if (mMap.isGameOver()) {
            DidWin = false;
            notifyMessage = getString(R.string.lost);

        }

        notifyMessage += getString(R.string.you_did) + String.valueOf(mMap.getAmountPlayerSteps())
                + getString(R.string.steps_and_find) + String.valueOf(mMap.getGoldFound()) + "/"
                + String.valueOf(mMap.getAmoutGold());

        FragmentManager manager = getFragmentManager();
        GameDialog dialog = GameDialog.newInstance(notifyMessage, DidWin);
        dialog.setTargetFragment(GameFragment.this, REQUEST_GameDialog);
        dialog.show(manager, AFTER_GAME_DIALOG_Fragment);

    }

    private void updateGoldStatus() {
        int GoldFound;
        int AllGold;
        if (mMap == null) {
            GoldFound = 0;
            AllGold = 0;
        } else {
            GoldFound = mMap.getGoldFound();
            AllGold = mMap.getAmoutGold();
        }

        String goldStatus = String.valueOf(GoldFound) + " / " + String.valueOf(AllGold);
        if (goldCounter == null)
            return;
        goldCounter.setText(goldStatus);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        View v = layoutInflater.inflate(R.layout.game_fragment, container, false);

        viewFragmentHolder = new ViewFragmentHolder(v);
        v.setTag(viewFragmentHolder);


        manager = new GridLayoutManager(getActivity(), mapSize);
        viewFragmentHolder.mRecyclerView.setLayoutManager(manager);

        View.OnClickListener onNavigateClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Coordinate newPosition = (Coordinate) mMap.getPlayerPosition().clone();
                int[][] oldMap = Helper.copyArray(mMap.getArrayMap());

                int position;

                switch (v.getId()) {
                    case R.id.buttonUp:
                        newPosition.setRow(newPosition.getRow() - 1);

                        // смещаем камеру на область рядом с игроком
                        position = mMap.getPlayerPosition().getPositionInList(mMap.getArrayMap());
                        if (position/mapSize*mapSize > 1) {
                            viewFragmentHolder.mRecyclerView
                                .smoothScrollToPosition(position - 3*mapSize);
                        }

                        break;
                    case R.id.buttonDown:
                        newPosition.setRow(newPosition.getRow() + 1);

                        // смещаем камеру на область рядом с игроком
                        position = mMap.getPlayerPosition().getPositionInList(mMap.getArrayMap());
                        if ((mapSize*mapSize-position)/mapSize>=3){
                            viewFragmentHolder.mRecyclerView
                                    .smoothScrollToPosition(position + 3*mapSize);
                        }
                        break;
                    case R.id.buttonLeft:
                        newPosition.setColumn(newPosition.getColumn() - 1);
                        break;
                    case R.id.buttonRight:
                        newPosition.setColumn(newPosition.getColumn() + 1);
                        break;
                    case R.id.relativeBlasterBlock:

                        if (viewFragmentHolder.listPatron.size() > 0) {

                            mMap.freezeAllRobots();
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

                if (mMap.movePlayer(newPosition))
                    mMap.moveAllRobots();

                List listUpdates = Helper.detectUpdates(oldMap, mMap.getArrayMap());
                mAdapter.updateMap(listUpdates, mMap.getArrayMap());

                viewFragmentHolder.updateBlasterState(savedIdsPatrons);
                updateGameStatus();

            }
        };


        for (View button : viewFragmentHolder.listButtons) {
            button.setOnClickListener(onNavigateClick);
        }
        updateUI();

        return v;
    }

    public void updateUI() {

        if (mMap == null || restartGame) { // если карта ранее не была создана, или игрок хочет
                                           // сыграть заново, то генерируем её

            viewFragmentHolder.getReadyForNewGame();


            mMap = new MapGenerator(mapSize, robotAmount, goldAmount, pitAmount).getMap();
            if (mMap == null) {

                // Создадим диалог, на случай если карта не сможет создаться
                FragmentManager manager = getFragmentManager();
                MapResizeDialogFragment dialog = new MapResizeDialogFragment();
                dialog.setTargetFragment(GameFragment.this, REQUEST_MapResizeDialogFragment);
                dialog.show(manager, MAP_RESIZE_DOALOG_FRAGMENT);
                return;
            }
        }
        // иначе испульзуем уже существующую, которую нам вернул метод onSaveInstanceState

        mAdapter = new MapAdapter(getActivity(), mMap.getArrayMap());
        viewFragmentHolder.mRecyclerView.setAdapter(mAdapter);

        // смещаем камеру на позицию игрока
        int position = mMap.getPlayerPosition().getPositionInList(mMap.getArrayMap());
        viewFragmentHolder.mRecyclerView.smoothScrollToPosition(position+3*mapSize);

        viewFragmentHolder.updateBlasterState(savedIdsPatrons);
        if (savedIdsPatrons != null)
            savedIdsPatrons = null;
        updateGameStatus();
    }
}

