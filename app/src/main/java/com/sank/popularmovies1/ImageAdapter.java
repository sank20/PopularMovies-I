package com.sank.popularmovies1;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by sanket on 9/5/16.
 */
public class ImageAdapter extends BaseAdapter {

    private final String LOG_TAG= getClass().getSimpleName();
    private Context mContext;
    //ArrayList<String> movieNames;
    ArrayList<String> imageURLlist;
    public ImageAdapter(Context context,ArrayList<String> imageURLlist){
        mContext = context;
 //       this.movieNames = movieNames;
        this.imageURLlist = imageURLlist;
    }

    @Override
    public int getCount() {
        return imageURLlist.size();
    }

    @Override
    public Object getItem(int position) {
        return imageURLlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView movieImageView;
        if(convertView == null){
            movieImageView = new ImageView(mContext);
            movieImageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            movieImageView.setAdjustViewBounds(true);
            movieImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            /*movieImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = ((ActionBarActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container,new MovieDetailFragment(),"details_fragment");
                    ft.commit();
                    ((ActionBarActivity) mContext).setTitle(movieNames.get(position));

                }
            });*/
        }else{
            movieImageView = (ImageView) convertView;
        }

        Log.i(LOG_TAG,"pos:"+position);

        Log.i("imageurl:",imageURLlist.get(position));
        //movieImageView.setImageResource(R.drawable.ic_image_alt);
        Picasso.with(mContext).load(URLDecoder.decode(imageURLlist.get(position))).
                placeholder(R.drawable.ic_crop_original_black_24dp).fit()
        .error(R.drawable.ic_cross_black_24dp)
        .into(movieImageView);
        return movieImageView;
    }
}
