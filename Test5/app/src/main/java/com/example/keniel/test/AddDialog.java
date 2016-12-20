package com.example.keniel.test;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Keniel on 12/1/2016.
 */

public class AddDialog extends DialogFragment {

    LayoutInflater layoutInflater;
    View view;
    EditText message,title;
    Spinner spin;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        layoutInflater = getActivity().getLayoutInflater();
        view = layoutInflater.inflate(R.layout.fragment_dialog,null);
        message = (EditText) view.findViewById(R.id.message);
        title = (EditText) view.findViewById(R.id.title);
        spin = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.firetype_array, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spin.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sendBackResult(title,message,spin);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        return builder.create();
    }
    public interface EditNameDialogListener {
        void onFinishEditDialog(String t,String m, String s, Double lat, Double lng);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(EditText t,EditText m,Spinner s) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        Log.i("yaaaaaaaaaaaaaaaaaaaaay",MainActivity.getX().toString());
        EditNameDialogListener listener = (EditNameDialogListener) getTargetFragment();
        listener.onFinishEditDialog(t.getText().toString(), m.getText().toString(),s.getSelectedItem().toString()
                , MainActivity.getX(),MainActivity.getY());
        dismiss();
    }

}
