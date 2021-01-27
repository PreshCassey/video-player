package com.example.videoplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.videoplayer.adapter.VideoItemAdapter;
import com.example.videoplayer.interfaces.ClickListener;
import com.example.videoplayer.model.VideoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ClickListener {
    private static final int PERMISSION_CODE = 101;
    RecyclerView video_list;
    SearchView searchView;
    TextView videos;
    FrameLayout m;
    VideoItemAdapter adapter;
    private List<VideoModel> videoModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        video_list = findViewById(R.id.video_list);
        searchView = findViewById(R.id.search_view);
         videos = findViewById(R.id.videos);
         m = findViewById(R.id.m);


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                readfiles();
            } else {
                requestPermissions();
            }
        } else {
            readfiles();
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Allow Permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readfiles();
                }
        }
    }

    private void readfiles() {
        HashSet<String> hashSet = new HashSet<>();
        String[] projection = {MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};

      Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                hashSet.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
            }
            while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> file_path = new ArrayList<>(hashSet);
       List<VideoModel> videoModelList = new ArrayList<>();
        for (String data : file_path) {
            File file = new File(data);
            videoModelList.add(new VideoModel(file.getName(), file.getAbsolutePath()));
        }
            video_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new VideoItemAdapter(videoModelList, MainActivity.this, MainActivity.this);
          video_list.setHasFixedSize(true);
            video_list.setAdapter(adapter);

        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               if (videoModelList.contains(s)){
                   adapter.getFilter().filter(s);
               }else {
                   Toast.makeText(MainActivity.this, "No match found", Toast.LENGTH_LONG).show();
               }
               return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    @Override
    public void onClickItem(String filePath) {
        startActivity(new Intent(MainActivity.this, VideoPlayerActivity.class).putExtra("path_file",filePath));
    }




}

