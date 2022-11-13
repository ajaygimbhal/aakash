package com.example.aakash.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aakash.R;
import com.example.aakash.databinding.PostFilterCardViewBinding;

import java.util.ArrayList;

public class HomePostFilterAdapter extends RecyclerView.Adapter<HomePostFilterAdapter.HomePostFilterHolder> {

    ArrayList<HomePostFilterModel> filterModelArrayList;
    Context context;

    public HomePostFilterAdapter(ArrayList<HomePostFilterModel> filterModelArrayList, Context context) {
        this.filterModelArrayList = filterModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HomePostFilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomePostFilterHolder(LayoutInflater.from(context).inflate(R.layout.post_filter_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostFilterHolder holder, int position) {
        holder.setHomePostFilterData(filterModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return filterModelArrayList.size();
    }

    public class HomePostFilterHolder extends RecyclerView.ViewHolder {

        PostFilterCardViewBinding binding;

        public HomePostFilterHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostFilterCardViewBinding.bind(itemView);
        }

        public void setHomePostFilterData(HomePostFilterModel homePostFilterModel) {
            binding.filterTitle.setText(homePostFilterModel.getFilterTitle());
        }
    }
}
