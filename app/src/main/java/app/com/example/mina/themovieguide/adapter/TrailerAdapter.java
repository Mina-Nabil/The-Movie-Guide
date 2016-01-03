package app.com.example.mina.themovieguide.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.example.mina.themovieguide.Data.Trailer;
import app.com.example.mina.themovieguide.R;

/**
 * Created by Mina on 27-Dec-15.
 */
public class TrailerAdapter extends BaseAdapter {

    ArrayList<Trailer> list ;
    LayoutInflater inflater ;
    final Context View;


    public TrailerAdapter(ArrayList<Trailer> list,Activity v) {
        this.list = list;
        this.inflater = v.getLayoutInflater();
        this.View = v ;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.trailer, null);

            TextView tv = (TextView) convertView.findViewById(R.id.trailer_txt);
            tv.setText(list.get(position).getName());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + list.get(position).getKey())));
                }
            });

        }

        return convertView;
    }
}
