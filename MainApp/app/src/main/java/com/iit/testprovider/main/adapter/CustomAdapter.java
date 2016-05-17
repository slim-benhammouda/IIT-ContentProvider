package com.iit.testprovider.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.iit.testprovider.main.R;
import com.iit.testprovider.main.core.RecordsHelper;
import com.iit.testprovider.main.wrapper.ListItemWrapper;


public class CustomAdapter extends
        RecyclerView.Adapter<CustomAdapter.ViewHolder> implements View.OnClickListener {

        private OnItemClickedListener mItemClickedListener;

    private CustomAdapter(){
        //hide default constructor
    }

    public CustomAdapter( OnItemClickedListener listener) {

                mItemClickedListener = listener;
    }

    @Override
    public int getItemCount() {

        return RecordsHelper.getInstance().getSize();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("slim", "Adapter:onCreateViewHolder called");
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // ... Nothing to do
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mItemWrapper = RecordsHelper.getInstance().get(position);
        holder.mItemTitle.setText(holder.mItemWrapper.getTitle());
        holder.mItemDescription.setText(holder.mItemWrapper.getDescription());
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);

    }


    @Override
    public void onClick(View v) {
        mItemClickedListener.onItemClicked(RecordsHelper.getInstance().indexOf(((ViewHolder) v.getTag()).mItemWrapper));
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ListItemWrapper mItemWrapper;
        public TextView mItemTitle;
        public TextView mItemDescription;


        public ViewHolder(View view) {
            super(view);
            mItemTitle = (TextView) view.findViewById(R.id.label);
            mItemDescription = (TextView) view.findViewById(R.id.description);

        }
    }


    public interface OnItemClickedListener {

        public void onItemClicked(int position);

    }

}
