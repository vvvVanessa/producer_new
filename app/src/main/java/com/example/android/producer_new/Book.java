package com.example.android.producer_new;

import android.widget.ArrayAdapter;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuan
 */
@Builder
@Getter
@Setter
public class Book {
    private User user;
    private List<Dish> dishes;
    private int acceptBtnStatus;

    private String getDishString() {
        String rets = "";
        for (Dish dish:dishes) {
            rets += dish.getName() + "  ";
        }
        return rets;
    }
    private double getTotPrice() {
        double totPrice = 0;
        for (Dish dish:dishes) {
            totPrice += dish.getPrice();
        }
        return totPrice;
    }
    @Override
    public String toString() {
        return "用户名: " + user.getUsername() +
                "\n联系电话: " + user.getPhone() +
                "\n点餐: " + getDishString() +
                "\n总价: " + getTotPrice();
    }
}
