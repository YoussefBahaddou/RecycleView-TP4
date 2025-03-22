package com.emsi.recycleviewtp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emsi.recycleviewtp.R;
import com.emsi.recycleviewtp.beans.Star;
import com.emsi.recycleviewtp.service.StarService;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {

    private static final String TAG = "StarAdapter";
    private List<Star> stars;
    private List<Star> starsFilter;
    private Context context;
    private NewFilter mfilter;
    private StarService starService;

    public StarAdapter(Context context, List<Star> stars) {
        this.stars = stars;
        this.context = context;
        starsFilter = new ArrayList<>();
        if (stars != null) {
            starsFilter.addAll(stars);
        }
        mfilter = new NewFilter(this);
        starService = StarService.getInstance();
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, viewGroup, false);
        return new StarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {
        try {
            Log.d(TAG, "onBindView call ! "+ i);
            if (starsFilter != null && i < starsFilter.size()) {
                Star star = starsFilter.get(i);
                if (star != null) {
                    // Load image with error handling
                    Glide.with(context)
                            .asBitmap()
                            .load(star.getImg())
                            .apply(new RequestOptions().override(100, 100))
                            .error(R.drawable.ic_launcher_foreground) // Use a default image
                            .into(starViewHolder.img);

                    starViewHolder.name.setText(star.getName() != null ? star.getName().toUpperCase() : "");
                    starViewHolder.stars.setRating(star.getStar());
                    starViewHolder.idss.setText(String.valueOf(star.getId()));

                    // Set up long click listener for deletion
                    starViewHolder.parent.setOnLongClickListener(v -> {
                        showDeleteDialog(star);
                        return true;
                    });
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error binding view holder: " + e.getMessage());
        }
    }

    private void showDeleteDialog(Star star) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Star");
        builder.setMessage("Do you want to delete this star?");

        // Add the buttons
        builder.setPositiveButton("Yes", (dialog, id) -> {
            // User clicked Yes button - delete the star
            deleteStar(star);
        });

        builder.setNegativeButton("No", (dialog, id) -> {
            // User clicked No button - dismiss dialog
            dialog.dismiss();
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteStar(Star star) {
        try {
            // Remove from service
            boolean success = starService.delete(star);

            if (success) {
                // Remove from both lists
                int position = starsFilter.indexOf(star);
                stars.remove(star);
                starsFilter.remove(star);

                // Notify adapter of item removal
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, starsFilter.size());

                Toast.makeText(context, "Star deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete star", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting star: " + e.getMessage());
            Toast.makeText(context, "Error deleting star", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return starsFilter != null ? starsFilter.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public class NewFilter extends Filter {
        public RecyclerView.Adapter<StarViewHolder> mAdapter;

        public NewFilter(RecyclerView.Adapter<StarViewHolder> mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filteredList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                if (stars != null) {
                    filteredList.addAll(stars);
                }
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                if (stars != null) {
                    for (Star p : stars) {
                        if (p != null && p.getName() != null &&
                                p.getName().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(p);
                        }
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter.clear();
            if (filterResults.values != null && filterResults.values instanceof List<?>) {
                List<?> resultList = (List<?>) filterResults.values;
                for (Object item : resultList) {
                    if (item instanceof Star) {
                        starsFilter.add((Star) item);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
