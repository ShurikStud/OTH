package com.omegaauto.shurik.mobilesklad.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.settings.ListFontSizes;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladFontSize;

public class FontSizeAdapter extends ArrayAdapter<MobileSkladFontSize> {

    Context context;

    public FontSizeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public int getCount() {
        return ListFontSizes.getInstance().getCount();
    }

    @Override
    public int getPosition(@Nullable MobileSkladFontSize item) {
        return  ListFontSizes.getInstance().getPosition(item);
    }

    @Nullable
    @Override
    public MobileSkladFontSize getItem(int position) {
        return ListFontSizes.getInstance().get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return  getCustomView(position, convertView, parent, R.layout.font_size_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return  getCustomView(position, convertView, parent, R.layout.font_size_item);
    }

    private View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int layout){
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
        }

        MobileSkladFontSize fontSize = ListFontSizes.getInstance().get(position);

        if (fontSize != null){

            TextView textValue = row.findViewById(R.id.font_size_text_value);
            TextView textName = row.findViewById(R.id.font_size_text_name);
            textValue.setText(fontSize.getDescr());
            textValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.getSizeValue());
            textName.setText("имя поля");
            textName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.getSizeName());
        }
        return row;
        //return super.getDropDownView(position, convertView, parent);
    }
}
