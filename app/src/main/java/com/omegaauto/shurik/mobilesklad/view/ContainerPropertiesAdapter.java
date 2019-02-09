package com.omegaauto.shurik.mobilesklad.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ContainerPropertiesAdapter extends BaseAdapter {

    Context context;
    List<ContainerProperty> properties;

    public ContainerPropertiesAdapter(Context context, Container container, ContainerPropertiesSettings containerPropertiesSettings) {
        this.context = context;
        properties = new ArrayList<ContainerProperty>();

        for (ContainerPropertiesSettings.Property property: containerPropertiesSettings.getVisibleProperties()) {
            properties.add(new ContainerProperty(context,
                    property.getDescription(),
                    container.getPropertyValueString(property.getName()),
                    property.getDelimiter())
            );

        }

    }

    @Override
    public int getCount() {
        return properties.size();
    }

    @Override
    public Object getItem(int position) {
        return properties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContainerProperty containerProperty = properties.get(position);

        View row = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if (row == null){
            // если элемент еще не отображался, то необходимо его создать

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(R.layout.activity_container_item, parent, false);

            viewHolder.textValue = (TextView) row.findViewById(R.id.activity_container_item_text_value);
            viewHolder.textName = (TextView) row.findViewById(R.id.activity_container_item_text_name);
            viewHolder.value = containerProperty.getValueString();

            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        row.setOnLongClickListener(new PropertyOnLongClickListener());


        viewHolder.textValue.setText(containerProperty.getValue());
        viewHolder.textName.setText(containerProperty.getName());
        viewHolder.value = containerProperty.getValueString();

        viewHolder.textValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        viewHolder.textName.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeName());

        return row;
    }

    class ViewHolder{
        TextView textValue;
        TextView textName;
        String value;
    }

    class PropertyOnLongClickListener implements View.OnLongClickListener{
        @Override
        public boolean onLongClick(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("", viewHolder.textValue.getText());
            ClipData clip = ClipData.newPlainText("", viewHolder.value);
            clipboard.setPrimaryClip(clip);

            String textMessage = context.getResources().getString(R.string.value) + "\n" + viewHolder.textName.getText() + "\n" + context.getResources().getString(R.string.copied);

            Toast toast = Toast.makeText(context, textMessage , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,0 , 0);
            toast.show();
            return false;
        }
    }


}
