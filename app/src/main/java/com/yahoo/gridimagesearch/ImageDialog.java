package com.yahoo.gridimagesearch;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by sinze on 8/5/15.
 */
public class ImageDialog extends DialogFragment {
    MainActivity main;
    GImageItem item;
    TouchImageView ivFull;
    ImageView ivZoomIn;
    ImageView ivZoomOut;
    ImageView ivMail;

    static ImageDialog newInstance(MainActivity main, GImageItem item) {
        ImageDialog fragment = new ImageDialog();
        fragment.main = main;
        fragment.item = item;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ivFull = (TouchImageView) view.findViewById(R.id.fullImage);
        ivMail = (ImageView) view.findViewById(R.id.mailIcon);
        ivZoomIn = (ImageView) view.findViewById(R.id.glassPlus);
        ivZoomOut = (ImageView) view.findViewById(R.id.glassMinus);

        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.sendMail("myfriend@abc.com", item.title, item.img);
            }
        });

        ivZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFull.setZoom(ivFull.getCurrentZoom() + 1);
            }
        });
        ivZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFull.setZoom(ivFull.getCurrentZoom() + -1);
            }
        });

        if (main == null) {
            Log.d("my","main is null");
        }

        Picasso.with(main)
            .load(item.hdImg)
            .placeholder(R.drawable.loading)
            .error(R.drawable.ic_notfound)
            .into(ivFull);

        return view;
    }
}
