package app.com.example.mina.themovieguide.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.example.mina.themovieguide.Data.Review;
import app.com.example.mina.themovieguide.R;

/**
 * Created by Mina on 27-Dec-15.
 */
public class ReviewAdapter extends BaseAdapter {

    private ArrayList<Review> list;
    private LayoutInflater inflater ;
    private Activity view;


    public ReviewAdapter(ArrayList<Review> list, Activity view) {
        this.list = list;
        this.view = view;

        inflater = view.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {

            convertView = inflater.inflate(R.layout.review, null);
            TextView Author = (TextView) convertView.findViewById(R.id.txt_reviewAuthor);
            TextView Content = (TextView) convertView.findViewById(R.id.txt_reviewContent);

            Author.setText(list.get(position).getAuthor());
            Content.setText(list.get(position).getContent());

        }

        return convertView;
    }
}
