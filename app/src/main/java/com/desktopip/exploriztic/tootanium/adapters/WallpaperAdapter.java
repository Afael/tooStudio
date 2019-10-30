package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.Wallpaper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;


public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_BTN = 1;

    private Context context;

    private WallpaperOnClickListener wallpaperOnClickListener;
    private OnAddNewWallpaperClickListener onAddNewWallpaperClickListener;
    private ArrayList<Wallpaper> listData;

    public void addNewWallPaper(Wallpaper newWallpaper) {
        listData.add(newWallpaper);
        notifyDataSetChanged();
    }

    public WallpaperAdapter(Context context, ArrayList<Wallpaper> listData, WallpaperOnClickListener wallpaperOnClickListener, OnAddNewWallpaperClickListener newWallpaper) {
        this.context = context;
        this.wallpaperOnClickListener = wallpaperOnClickListener;
        this.listData = listData;
        this.onAddNewWallpaperClickListener = newWallpaper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater != null) {
            if (viewType != ADD_BTN) {
                view = layoutInflater.inflate(R.layout.snippet_wallpaper, parent, false);
                return new WallpaperVH(view);

            } else {
                view = layoutInflater.inflate(R.layout.snippet_add_wallpaper, parent, false);
                return new AddWallpaperVH(view);
            }
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Wallpaper wallpaper = listData.get(position);
        String picture = listData.get(position).getUri();

        ImageView imageThumbnail;

        CardView cardView;

        if (holder.getItemViewType() != ADD_BTN) {
            cardView = ((WallpaperVH) holder).cardView;
            imageThumbnail = ((WallpaperVH) holder).imageView;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wallpaperOnClickListener.onWallpaperClick(position);
                }
            });

            Picasso.with(context)
                    .load(picture)
                    .resize(1500, 0)
                    .into(imageThumbnail);

           /* Glide.with(context)
                    .load(URI.create(wallpaper.getUri()))
                    .apply(new RequestOptions()
                            .override(500, 500)
                            .centerInside())
                    .into(imageThumbnail);*/
        } else {

            cardView = ((AddWallpaperVH)holder).cardView;
            cardView.setRadius(2);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddNewWallpaperClickListener.onAddWallpaperClick(v);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    @Override
    public int getItemViewType(int position) {
        Wallpaper wallpaper = listData.get(position);
        switch (wallpaper.getUri()) {
            case Wallpaper.ADD_BUTTON:
                return ADD_BTN;
            default:
                return -111;
        }
    }


    class WallpaperVH extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;

        WallpaperVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.snippet_wallpaper_imageview);
            cardView = itemView.findViewById(R.id.snippet_wallpaper_card);
        }
    }

    class AddWallpaperVH extends RecyclerView.ViewHolder {

        CardView cardView;

        AddWallpaperVH(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.add_new_wallpaper);
        }
    }

    public interface WallpaperOnClickListener {

        void onWallpaperClick(int pos);
    }

    public interface OnAddNewWallpaperClickListener {
        void onAddWallpaperClick(View v);
    }
}
