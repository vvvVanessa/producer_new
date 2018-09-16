package com.example.android.producer_new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class BookAdapter extends ArrayAdapter<Book> {
    private Callback callBack;
    public BookAdapter(Context context, int resourceId, List<Book> books, Callback callBack) {
        super(context, resourceId, books);
        this.callBack = callBack;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_view, null);
            viewHolder = new ViewHolder();
            viewHolder.bookMsg = convertView.findViewById(R.id.book_msg);
            viewHolder.acceptBtn = convertView.findViewById(R.id.accept_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Book book = getItem(position);
        viewHolder.bookMsg.setText(book.toString());
        viewHolder.acceptBtn.setEnabled(book.getAcceptBtnStatus() == 1 ? true:false);
        viewHolder.acceptBtn.setText(book.getAcceptBtnStatus() == 1 ? "接单" : "已接单");
        viewHolder.acceptBtn.setTag(position);
        viewHolder.acceptBtn.setOnClickListener(new onButtonClickListener());
        return convertView;
    }

    private class onButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int position = (Integer)view.getTag();
            Book book = getItem(position);
            callBack.click(view, position, book);
        }
    }
    public interface Callback {
        void click(View view, int position, Book book);
    }

    private static class ViewHolder {
        TextView bookMsg;
        Button acceptBtn;
    }
}
