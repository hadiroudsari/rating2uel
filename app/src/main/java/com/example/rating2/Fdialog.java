package com.example.rating2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Fdialog extends AppCompatDialogFragment {
    TextView textView;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialog,null);
        Bundle bundle = getArguments();
        String status = bundle.getString("TEXT","");

        builder.setView(view)
                .setTitle("Status")
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Exit");
                        getActivity().finishAffinity();
                    }
                })
                .setPositiveButton("Rematch", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Rematch");
                    final Profile profile= (Profile) getActivity().getIntent().getSerializableExtra("Profile");
                        System.out.println("in Rematch"+profile.toString());
                        profile.setDuelTime("");
                        profile.setOpponent("");
                        newActivity(profile);
                    }
                });
        textView= view.findViewById(R.id.textViewDialog);
        textView.setText(" "+status);
        return builder.create();
    }

    public void newActivity(Profile profile){
        Intent i = new Intent((BattleActivity)getContext(),ProfileActivity.class);
        i.putExtra("Profile", profile);
        startActivity(i);
    }

}
