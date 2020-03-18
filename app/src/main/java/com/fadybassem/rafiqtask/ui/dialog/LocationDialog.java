package com.fadybassem.rafiqtask.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;

import com.fadybassem.rafiqtask.R;
import com.fadybassem.rafiqtask.ui.interfaces.LocationInterface;
import com.google.android.material.textfield.TextInputEditText;

public class LocationDialog {

    private static LocationDialog locationDialog;
    private static Dialog dialog;

    public static LocationDialog getInstance() {

        if (locationDialog == null)
            locationDialog = new LocationDialog();

        return locationDialog;
    }

    public Dialog showDialog(Context mContext, LocationInterface locationInterface) {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        TextInputEditText edittext = dialog.findViewById(R.id.edittext);
        Button button = dialog.findViewById(R.id.button);

        button.setOnClickListener(v -> {
            locationInterface.search(edittext.getText().toString());
        });
        return dialog;
    }
}
