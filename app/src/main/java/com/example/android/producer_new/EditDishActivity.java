package com.example.android.producer_new;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditDishActivity extends AppCompatActivity implements DishAdapter.Callback {

    private List<Dish> dishes;
    private ListView listView;
    private DishAdapter dishAdapter;
    private EditText addDishName;
    private EditText addDishPrice;
    private List<Dish> delDishes, addDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu);
        dishes = getDishesFromRemote();
        dishAdapter = new DishAdapter(this, R.layout.book_view, dishes, this);
        listView = findViewById(R.id.edit_dish);
        listView.setAdapter(dishAdapter);
        addDishName = findViewById(R.id.dish_name);
        addDishPrice = findViewById(R.id.dish_price);
        delDishes = new ArrayList<>();
        addDishes = new ArrayList<>();
    }
    public void onReturn(View view) {
        for(int i = delDishes.size() - 1; i >= 0; i--) {
            delDishFromRemote(delDishes.get(i));
            delDishes.remove(i);
        }
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
        System.out.println("caiming:" + addDishName.getEditableText().toString());
        String dishName = addDishName.getEditableText().toString();
        String dishPriceString = addDishPrice.getEditableText().toString();
        for (Dish dish : dishes) {
            if (dish.getName().equals(dishName)) {
                showDialog("请勿重复添加");
                return;
            }
        }
        Double dishPrice;
        try {
            dishPrice = Double.parseDouble(dishPriceString);
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
    private ArrayList<Dish> getDishesFromRemote() {
        return new ArrayList<Dish> () {{
            add(Dish.builder().name("aaa").price((double)10).build());
            add(Dish.builder().name("bbb").price((double)15).build());
            add(Dish.builder().name("ccc").price((double)20).build());
        }};
    }
    private void delDishFromRemote(final Dish dish) {
        String DishName = dish.getName();
        String DishPrice = Double.toString(dish.getPrice());
    }
    private void addDishFromRemote(final Dish dish) {
        String DishName = dish.getName();
        String DishPrice = Double.toString(dish.getPrice());
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
