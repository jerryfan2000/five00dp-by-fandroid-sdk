package com.nyuen.five00dp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.nyuen.five00dp.R;
import com.nyuen.five00dp.adapter.MenuAdapter;

public class MenuFragment extends SherlockFragment {
    
    private MenuAdapter mMenuAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        
        mMenuAdapter = new MenuAdapter(getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sliding_menu_fragment, container, false);   
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        ListView view = (ListView) getView().findViewById(R.id.listViewMenu);
        view.setAdapter(mMenuAdapter);
    }
    
}
