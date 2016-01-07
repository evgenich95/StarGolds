package ru.spaceootechnologies.game.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.spaceootechnologies.game.holder.MapItemViewHolder;
import ru.spaceootechnologies.game.R;
import ru.spaceootechnologies.game.entity.Coordinate;
import ru.spaceootechnologies.game.ui.fragment.GameFragment;

/**
 * Created by Anton on 18.12.2015.
 */
public class MapAdapter extends RecyclerView.Adapter<MapItemViewHolder> {

    public static final int RobotId = GameFragment.RobotId;
    public static final int RobotISFreezed = GameFragment.RobotISFreezed;
    public static final int PlayerId = GameFragment.PlayerId;
    public static final int GoldID = GameFragment.GoldID;
    public static final int PitID = GameFragment.PitID;
    public static final int EmptyId = GameFragment.EmptyId;

    private int sizeMap;
    private Context context;

    private int[][] arrayMap;

    public MapAdapter(Context context, int[][] arrayMap) {
        this.context = context;
        this.arrayMap = arrayMap;
        sizeMap = arrayMap.length * arrayMap[0].length;
    }

    @Override
    public int getItemCount() {
        return sizeMap;
    }

    @Override
    public MapItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View v = layoutInflater.inflate(R.layout.item, parent, false);

        return new MapItemViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MapItemViewHolder holder, int position) {

        Coordinate pos = Coordinate.CoordinateForPositionInList(arrayMap, position);

        switch (arrayMap[pos.getRow()][pos.getColumn()]) {
            case RobotId: // робот
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.robot));
                holder.image
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case PlayerId: // игрок
                holder.image
                        .setImageDrawable(ContextCompat.getDrawable(context, R.drawable.player));
                holder.image
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case GoldID: // золото
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gold));
                holder.image
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case PitID: // яма
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pit));
                holder.image
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case EmptyId: // проход
                holder.image.setImageDrawable(null);
                holder.image
                        .setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                break;
            case RobotISFreezed: // замороженный робот
                holder.image.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.disabled_robot));
                break;

            default:
                break;
        }
    }

    public void UpdateMap(List<Integer> listUpdates, int[][] array) {
        this.arrayMap = array;
        for (int item : listUpdates) {
            notifyItemChanged(item);
        }
    }

    public void UpdateMap(int[][] array) {
        this.arrayMap = array;

        notifyDataSetChanged();
    }
}
