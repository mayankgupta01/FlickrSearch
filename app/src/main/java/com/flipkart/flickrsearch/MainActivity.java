package com.flipkart.flickrsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private GridView imageGridView;
    ImageAdapter adapter;
    FlickrGetter flickrGetter;
    ArrayList<FlickrItem> flickrItemArrayList;
    FlickrJSONTask flickrJSONTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText) findViewById(R.id.editText);
        imageGridView = (GridView) findViewById(R.id.gridView);

        flickrGetter = new FlickrGetter();
        flickrItemArrayList = new ArrayList<FlickrItem>();

        adapter = new ImageAdapter(flickrItemArrayList, this);
        imageGridView.setAdapter(adapter);


    }

    public void search(View view) {
        flickrItemArrayList.clear();
        String searchText = searchEditText.getText().toString();

        flickrJSONTask = new FlickrJSONTask(adapter,flickrGetter, flickrItemArrayList);
        flickrJSONTask.execute(searchText);
    }
}
