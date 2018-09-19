package com.example.android.producer_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.List;

public class EditDishActivity extends AppCompatActivity implements DishAdapter.Callback {
    private final static String HOST = "http://118.25.173.49:8080";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private List<Dish> dishes;
    private ListView listView;
    private DishAdapter dishAdapter;
    private EditText addDishName;
    private EditText addDishPrice;
    private List<Dish> delDishes, addDishes;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(EditDishActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu);
        getDishesFromRemote();
        addDishName = findViewById(R.id.dish_name);
        addDishPrice = findViewById(R.id.dish_price);
        addDishes = new ArrayList<>();
    }
    public void onReturn(View view) {
//        for(int i = delDishes.size() - 1; i >= 0; i--) {
//            delDishFromRemote(delDishes.get(i));
//            delDishes.remove(i);
//        }
        for(int i = addDishes.size() - 1; i >= 0; i--) {
            addDishFromRemote(addDishes.get(i));
            addDishes.remove(i);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void addDish(View view) {
        if(addDishName.getEditableText() == null || addDishName.getEditableText().toString().equals("")) {
            showDialog("菜品名称不能为空");
            return;
        }
      //  System.out.println("caiming:" + addDishName.getEditableText().toString());
        String dishName = addDishName.getEditableText().toString();
        String dishPriceString = addDishPrice.getEditableText().toString();
        for (Dish dish : dishes) {
            if (dish.getName().equals(dishName)) {
                showDialog("请勿重复添加");
                return;
            }
        }
        int dishPrice;
        try {
            dishPrice = Integer.valueOf(dishPriceString);
        } catch(NumberFormatException e) {
            showDialog("请输入正确的菜品价格");
            return;
        }
        addDishes.add(Dish.builder().name(dishName)
                                  .price(dishPrice)
                                  .build());
        dishes.add(Dish.builder().name(dishName)
                                 .price(dishPrice)
                                 .build());
        dishAdapter.notifyDataSetChanged();
    }
    private void getDishesFromRemote() {
        dishes = new ArrayList<Dish> () {{
            add(Dish.builder().name("aaa").price(10).build());
            add(Dish.builder().name("bbb").price(15).build());
            add(Dish.builder().name("ccc").price(20).build());
        }};
        final Activity activity = this;
        final DishAdapter.Callback callback = this;
        String url = HOST + "/producer/menu";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Dish>>() {}.getType();
                        dishes = gson.fromJson(response.toString(), listType);
                        Log.d("books", Arrays.toString(dishes.toArray()));
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                dishAdapter = new DishAdapter(
                                        activity, R.layout.dish_view, dishes, callback);
                                listView = findViewById(R.id.edit_dish);
                                listView.setAdapter(dishAdapter);
                                dishAdapter.notifyDataSetChanged();
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
    private void addDishFromRemote(final Dish dish) {
        String url = HOST + "/producer/menu";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(OBJECT_MAPPER.writeValueAsString(dish));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("hello", jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("{}", error.toString());
            }}){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(jsonObjectRequest);
    }
    @Override
    public void clickDel(View view, int position, Dish dish) {
        delDishes.add(Dish.builder().name(dish.getName()).price(dish.getPrice()).build());
        Iterator<Dish> iter = dishes.iterator();
        while(iter.hasNext()) {
            if(iter.next().equals(dish)) {
                iter.remove();
            }
        }
        dishAdapter.notifyDataSetChanged();
    }
    public void showDialog(String msg) {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
