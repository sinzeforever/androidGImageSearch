package com.yahoo.gridimagesearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sinze on 8/4/15.
 */
public class GImageAdapter extends ArrayAdapter<GImageItem> {
    Context context;
    MainActivity main;
    public GImageAdapter(Context context, List<GImageItem> objects) {
        super(context, 0, objects);
        main = (MainActivity) context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GImageItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gimg_card, parent, false);
        }

        // image
        ImageView ivImg = (ImageView) convertView.findViewById(R.id.GCardImg);
        // clear old image
        ivImg.setImageResource(R.drawable.loading);
        // insert the image using picasso
        Picasso.with(getContext())
            .load(item.img)
            .placeholder(R.drawable.loading)
            .error(R.drawable.ic_notfound)
            .into(ivImg);

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.GCardTitle);
        tvTitle.setText(item.title);

        // set click
        RelativeLayout rlGCard = (RelativeLayout) convertView.findViewById(R.id.GCard);
        rlGCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("my", "click : " + item.title);
                main.showImageDialog(item);
                /*
                if (getContext() instanceof MainActivity) {
                    ImageDialog imageDialog = ImageDialog.newInstance((MainActivity)getContext(), item);
                    imageDialog.show(((MainActivity)getContext()).getFragmentManager(), "dialog");
                }
                */
            }
        });

        return convertView;
    }
}


