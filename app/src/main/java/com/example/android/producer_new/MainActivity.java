package com.example.android.producer_new;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookAdapter.Callback {

    private List<Book> books;
    private ListView listView;
    private BookAdapter bookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        books = getBooksFromRemote();
        bookAdapter = new BookAdapter(this, R.layout.book_view, books, this);
        listView = findViewById(R.id.book_list);
        listView.setAdapter(bookAdapter);
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
                                .price((double)10).build());
                        add(Dish.builder().name("bbb")
                                .price((double)15).build());
                        add(Dish.builder().name("ccc")
                                .price((double)20).build());
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
