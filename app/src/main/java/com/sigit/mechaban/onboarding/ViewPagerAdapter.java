package com.sigit.mechaban.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {
    private final List<ScreenItem> screenItemList;

    public ViewPagerAdapter(List<ScreenItem> screenItemList) {
        this.screenItemList = screenItemList;
    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPagerViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.view_pager_onboarding, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        holder.title.setText(screenItemList.get(position).getTitle());
        holder.desc.setText(screenItemList.get(position).getDescription());
        holder.imgSlide.setImageResource(screenItemList.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return screenItemList.size();
    }

    public static class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSlide;
        TextView title;
        TextView desc;

        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSlide = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
        }
    }
}
