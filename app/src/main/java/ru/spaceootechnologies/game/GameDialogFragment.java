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

/**
 * Created by Anton on 19.12.2015.
 */
public class GameDialogFragment extends DialogFragment {

    private static final String NOTIFY_MESSAGE = "messagetoDialog";
    private static final String DID_WIN = "booleanDidWin";
    public static final String PLAY_AGAIN = "booleanPlayAgain";

    public static GameDialogFragment newInstance(String notifyMessage, boolean DidWin) {

        Bundle args = new Bundle();
        args.putString(NOTIFY_MESSAGE, notifyMessage);
        args.putBoolean(DID_WIN, DidWin);

        GameDialogFragment fragment = new GameDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void SendResult(int resultCode, boolean playAgain) {
        Intent intent = new Intent();
        intent.putExtra(PLAY_AGAIN, playAgain);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        TextView notify = new TextView(getActivity());
        notify.setText(getArguments().getString(NOTIFY_MESSAGE));
        notify.setGravity(Gravity.CENTER_HORIZONTAL);
        // notify.
        notify.setTextSize(25);
        notify.setTextColor(ContextCompat.getColor(getActivity(), R.color.purpur));
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        notify.setLayoutParams(params);

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        if (getArguments().getBoolean(DID_WIN))
            ll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen2));
        else
            ll.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed));

        ll.addView(notify);


        return new AlertDialog.Builder(getActivity()).setView(ll)
                // .setTitle(R.string.date_picker_title)
                .setPositiveButton(R.string.repeatGame, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendResult(getActivity().RESULT_OK, true);
                    }
                }).setNegativeButton(R.string.exitGame, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendResult(getActivity().RESULT_OK, false);
                    }
                }).create();
    }
}
