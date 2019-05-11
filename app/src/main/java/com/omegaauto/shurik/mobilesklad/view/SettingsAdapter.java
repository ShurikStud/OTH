package com.omegaauto.shurik.mobilesklad.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;

public class SettingsAdapter extends BaseAdapter {

    private static final int colorSeparator = 0x64089E;
    private static final int colorSettingVisible = 0x82E4F3;
    private static final int colorSettingInvisible = 0xF4D179;

    private Context context;
    private ContainerPropertiesSettings containerPropertiesSettings;
    private MobileSkladSettings mobileSkladSettings;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SettingsAdapter(Context context){
        this.context = context;
        containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
        mobileSkladSettings = MobileSkladSettings.getInstance();
    }
    @Override
    public int getCount() {
        return containerPropertiesSettings.getSize();
    }

    @Override
    public Object getItem(int position) {
        return containerPropertiesSettings.getProperty(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        ViewHolder viewHolder = null;
        //
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            viewHolder = new ViewHolder();

            rowView = inflater.inflate(R.layout.settings_container_item, null);
            viewHolder.text = (TextView) rowView.findViewById(R.id.settings_item_text_view);
            viewHolder.button = (ImageButton) rowView.findViewById(R.id.settings_item_button);

            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        ContainerPropertiesSettings.Property property = containerPropertiesSettings.getProperty(position);

        viewHolder.text.setText(property.getDescription());

        rowView.setOnDragListener(new MyItemOnDragListener(property, context));

        if (position < containerPropertiesSettings.getSeparator()) {
            rowView.setBackgroundResource(R.drawable.shape_row_visible);
            viewHolder.text.setTextColor(context.getResources().getColor(R.color.colorTextEnable));
            viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mobileSkladSettings.getFontSize().getSizeValue());
        } else if (position == containerPropertiesSettings.getSeparator()) {
            rowView.setBackgroundResource(R.drawable.shape_row_separator);
            viewHolder.text.setText(null);
        } else {
            rowView.setBackgroundResource(R.drawable.shape_row_invisible);
            viewHolder.text.setTextColor(context.getResources().getColor(R.color.colorTextDisable));//0xff990044
            viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mobileSkladSettings.getFontSize().getSizeValue());
       }

        if (position != containerPropertiesSettings.getSeparator()) {
            viewHolder.button.setOnTouchListener(new MyOnTouchListener(property));
            viewHolder.button.setVisibility(View.VISIBLE);
        } else {
            viewHolder.button.setVisibility(View.GONE);
        }

        return rowView;

    }

    static class ViewHolder {
        //ImageView icon;
        TextView text;
        ImageButton button;

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "text=" + text.getText() +
                    ", button=" + button.toString() +
                    '}';
        }
    }


    class MyOnTouchListener implements View.OnTouchListener{

        ContainerPropertiesSettings containerPropertiesSettings;
        ContainerPropertiesSettings.Property property;

        public MyOnTouchListener(ContainerPropertiesSettings.Property property) {
            this.property = property;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ConstraintLayout view = (ConstraintLayout) v.getParent();

            // Get view object tag value.
            String tag = view.getTag().toString();
            // Create clip data.
            ClipData clipData = ClipData.newPlainText("", tag);

            PassObject passObj = new PassObject(view, property);

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            //View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            view.startDrag(clipData, shadowBuilder, passObj, 0);
//            view.startDrag(clipData, shadowBuilder, view, 0);

            DrugDropSingleton.getInstance().startDrug((View)(v.getParent()), property);
            return false;
        }
    }

    class MyOnClickListener implements View.OnClickListener{

        ContainerPropertiesSettings containerPropertiesSettings;
        ContainerPropertiesSettings.Property property;

        public MyOnClickListener(ContainerPropertiesSettings.Property property) {
            containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
            this.property = property;
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, v.toString(), Toast.LENGTH_SHORT).show();

            ConstraintLayout view = (ConstraintLayout) v.getParent();

            PassObject passObj = new PassObject(view, property);

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, passObj, 0);

        }
    }


}
