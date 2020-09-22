package com.assigment.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assigment.newsapp.R;
import com.assigment.newsapp.db.NewsEntity;
import com.assigment.newsapp.utils.InjectManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsVerticalAdapter extends RecyclerView.Adapter<NewsVerticalAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<NewsEntity> listOfNews = new ArrayList<>();
    private Context context;
    private static final String TAG = "NewsVerticalAdapter";

    public NewsVerticalAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsEntity news = listOfNews.get(position);
        holder.getSource().setText(news.getSource());
        holder.getTitle().setText(news.getTitle());
        holder.getDate().setText(news.getDate());
        Glide.with(context).load(news.getImageUrl()).thumbnail(0.2f).into(holder.getNewsImage());
    }

    @Override
    public int getItemCount() {
        return listOfNews.size();
    }

    public void setData(ArrayList<NewsEntity> listOfNews) {
        this.listOfNews.clear();
        this.listOfNews = listOfNews;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView newsImage;
        private TextView title;
        private TextView source;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            title = itemView.findViewById(R.id.newsTitle);
            source = itemView.findViewById(R.id.newsSource);
            date = itemView.findViewById(R.id.newsDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InjectManager.getInstance().inject(InjectManager.LAUNCH_DETAIL_SCREEN, getAdapterPosition());
                }
            });
        }

        public ImageView getNewsImage() {
            return newsImage;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getSource() {
            return source;
        }

        public TextView getDate() {
            return date;
        }
    }
}
