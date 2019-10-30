package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.SpartialColor;

import java.util.ArrayList;


public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorVH> {

    private Context context;
    private ArrayList<SpartialColor> listColor;
    private ColorOnClick colorOnClickListener;

    public ColorAdapter(Context context, ArrayList<SpartialColor> listColor, ColorOnClick colorOnClickListener) {
        this.context = context;
        this.listColor = listColor;
        this.colorOnClickListener = colorOnClickListener;
    }

    @NonNull
    @Override
    public ColorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (inflater != null){
            view = inflater.inflate(R.layout.snippet_color,parent,false);
        }

        return new ColorVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorVH holder, int position) {

        final int pos = position;
        SpartialColor colorData = listColor.get(pos);
        FloatingActionButton mFoa = holder.foa;


        int colorId =colorData.colorResId();

        mFoa.setBackgroundTintList(ColorStateList.valueOf(colorId));

        mFoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorOnClickListener.onColorIconClick(v,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listColor.size();
    }

    class ColorVH extends RecyclerView.ViewHolder{

        FloatingActionButton foa;

        ColorVH(View itemView) {
            super(itemView);
            foa = itemView.findViewById(R.id.snippet_color_card);
        }
    }

    public interface ColorOnClick {
        void onColorIconClick(View view, int pos);
    }
}
