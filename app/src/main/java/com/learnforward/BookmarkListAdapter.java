package com.learnforward;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.learnforward.Models.Book.Bookmark;
import com.learnforward.R;

import java.util.ArrayList;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Bookmark> bookmarkList;
    private OnItemClickListener listener;

    interface OnItemClickListener {
        void onItemClick(Bookmark item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
        public void bind(final Bookmark item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public BookmarkListAdapter(Context context,
                               ArrayList<Bookmark> bookmarkList,
                               OnItemClickListener listener) {
        this.context = context;
        this.bookmarkList = bookmarkList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Bookmark item = bookmarkList.get(position);
        holder.name.setText("Page " + item.getPageNo());
        holder.bind(bookmarkList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public void removeBookmark(int position) {
        bookmarkList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreBookmark(Bookmark item, int position) {
        bookmarkList.add(position, item);
        notifyItemInserted(position);
    }
}