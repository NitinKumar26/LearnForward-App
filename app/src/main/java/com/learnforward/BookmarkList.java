package com.learnforward;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.learnforward.Models.Book.Bookmark;
import com.learnforward.R;
import com.learnforward.utils.ApplicationHelper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class BookmarkList extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ArrayList<Bookmark> bookmarkList;
    private BookmarkListAdapter mAdapter;

    private String bookId,bookmarkPath;

    void loadBookmark() {
        Gson gson = new Gson();
        bookmarkPath = ApplicationHelper.booksFolder + "/" + bookId+"/bookmark.json";
        try {
            Type listType = new TypeToken<ArrayList<Bookmark>>() {}.getType();
            bookmarkList = gson.fromJson(new FileReader(bookmarkPath), listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bookmarkList = new ArrayList<Bookmark>();
        }
    }

    void saveBook(){
        try {
            bookmarkPath = ApplicationHelper.booksFolder + "/" + bookId+"/bookmark.json";
            FileWriter writer = new FileWriter(bookmarkPath);

            Gson objGson = new GsonBuilder().setPrettyPrinting().create();
            objGson.toJson(bookmarkList,writer);
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Bookmarks");

        //Setup Book details
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");

        if (bookId.equals("")) {
            returnResult(-1);
        }
        loadBookmark();
        if (bookmarkList == null || bookmarkList.size()==0) {
            returnResult(-1);
        }
        else {
            recyclerView = findViewById(R.id.recycler_view);
            mAdapter = new BookmarkListAdapter(this,
                    bookmarkList,
                    new BookmarkListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Bookmark item) {
                            returnResult(item.getPageNo());
                        }
                    });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof BookmarkListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = String.valueOf(bookmarkList.get(viewHolder.getAdapterPosition()).getPageNo());

            // backup of removed item for undo purpose
            final Bookmark deletedBookmark = bookmarkList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeBookmark(viewHolder.getAdapterPosition());
            saveBook();

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(recyclerView, name + " removed from bookmark!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreBookmark(deletedBookmark, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        returnResult(-1);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void returnResult(int page) {
        saveBook();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("page", page);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
