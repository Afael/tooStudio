package com.desktopip.exploriztic.tootanium.unstopable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.fragment.FragWorkingStudio;

import java.util.ArrayList;
import java.util.List;

public class WebRVAdapter extends RecyclerView.Adapter<WebRVAdapter.ViewHolder> {

    private onCellClick listener;
    private Context context;
    private FragmentManager fragmentManager;
    private FragWorkingStudio container;
    private List<VHListener> holderListenersList;

    public WebRVAdapter(Context context, onCellClick onCellClick) {

        holderListenersList = new ArrayList<>();
        if(onCellClick instanceof onCellClick){
            this.listener = onCellClick;
            this.context = context;
            this.container = (FragWorkingStudio)onCellClick;
            this.fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        }else {
            throw new IllegalStateException("Must Implement On CellClick");
        }
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null) {
            view = inflater.inflate(R.layout.coba_snippet_cell_rv,parent,false);
        }

        ViewHolder viewHolder = ViewHolder.newInstance(view);

        holderListenersList.add(viewHolder);
        
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String title = container.getFragmentTitle(position);

        holder.tv.setText(title);

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adapter", "onClick: ");
                listener.onItemClick(position);
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClosedClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ((FragWorkingStudio)listener).getSize();
    }

    public void addNewFragmentToList(Fragment newFragment, @Nullable Fragment hiddenFragment){


        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(hiddenFragment != null){
            fragmentTransaction.hide(hiddenFragment);
        }
        fragmentTransaction.add(R.id.container_web,newFragment);
        fragmentTransaction.show(newFragment);
        fragmentTransaction.commit();
        notifyDataSetChanged();

    }

    public void showFragment(Fragment showFragment, Fragment hiddenFragment){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(hiddenFragment);
        fragmentTransaction.show(showFragment);
        fragmentTransaction.commit();
    }

    public void deleteFragmentFromList(Fragment fragment, @Nullable Fragment showFragment){

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        if(showFragment != null){
            fragmentTransaction.show(showFragment);
        }
        fragmentTransaction.commit();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements VHListener{

        TextView tv, close;

        public static ViewHolder newInstance(View itemView) {

            ViewHolder fragment = new ViewHolder(itemView);

            return fragment;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.coba_cell_title);
            close = itemView.findViewById(R.id.coba_cell_close);
        }

        @Override
        public void setViewIsFocus() {
            tv.setVisibility(View.GONE);
        }

        @Override
        public void setViewLoseFocus() {
            tv.setVisibility(View.VISIBLE);
        }
    }

    public interface VHListener{
        void setViewIsFocus();
        void setViewLoseFocus();
    }

    public interface onCellClick{
        void onItemClick(int pos);
        void onClosedClick(int pos);
    }
}
