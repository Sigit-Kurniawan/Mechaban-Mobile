package com.sigit.mechaban.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.sigit.mechaban.R;

import java.util.ArrayList;
import java.util.List;

public class ModalSeachMontir extends BottomSheetDialogFragment {
    private final Context context;
    private SearchBar searchBar;
    private SearchView searchView;
    private SearchAdapter searchAdapter;

    public ModalSeachMontir(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search_montir, container, false);
        searchBar = view.findViewById(R.id.search_bar);
        searchView = view.findViewById(R.id.search_view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_search);

        // Dummy data for demonstration
        List<String> dataList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            dataList.add("Item " + i);
        }

        searchAdapter = new SearchAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(searchAdapter);

        // Open SearchView when the SearchBar is clicked
        searchBar.setOnClickListener(v -> searchView.show());

        // Handle text changes in SearchView
        searchView.getEditText().addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the RecyclerView data
                searchAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Close SearchView when back is pressed
//        searchView.addTransitionListener((v, open) -> {
//            if (open) {
//                // SearchView is closed
//                searchBar.clearFocus();
//            } else {
//                // SearchView is open
//                searchView.requestFocus();
//            }
//        });

        return view;
    }

    public static class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        private final List<String> originalList;
        private final List<String> filteredList;

        public SearchAdapter(List<String> dataList) {
            this.originalList = dataList;
            this.filteredList = new ArrayList<>(dataList);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(filteredList.get(position));
        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void filter(String query) {
            filteredList.clear();
            if (query.isEmpty()) {
                filteredList.addAll(originalList);
            } else {
                for (String item : originalList) {
                    if (item.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
