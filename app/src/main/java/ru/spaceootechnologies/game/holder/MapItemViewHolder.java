package ru.spaceootechnologies.game.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import ru.spaceootechnologies.game.R;

/**
 * Created by Anton on 18.12.2015.
 */
public class MapItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;

    public MapItemViewHolder(View itemView) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
