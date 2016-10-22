package com.sank.popularmovies1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {

    TextView TVReleaseDate,TVSynopsis,TVRating;

    ImageView imageviewBackdrop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        TVReleaseDate = (TextView) findViewById(R.id.textview_release_date);
        TVSynopsis = (TextView) findViewById(R.id.textview_synopsis);
        TVRating = (TextView) findViewById(R.id.textview_rating);
        Bundle movieDetails = getIntent().getExtras();
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        toFormat.setLenient(false);
        Date relDate=new Date();
        try {
             relDate = dateFormat.parse(movieDetails.getString("release_date"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        TVReleaseDate.setText("Release Date: "+toFormat.format(relDate));
        TVRating.setText("Rating: "+movieDetails.getString("rating"));
        TVSynopsis.setText(movieDetails.getString("overview"));
        imageviewBackdrop = (ImageView) findViewById(R.id.imageview_backdrop);
        setTitle(getIntent().getStringExtra("movie_name"));
        Picasso.with(getApplicationContext()).load(URLDecoder.decode(getIntent().getStringExtra("backdrop"))).
                placeholder(R.drawable.ic_crop_original_black_24dp).fit()
                .error(R.drawable.ic_cross_black_24dp)
                .into(imageviewBackdrop);
    }
}
