package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.widget.ListView;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.utils.Const;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class MyItemOnDragListener implements View.OnDragListener{

    DrugDropSingleton drugDropSingleton;
    ContainerPropertiesSettings.Property property;

    Drawable drawable_visible;
    Drawable drawable_invisible;
    Drawable drawable_separator;

    Context context;
    private static final int shadowColor = 0x30000000;


    MyItemOnDragListener(ContainerPropertiesSettings.Property property, Context context){
        this.property = property;
        this.context = context;

        drawable_visible = context.getResources().getDrawable(R.drawable.shape_row_visible);
        drawable_invisible = context.getResources().getDrawable(R.drawable.shape_row_invisible);
        drawable_separator = context.getResources().getDrawable(R.drawable.shape_row_separator);

        drugDropSingleton = DrugDropSingleton.getInstance();
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();

        PassObject passObj = (PassObject) event.getLocalState();
        if (passObj != null){
            // если бысто пытаться изменять порядок строк, то возвращает  null
            int i = 10;
            i++;
        }

        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                // при начале перетаскивания имеем View v и property
                // что нам необходимо хранить?
                //drugDropSingleton.startDrug(v, property);
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(Const.colorBackgroundShadow);
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                int index_exited = containerPropertiesSettings.getIndex(property);
                setBackground(v, index_exited);
                break;

            case DragEvent.ACTION_DROP:
//                passObj = (PassObject) event.getLocalState();
//                if (passObj == null){
//                    // если бысто пытаться изменять порядок строк, то возвращает  null
//                    break;
//                }
//                View view = passObj.view;
//                ContainerPropertiesSettings.Property passedProperty = passObj.property;
//                ListView oldParent = (ListView)view.getParent();
//                SettingsAdapter srcAdapter = (SettingsAdapter) (oldParent.getAdapter());
//
//                int removeLocation = containerPropertiesSettings.getIndex(passedProperty);
//                int insertLocation = containerPropertiesSettings.getIndex(property);
//
//                /*
//                 * If drag and drop on the same list, same position,
//                 * ignore
//                 */
//                if(removeLocation != insertLocation){
//
//                    containerPropertiesSettings.movePropertyTo(insertLocation, passedProperty);
//                    srcAdapter.notifyDataSetChanged();
//                    v.getParent();
//                }
//
//                int index_drop = containerPropertiesSettings.getIndex(property);
//                setBackground(v, index_drop);
//                break;

                // используем "костыль" через синглтон
                if (drugDropSingleton.isBeginDrug()){
                    // если перетягивание начиналось, то все в порядке.
                    View view = drugDropSingleton.getView();
                    ContainerPropertiesSettings.Property passedProperty = drugDropSingleton.getProperty();
                    ListView oldParent = (ListView)view.getParent();
                    SettingsAdapter srcAdapter = (SettingsAdapter) (oldParent.getAdapter());

                    int removeLocation = containerPropertiesSettings.getIndex(passedProperty);
                    int insertLocation = containerPropertiesSettings.getIndex(property);

                    /*
                     * If drag and drop on the same list, same position,
                     * ignore
                     */
                    if(removeLocation != insertLocation){

                        containerPropertiesSettings.movePropertyTo(insertLocation, passedProperty);
                        srcAdapter.notifyDataSetChanged();
                        v.getParent();
                    }

                    int index_drop = containerPropertiesSettings.getIndex(property);
                    setBackground(v, index_drop);
                    drugDropSingleton.stopDrug();
                }

                break;

            case DragEvent.ACTION_DRAG_ENDED:

                int index_ended = containerPropertiesSettings.getIndex(property);
                setBackground(v, index_ended);
                break;

            default:
                break;
        }

        return true;

    }

    private void setBackground(View view, int index){

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();

        if (index == containerPropertiesSettings.getSeparator()) {
            view.setBackgroundResource(R.drawable.shape_row_separator);
        } else if (index < containerPropertiesSettings.getSeparator()) {
            view.setBackgroundResource(R.drawable.shape_row_visible);
        } else {
            view.setBackgroundResource(R.drawable.shape_row_invisible);
        }
        view.invalidate();

    }


}
