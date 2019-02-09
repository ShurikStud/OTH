package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.omegaauto.shurik.mobilesklad.NumberContainerActivity;
import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.container.NumberListLast;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;

public class NumberListAdapter extends BaseAdapter {

    private NumberListLast numberListLast;
    NumberContainerActivity context;

    public NumberListAdapter(Context context) {
        this.context = (NumberContainerActivity) context;
        numberListLast = NumberListLast.getInstance();
    }

    @Override
    public int getCount() {
        return numberListLast.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {


        //ContainerProperty containerProperty = properties.get(position);
        String number = numberListLast.getNumber(position);

        View row = view;
        NumberViewHolder viewHolder = new NumberViewHolder();

        if (row == null){
            // если элемент еще не отображался, то необходимо его создать

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(R.layout.activity_number_container_item, viewGroup, false);

            viewHolder.textViewNumber = (TextView) row.findViewById(R.id.activity_number_container_item_text_view);

            row.setTag(viewHolder);

        } else {
            viewHolder = (NumberViewHolder) row.getTag();
        }

        //row.setOnLongClickListener(new PropertyOnLongClickListener());
        row.setOnClickListener(new NumberListOnClickListener());
        //row.setOnTouchListener(new NumberListOnTouchListener());

        viewHolder.textViewNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, MobileSkladSettings.getInstance().getFontSize().getSizeValue());
        viewHolder.textViewNumber.setText(number);
        //viewHolder.textViewNumber.setOnClickListener(new NumberListOnClickListener());
        viewHolder.textViewNumber.setOnTouchListener(new NumberListOnTouchListener());

        return row;

    }

    public static class NumberViewHolder {
        TextView textViewNumber;
//        public NumberViewHolder(View itemView) {
//            super(itemView);
//            textViewNumber = (TextView) itemView.findViewById(R.id.activity_number_container_item_text_view);
//        }
    }

    protected void startSearch(String barcode){
        Intent intent = new Intent();
        intent.putExtra("Barcode", barcode);
        context.setResult(NumberContainerActivity.REQUEST_RESULT_OK, intent);
        context.finish();
    }

    class NumberListOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            TextView textView = (TextView) view.getTag();
                String barcode = (String) textView.getText();
                if (!barcode.isEmpty()) {
                    startSearch(barcode);
                }
        }
    }

    class NumberListOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ){
                String barcode = (String) ((TextView)view).getText();
                if (!barcode.isEmpty()) {
                    startSearch(barcode);
                }
            }
            return false;
        }
    }
}
