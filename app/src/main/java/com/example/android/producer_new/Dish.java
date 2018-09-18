package com.example.android.producer_new;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuan
 */

@Data
@Builder
public class Dish {
    private String name;
    private int price;
    @Override
    public String toString() {
        return "名称：" + name + "\n价格：" + price;
    }
}
