package com.example.videoplayer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videoplayer.R;
import com.example.videoplayer.interfaces.ClickListener;
import com.example.videoplayer.model.VideoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoItemAdapter extends RecyclerView.Adapter implements Filterable{



    List<VideoModel> videoModels = new ArrayList<>();
    List<VideoModel> videoModelList;
    Context context;
    ClickListener clickListener;

    public VideoItemAdapter(List<VideoModel> videoModels, Context context, ClickListener clickListener) {
        this.videoModels = videoModels;
        videoModelList = new ArrayList<>(videoModels);
        this.context = context;
        this.clickListener = clickListener;

    }


    private class VideoHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView duration;
        private TextView filePath;
        private ImageView thumb;

        private VideoHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            duration = view.findViewById(R.id.duration);
            filePath = view.findViewById(R.id.filepath);
            thumb = view.findViewById(R.id.thumb);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new VideoHolder(item);

    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        VideoHolder videoHolder = (VideoHolder) holder;
        VideoModel videoModel = videoModels.get(position);

        videoHolder.title.setText(videoModel.getFileName());
        videoHolder.filePath.setText(videoModel.getFilePath());

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(videoModel.getFilePath())));
        double msec = 0;
        if (mediaPlayer != null) {
            msec = mediaPlayer.getDuration();
        }
        double minutes = (msec % 3600) / 60;

        videoHolder.duration.setText("" + String.format("%.2f", minutes));

        Glide.with(context)
                .load(videoModel.getFilePath())
                .into(videoHolder.thumb);

        double finalmsc = msec;
        videoHolder.thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalmsc == 0) {
                    Toast.makeText(context, "Invalid Video", Toast.LENGTH_SHORT).show();
                } else {
                    clickListener.onClickItem(videoModel.getFilePath());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }



    @Override
    public Filter getFilter() {
        return SearchFilter;
    }

    private Filter SearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VideoModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(videoModelList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (VideoModel item : videoModelList) {
                    if (item.getFileName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            videoModels.clear();
            videoModels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

