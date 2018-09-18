package com.example.android.producer_new;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuan
 */
@Builder
@Data
public class User {
    private String username;
    private String password;
    private String phone;
}
