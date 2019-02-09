package com.omegaauto.shurik.mobilesklad.settings;

import android.content.Context;
import android.content.res.Resources;

import com.omegaauto.shurik.mobilesklad.R;

import java.util.ArrayList;
import java.util.List;

public final class ListFontSizes {

    static private ListFontSizes instance;
    List<MobileSkladFontSize> mobileSkladFontSizeList;

    //================ МЕТОДЫ ============

    private ListFontSizes(){
        // проинициализируем варианты шрифтов
        mobileSkladFontSizeList = new ArrayList<MobileSkladFontSize>();

    }

    public void init(Context context){

        Resources resources = context.getResources();
        String[] arrayFontID = resources.getStringArray(R.array.font_id);
        String[] arrayFontDescr = resources.getStringArray(R.array.font_descr);
        int[] arrayFontSizeName = resources.getIntArray(R.array.font_size_name);
        int[] arrayFontSizeValue = resources.getIntArray(R.array.font_size_value);

        int counter = 0;
        for (String fontID: arrayFontID) {
            MobileSkladFontSize smallSize = new MobileSkladFontSize(fontID);
            smallSize.setDescr(arrayFontDescr[counter]);
            smallSize.setSizeName(arrayFontSizeName[counter]);
            smallSize.setSizeValue(arrayFontSizeValue[counter]);
            mobileSkladFontSizeList.add(smallSize);
            counter++;
        }

    }

    static public ListFontSizes getInstance(){
        if (instance == null){
            instance = new ListFontSizes();
        }

        return instance;
    }

    public MobileSkladFontSize get(int index){
        if (mobileSkladFontSizeList.size() <= index) {
            return null;
        }
        return mobileSkladFontSizeList.get(index);
    }

    public MobileSkladFontSize get(String id){
        for (MobileSkladFontSize currentFontSize: mobileSkladFontSizeList) {
            if (currentFontSize.id.equals(id)){
                return currentFontSize;
            }
        }
        return null;
    }

    public int getPosition(MobileSkladFontSize mobileSkladFontSize){
        return mobileSkladFontSizeList.indexOf(mobileSkladFontSize);
    }

    public MobileSkladFontSize getDefault(){
        return get("NORMAL");
    }

    public int getCount(){
        return mobileSkladFontSizeList.size();
    }
}
