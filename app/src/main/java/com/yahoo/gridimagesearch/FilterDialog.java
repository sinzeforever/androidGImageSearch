package com.yahoo.gridimagesearch;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by sinze on 8/4/15.
 */
public class FilterDialog extends DialogFragment {
    MainActivity main;
    Spinner spImgSize;
    Spinner spColor;
    Spinner spType;
    EditText etSite;
    Button btSave;
    Button btCancel;
    GImageList gImageList;

    static FilterDialog newInstance(MainActivity main, GImageList gImageList) {
        FilterDialog fragment = new FilterDialog();
        fragment.main = main;
        fragment.gImageList = gImageList;
        Bundle args = new Bundle();
        // args.putInt("id", id);
        // args.putString("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // image size
        spImgSize = (Spinner) view.findViewById(R.id.spinnerSize);
        ArrayAdapter<CharSequence> imgSizeAdapter = ArrayAdapter.createFromResource(main,
                R.array.filter_image_size_items, android.R.layout.simple_spinner_item);
        imgSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImgSize.setAdapter(imgSizeAdapter);
        if (gImageList.size != null) {
            spImgSize.setSelection(imgSizeAdapter.getPosition(gImageList.size));
        }

        // image color
        spColor = (Spinner) view.findViewById(R.id.spinnerColor);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(main,
                R.array.filter_color_filter_items, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(colorAdapter);
        if (gImageList.color != null) {
            spColor.setSelection(colorAdapter.getPosition(gImageList.color));
        }

        // image type
        spType = (Spinner) view.findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(main,
                R.array.filter_type_items, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);
        if (gImageList.type != null) {
            spType.setSelection(typeAdapter.getPosition(gImageList.type));
        }

        // image site domain
        etSite = (EditText) view.findViewById(R.id.siteFilter);
        if (gImageList.site != null) {
            etSite.setText(gImageList.site);
        }

        // cancel and save button
        btSave = (Button) view.findViewById(R.id.filter_save);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gImageList.setColor(spColor.getSelectedItem().toString());
                gImageList.setType(spType.getSelectedItem().toString());
                gImageList.setImgSize(spImgSize.getSelectedItem().toString());
                gImageList.setSite(etSite.getText().toString());
                main.getGImgByAPINew();
                dismiss();
            }
        });

        btCancel = (Button) view.findViewById(R.id.filter_cancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;
    }
}
