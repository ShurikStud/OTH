package com.omegaauto.shurik.mobilesklad.view;

import android.content.ClipData;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener{

    ContainerPropertiesSettings containerPropertiesSettings;

    public MyOnItemLongClickListener(){
        containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (containerPropertiesSettings.getSeparator() != position) {

            ContainerPropertiesSettings.Property property = (ContainerPropertiesSettings.Property) (parent.getItemAtPosition(position));

            PassObject passObj = new PassObject(view, property);

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//            view.startDrag(data, shadowBuilder, passObj, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, passObj, 0);
            } else {
                view.startDrag(data, shadowBuilder, passObj, 0);
            }
        }
        return true;
    }
}
