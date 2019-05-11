package com.omegaauto.shurik.mobilesklad;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.omegaauto.shurik.mobilesklad.view.MyOnItemLongClickListener;
import com.omegaauto.shurik.mobilesklad.view.SettingsAdapter;

//public class SettingsContainerFragment extends AppCompatActivity {
public class SettingsContainerFragment extends Fragment {

    ListView listView;
    LinearLayout linearLayout;

    MyOnDragListener myOnDragListener;
    MyOnItemLongClickListener myOnItemLongClickListener;
    //MyItemOnDragListener myItemOnDragListener;

    SettingsAdapter settingsAdapter;

    public static SettingsContainerFragment getInstance(){
        SettingsContainerFragment settingsContainerFragment = new SettingsContainerFragment();
        return settingsContainerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_container, null);

        myOnDragListener = new MyOnDragListener();
        myOnItemLongClickListener = new MyOnItemLongClickListener();

        linearLayout = (LinearLayout) view.findViewById(R.id.activity_settings_layout_list_view);
        listView = (ListView) view.findViewById(R.id.activity_settings_list_view);

        settingsAdapter = new SettingsAdapter(this.getContext());
        //settingsAdapter.notifyDataSetChanged();

        listView.setAdapter(settingsAdapter);
        listView.setOnDragListener(myOnDragListener);
        listView.setOnItemLongClickListener(myOnItemLongClickListener);

        return view;
    }


    class MyOnDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            return true;
        }
    }

    public void updateView(){
        settingsAdapter.notifyDataSetChanged();
    }

}
