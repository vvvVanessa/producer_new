package com.example.android.producer_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BookAdapter.Callback {

    private List<Book> books;
    private ListView listView;
    private BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;
        final BookAdapter.Callback callback = this;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    books = getBooksFromRemote();
                    bookAdapter = new BookAdapter(activity, R.layout.book_view, books, callback);
                    listView = findViewById(R.id.book_list);
                    listView.setAdapter(bookAdapter);;
                } catch (InterruptedException e) {
                    Log.e("main create" , e.toString());
                }
            }
        });
    }

    @Override
    public void click(View view, int position, Book book) {
        book.setAcceptBtnStatus(0);
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int lastVisiblePosition = listView.getLastVisiblePosition();

        if(position >= firstVisiblePosition && position <= lastVisiblePosition) {
            view = listView.getChildAt(position - firstVisiblePosition);
            bookAdapter.getView(position, view, listView);
        }
    }

    private void delBookFromRemote(final Book book) {
        // 服务器端, 实际不删除
        // 仅把状态更新为已接单
    }

    private void delBooks() {
        for(Book book:books) {
            if (book.getAcceptBtnStatus() == 0) {
                delBookFromRemote(book);
            }
        }
    }
    private List<Book> getBooksFromRemote() {
        List<Book> retlist = new ArrayList<Book>() {{
            for(int i = 0; i < 20; i++) {
            add(Book.builder().user(User.builder()
                    .username("yy")
                    .password("123")
                    .phone("4008823823").build())
                    .dishes(new ArrayList<Dish>() {{
                        add(Dish.builder().name("aaa")
                                .price(10).build());
                        add(Dish.builder().name("bbb")
                                .price(15).build());
                        add(Dish.builder().name("ccc")
                                .price(20).build());
                    }})
                    .acceptBtnStatus(1).build());
            }
        }};
        return retlist;
    }
    public void refresh(View view) {
        delBooks();
        books.clear();
        books.addAll(getBooksFromRemote());
        bookAdapter.notifyDataSetChanged();
    }

    public void editDish(View view) {
        Intent intent = new Intent(this, EditDishActivity.class);
        startActivity(intent);
    }


}
