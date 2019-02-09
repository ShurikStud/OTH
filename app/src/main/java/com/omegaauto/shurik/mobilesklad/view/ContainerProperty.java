package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.omegaauto.shurik.mobilesklad.R;

public class ContainerProperty {

    Context context;
    //    String value;
    SpannableStringBuilder value;
    String name;
    String spanDelimiter;


    public ContainerProperty(Context context, String name, String value, String delimiter) {
        this.context = context;
        this.value = new SpannableStringBuilder(value);
        this.name = name;
        this.spanDelimiter = delimiter;
    }

    public SpannableStringBuilder getValue() {
        if (spanDelimiter != null) {
            value.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)),
                    value.toString().indexOf(spanDelimiter),
                    value.length(),
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return value;
    }

    // возвращает значение до разделителя
    public String getValueString(){
        if (spanDelimiter == null){
            return value.toString().trim();
        } else {
            return value.toString().substring(0, value.toString().indexOf(spanDelimiter)).trim();
        }
    }

    public void setValue(String value) {
        this.value.clear();
        this.value.append(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
