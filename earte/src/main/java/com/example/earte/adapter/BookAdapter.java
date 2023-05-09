package com.example.earte.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.earte.R;
import com.example.earte.db.BookTESST;

import java.util.List;

public class BookAdapter extends BaseAdapter {


    private final Context context;
    private final List<BookTESST> query;

    public BookAdapter(FragmentActivity activity, List<BookTESST> query) {
        this.context = activity;
        this.query = query;
    }

    @Override
    public int getCount() {
        return query.size();
    }

    @Override
    public Object getItem(int position) {
        return query.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BookTESST bookTESST = (BookTESST) getItem(position);

        ViewHolder viewHolder = null;
        View view ;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_1, null);
            viewHolder = new ViewHolder();
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.message = view.findViewById(R.id.message);
            viewHolder.tv_data = view.findViewById(R.id.tv_data);

            view.setTag(viewHolder);

        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(bookTESST.title);
        viewHolder.message.setText(bookTESST.description);
        viewHolder.tv_data.setText(bookTESST.ctime);

        return view;
    }

    public class ViewHolder{
        TextView title;
        TextView message;
        TextView tv_data;
    }
}
