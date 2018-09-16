package com.example.android.producer_new;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuan
 */
@Builder
@Setter
@Getter
public class Dish {
    private String name;
    private Double price;
    @Override
    public String toString() {
        return "名称：" + getName() + "\n价格：" + getPrice().toString();
    }
}
