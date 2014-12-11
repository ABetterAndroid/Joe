package com.joe.orangee.activity.search;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.joe.orangee.R;
import com.joe.orangee.util.Utils;

public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        View contentView=findViewById(R.id.content_layout);
        Utils.setTopPadding(this, contentView);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View searchView=getLayoutInflater().inflate(R.layout.toobar_search_layout, null);
        getSupportActionBar().setCustomView(searchView);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        Utils.setActionBarStyle(getSupportActionBar());
        Spinner optionSpinner=(Spinner)searchView.findViewById(R.id.search_option);

        ArrayAdapter mAdapter=new ArrayAdapter<>(this,R.layout.action_spinner_item,
                getResources().getStringArray(R.array.search_option));
        optionSpinner.setAdapter(mAdapter);
        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this, "你点击的是:"+parent.getItemAtPosition(position).toString(), 2000).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AutoCompleteTextView search=(AutoCompleteTextView)searchView.findViewById(R.id.search_tv);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(SearchActivity.this,"change",1000).show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
