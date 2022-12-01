package com.jadhav.aakash.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jadhav.aakash.R;
import com.jadhav.aakash.databinding.PostFilterCardViewBinding;

import java.util.ArrayList;

public class HomePostFilterAdapter extends RecyclerView.Adapter<HomePostFilterAdapter.HomePostFilterHolder> {

    public OnClickListener listener;
    ArrayList<HomePostFilterModel> filterModelArrayList;
    Context context;

    public HomePostFilterAdapter(ArrayList<HomePostFilterModel> filterModelArrayList, Context context, OnClickListener listener) {
        this.filterModelArrayList = filterModelArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomePostFilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomePostFilterHolder(LayoutInflater.from(context).inflate(R.layout.post_filter_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostFilterHolder holder, int position) {
        holder.setHomePostFilterData(filterModelArrayList.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return filterModelArrayList.size();
    }

    public interface OnClickListener {
        void OnClick(int position, String type);
    }

    public class HomePostFilterHolder extends RecyclerView.ViewHolder {

        PostFilterCardViewBinding binding;

        public HomePostFilterHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostFilterCardViewBinding.bind(itemView);
        }

        public void setHomePostFilterData(HomePostFilterModel homePostFilterModel, int position, OnClickListener listener) {

            if (homePostFilterModel.isSelected()) {
                binding.filterTitle.setBackground(context.getResources().getDrawable(R.drawable.active_post_filter_view_bg));
                binding.filterTitle.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                binding.filterTitle.setBackground(context.getResources().getDrawable(R.drawable.post_filter_view_bg));
                binding.filterTitle.setTextColor(context.getResources().getColor(R.color.blue));
            }
            binding.filterTitle.setText(homePostFilterModel.getFilterTitle());

            binding.filterTitle.setOnClickListener(view -> {
                listener.OnClick(position, homePostFilterModel.getFilterTitle());
            });

        }
    }
}
