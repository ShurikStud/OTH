package com.omegaauto.shurik.mobilesklad;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.omegaauto.shurik.mobilesklad.container.NumberListLast;
import com.omegaauto.shurik.mobilesklad.view.NumberListAdapter;

import java.util.List;

public class NumberContainerActivity extends AppCompatActivity {

    public static final int REQUEST_RESULT_OK = 1;

    ImageButton buttonSearch;
    EditText editTextContainer;
    //RecyclerView recyclerView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_container);

        editTextContainer = (EditText) findViewById(R.id.activity_number_container_edit_text);
        buttonSearch = (ImageButton) findViewById(R.id.activity_number_container_button_search);

        NumberListAdapter numberListAdapter = new NumberListAdapter(this);

        listView = findViewById(R.id.activity_number_container_list_view);
        listView.setAdapter(numberListAdapter);

        NumberOnClickListener numberOnClickListener = new NumberOnClickListener();
        buttonSearch.setOnClickListener(numberOnClickListener);

        editTextContainer.setText(NumberListLast.getInstance().getCurrentNumber());
        editTextContainer.setSelectAllOnFocus(true);
        //editTextContainer.setOnClickListener(numberOnClickListener);
        editTextContainer.setOnTouchListener(new NumberOnTouchListener());
        //editTextContainer.selectAll();

        //editTextContainer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT);
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        editTextContainer.setFocusable(true);
//        editTextContainer.requestFocus();
//        editTextContainer.selectAll();

    }

    protected void startSearch(String barcode){

        InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

        Intent intent = new Intent();
        intent.putExtra("Barcode", barcode);
        setResult(REQUEST_RESULT_OK, intent);
        finish();
   }

    class NumberOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.activity_number_container_button_search){
                if (editTextContainer.length() > 0) {
                    startSearch(editTextContainer.getText().toString());
                }
                return;
            }
//            if (v.getId() == R.id.activity_number_container_edit_text){
//                editTextContainer.selectAll();
//            }
        }
    }

    class NumberOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

//            if (motionEvent.getAction() == MotionEvent.ACTION_UP && view.getId() == R.id.activity_number_container_edit_text){
//                //editTextContainer.;
//                editTextContainer.selectAll();
//
//            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.activity_number_container_edit_text){

//                InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }

            return false;
        }
    }

}
