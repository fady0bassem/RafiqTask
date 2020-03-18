package com.fadybassem.rafiqtask.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fadybassem.rafiqtask.R;
import com.fadybassem.rafiqtask.data.remote.pojo.LocationModel;
import com.fadybassem.rafiqtask.ui.interfaces.LocationClickListener;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private List<LocationModel.Datum> list;
    private LocationClickListener listener;

    public LocationAdapter(List<LocationModel.Datum> list, LocationClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.MyViewHolder holder, int position) {
        LocationModel.Datum datum = list.get(position);

        if (datum != null) {
            if (datum.getAddressLine1() != null)
                holder.addressLine1_textview.setText(datum.getAddressLine1());
            if (datum.getAddressLine2() != null)
                holder.addressLine2_textview.setText(datum.getAddressLine2());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.location(datum);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView addressLine1_textview, addressLine2_textview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            addressLine1_textview = itemView.findViewById(R.id.addressLine1_textview);
            addressLine2_textview = itemView.findViewById(R.id.addressLine2_textview);
        }
    }
}
