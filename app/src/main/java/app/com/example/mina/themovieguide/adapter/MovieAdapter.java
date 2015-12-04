package app.com.example.mina.themovieguide.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import app.com.example.mina.themovieguide.Data.Movie;

/**
 * Created by Mina on 04-Dec-15.
 */
public class MovieAdapter extends BaseAdapter {

    private ArrayList<Movie> list;
    private LayoutInflater inflater ;
    private Activity view;

    public void MovieAdapter(Activity v, ArrayList<Movie> ls){

        this.list = ls;
        this.view = v;
        this.inflater = v.getLayoutInflater();

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
        return null;
    }
}