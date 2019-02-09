package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class MyOnItemClickListener implements AdapterView.OnItemClickListener{

    Context context;


    public MyOnItemClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show();

//            if (containerPropertiesSettings.getSeparator() != position) {
//
//                ContainerPropertiesSettings.Property property = (ContainerPropertiesSettings.Property) (parent.getItemAtPosition(position));
//
//                PassObject passObj = new PassObject(view, property);
//
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                view.startDrag(data, shadowBuilder, passObj, 0);
//            }
//            return true;

    }
}
