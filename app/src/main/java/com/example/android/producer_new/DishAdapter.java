package com.example.android.producer_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DishAdapter extends ArrayAdapter<Dish> {
    private Callback callback;
    public DishAdapter(Context context, int resourceId, List<Dish> dishes, Callback callback) {
        super(context, resourceId, dishes);
        this.callback = callback;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_view, null);
            viewHolder = new ViewHolder();
            viewHolder.dishMsg = convertView.findViewById(R.id.book_msg);
            viewHolder.del= convertView.findViewById(R.id.accept_btn);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Dish dish = getItem(position);
        viewHolder.dishMsg.setText(dish.toString());
        viewHolder.del.setText("删除");
        viewHolder.del.setTag(position);
        viewHolder.del.setOnClickListener(new onButtonClickListener());
        return convertView;
    }

    private class onButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            int position = (Integer)view.getTag();
            Dish dish = getItem(position);
            callback.clickDel(view, position, dish);
        }
    }
    public interface Callback {
        void clickDel(View view, int position, Dish dish);
    }
    private static class ViewHolder {
        TextView dishMsg;
        Button del;
    }
}
