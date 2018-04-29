package com.antonkokoryshkin.flickrrecent.UI.PhotoGallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.antonkokoryshkin.flickrrecent.R;

public class AboutAppDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        ((TextView) getDialog().findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.dialog_msg)
                .setPositiveButton("OK", null)
                .create();

    }
}
