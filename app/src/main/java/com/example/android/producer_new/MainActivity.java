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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BookAdapter.Callback {
    private final static String HOST = "http://118.25.173.49:8080/";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private List<Book> books;
    private ListView listView;
    private BookAdapter bookAdapter;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (books != null) {
            for(Book book:books) {
                if (book.getAcceptBtnStatus() == 0) {
                    delBookFromRemote(book);
                }
            }
            books.clear();
        }
    }
    private void getBooksFromRemote() {
        final Activity activity = this;
        final BookAdapter.Callback callback = this;
        String url = HOST + "/producer/book";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Book>>() {}.getType();
                            books = gson.fromJson(response.toString(), listType);
                            Log.d("books", Arrays.toString(books.toArray()));
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    bookAdapter = new BookAdapter(activity, R.layout.book_view, books, callback);
                                    listView = findViewById(R.id.book_list);
                                    listView.setAdapter(bookAdapter);
                                    bookAdapter.notifyDataSetChanged();
                                }
                            });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("{}", error.toString());
                }}) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(jsonArrayRequest);
    }
    public void refresh(View view) {
        delBooks();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBooksFromRemote();
            }
        }).start();
    }
    public void editDish(View view) {
        Intent intent = new Intent(this, EditDishActivity.class);
        startActivity(intent);
    }


}
