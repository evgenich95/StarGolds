package ru.spaceootechnologies.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Anton on 25.12.2015.
 */
public class MapResizeDialogFragment extends DialogFragment {

    public static final String RECREATE_MAP = "RecreateMap";

    private void SendResult(int resultCode, boolean playAgain) {
        Intent intent = new Intent();
        intent.putExtra(RECREATE_MAP, playAgain);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TextView notify = new TextView(getActivity());
        notify.setText(R.string.cantCreateMap);
        notify.setGravity(Gravity.CENTER_HORIZONTAL);

        notify.setTextColor(ContextCompat.getColor(getActivity(), R.color.gameOverText));
        notify.setTextSize(25);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        notify.setLayoutParams(params);

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        ll.addView(notify);
        ll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gameOverBaground));

        return new AlertDialog.Builder(getActivity()).setView(ll)
                .setPositiveButton(R.string.reCreateMap, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendResult(getActivity().RESULT_OK, true);
                    }
                }).setNegativeButton(R.string.changeMapProperties, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendResult(getActivity().RESULT_OK, false);
                    }
                }).create();


    }


}
