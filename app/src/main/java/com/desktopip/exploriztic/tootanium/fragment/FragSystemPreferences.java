package com.desktopip.exploriztic.tootanium.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.SystemPreferenceAdapter;
import com.desktopip.exploriztic.tootanium.models.SystemPreferencesMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragSystemPreferences extends BottomSheetDialogFragment {

    private List<SystemPreferencesMenu> listMenu;
    private SystemPreferenceAdapter systemPreferenceAdapter;

    private RecyclerView menuRecyclerView;

    private GridLayout mainGrid;
    private Fragment fragment = null;
    private View view;

    public FragSystemPreferences() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(inflater != null){

            view = inflater.inflate(R.layout.system_preference, container, false);

            listMenu = new ArrayList<>();
            listMenu.add(new SystemPreferencesMenu("Working Studio", R.mipmap.ic_workstation));
            listMenu.add(new SystemPreferencesMenu("Personalization", R.mipmap.ic_personalization));
            listMenu.add(new SystemPreferencesMenu("Profile", R.mipmap.ic_profile));
            listMenu.add(new SystemPreferencesMenu("Marketplace", R.mipmap.ic_marketplace));
            listMenu.add(new SystemPreferencesMenu("Language", R.mipmap.language));
            listMenu.add(new SystemPreferencesMenu("Help and Guide", R.mipmap.ic_help_and_guide));

            mainGrid = view.findViewById(R.id.mainGrid);
            menuRecyclerView = view.findViewById(R.id.menuRecyclerView);


            setSystemPreferenceAdapter(getActivity(), mainGrid, listMenu);

            //setSingleEvent(mainGrid);
        }


        return view;
    }

    private void setSystemPreferenceAdapter(Context context, GridLayout mainGrid, List<SystemPreferencesMenu> listMenu){
        systemPreferenceAdapter = new SystemPreferenceAdapter(context, mainGrid, listMenu);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        menuRecyclerView.setAdapter(systemPreferenceAdapter);
    }

//    private void setSingleEvent(GridLayout mainGrid) {
//        for(int i =0; i < mainGrid.getChildCount(); i++) {
//
//            CardView cardView = (CardView) mainGrid.getChildAt(i);
//
//            final int count = i;
//
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    switch (count)
//                    {
//                        case 0:
//                            fragment = new FragWorkingStudio();
//                            ((MainActivity)getActivity()).callFragment(fragment);
//                            dismiss();
//                            break;
//                        case 1:
////                            Intent workingStudio = new Intent(getActivity(), FragWorkingStudio.class);
////                            workingStudio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            workingStudio.putExtra("param", "Your Personalization");
////                            getActivity().startActivity(workingStudio);
//                            break;
//                        case 2:
//                            fragment = new UserProfile();
//                            ((MainActivity)getActivity()).callFragment(fragment);
//                            dismiss();
//                            break;
//                        case 3:
////                            Intent workingStudio1 = new Intent(getActivity(), FragWorkingStudio.class);
////                            workingStudio1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            workingStudio1.putExtra("param", "Your Marketplace");
////                            getActivity().startActivity(workingStudio1);
//                            break;
//                        case 4:
////                            Intent workingStudio2 = new Intent(getActivity(), FragWorkingStudio.class);
////                            workingStudio2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            workingStudio2.putExtra("param", "Your Language");
////                            getActivity().startActivity(workingStudio2);
//                            break;
//                        case 5:
////                            Intent workingStudio3 = new Intent(getActivity(), FragWorkingStudio.class);
////                            workingStudio3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            workingStudio3.putExtra("param", "Your Help and Guide");
////                            getActivity().startActivity(workingStudio3);
//                            break;
//                    }
//
//                }
//            });
//        }
//    }

}
