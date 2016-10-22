package com.sank.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;
import java.util.zip.Inflater;

public class MainActivity extends ActionBarActivity {

    ArrayList<String> movieTitleList;
    final String popular = "popular";
    final String top_rated="top_rated";
    public GridView movieGrid;
    SharedPreferences sharedPreferences;
    ArrayList<String> backdropUriList, movieRatingList, releaseDateList, overviewList;
    private final String LOG_TAG= getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieGrid = (GridView) findViewById(R.id.movies_gridview);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openDetail = new Intent(MainActivity.this,MovieDetailActivity.class);
                Bundle movieDetails = new Bundle();
                movieDetails.putString("movie_name",movieTitleList.get(position));
                movieDetails.putString("backdrop",backdropUriList.get(position));
                movieDetails.putString("overview",overviewList.get(position));
                movieDetails.putString("release_date",releaseDateList.get(position));
                movieDetails.putString("rating",movieRatingList.get(position));
                openDetail.putExtras(movieDetails);
                startActivity(openDetail);
                //setTitle(movieTitleList.get(position));

            }
        });

          /*
 * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
 * performs a swipe-to-refresh gesture.
 */
       /* mySwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                        new GetMovieData(getApplicationContext()).execute();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                       // myUpdateOperation();
                    }
                }
        );*/

    }
    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        new GetMovieData(getApplicationContext()).execute(sharedPreferences.getString("movies_sorting_preference",popular));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=  getMenuInflater();
        inflater.inflate(R.menu.menu_sort_by,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
           /* case R.id.menu_sort_by_most_popular:
                new GetMovieData(getApplicationContext()).execute(popular);
                setTitle("Most Popular movies");
                return  true;

            case R.id.menu_sort_by_top_rated:
                new GetMovieData(getApplicationContext()).execute(top_rated);
                setTitle("Top rated Movies");
                return true;
*/
            case R.id.menu_settings:
                Intent openSettings = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(openSettings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onBackPressed() {

//        if(getSupportFragmentManager().findFragmentById(R.id.main_container) instanceof MovieDetailFragment){
//            getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new MainMoviesFragment()).commit();
//        }else {
         super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        //}
    }



    class GetMovieData extends AsyncTask<String,Void,String> {

        ArrayList<String> imageURLsList;

        Context context;

        public GetMovieData(Context context){
            this.context= context;
        }

        private final String LOG_TAG= getClass().getSimpleName();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... movieList) {
            final String baseURL = "http://api.themoviedb.org/3/movie/";



            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String popularJson = null;
            Uri builtPopularDataUri = Uri.parse(baseURL).buildUpon()
                    .appendPath(movieList[0])
                    .appendQueryParameter("api_key",BuildConfig.THE_MOVIEDB_API_KEY).build();
            try {
                URL popularDataURL = new URL(builtPopularDataUri.toString());
                Log.v(LOG_TAG,"popular uri:"+popularDataURL.toString());
                urlConnection = (HttpURLConnection) popularDataURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularJson = buffer.toString();
                Log.v(LOG_TAG,"json:" +popularJson );
            } catch (Exception e) {
                if(e instanceof UnknownHostException){
                    return null;
                }
                e.printStackTrace();
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return popularJson;

        }

        @Override
        protected void onPostExecute(String popularJson) {
            super.onPostExecute(popularJson);
            if (popularJson == null) {
                Toast.makeText(context, "Something went wrong, please check your internet connection", Toast.LENGTH_LONG).show();
            } else {
                imageURLsList = new ArrayList<>();
                movieTitleList = new ArrayList<>();
                backdropUriList = new ArrayList<>();
                movieRatingList = new ArrayList<>();
                releaseDateList = new ArrayList<>();
                overviewList = new ArrayList<>();
                try {
                    JSONArray popularMoviesArrays = new JSONObject(popularJson).getJSONArray("results");
                    for (int i = 0; i < popularMoviesArrays.length(); i++) {
                        JSONObject obj1 = popularMoviesArrays.getJSONObject(i);
                        String poster = obj1.getString("poster_path");
                        String backdrop = obj1.getString("backdrop_path");
                        poster = poster.substring(1);
                        overviewList.add(obj1.getString("overview"));
                        movieRatingList.add(obj1.getString("vote_average"));
                        releaseDateList.add(obj1.getString("release_date"));
                        backdropUriList.add(getImageUrl("w342", backdrop));
                        movieTitleList.add(obj1.getString("original_title"));
                        imageURLsList.add(getImageUrl("w185", poster));
                        Log.i(LOG_TAG, "poster:" + poster);
                    }
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    if (sharedPreferences.getString("movies_sorting_preference",popular).equals("popular"))
                        setTitle("Most Popular Movies");
                    else if(sharedPreferences.getString("movies_sorting_preference",popular).equals("top_rated"))
                        setTitle("Top Rated Movies");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                movieGrid.setAdapter(new ImageAdapter(context, imageURLsList));
                //  mySwipeRefreshLayout.setRefreshing(false);
            }
        }

        public String getImageUrl(String size, String tag){
            final String imageBaseURL = "http://image.tmdb.org/t/p/";
            final String image_size = "w185"; //you will need a ‘size’, which will be one of the following:
            // "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
            final String image_tag;
            Uri builtUri = Uri.parse(imageBaseURL).buildUpon()
                    .appendPath(size)
                    .appendPath(tag).build();
            return URLDecoder.decode(builtUri.toString());
        }
    }


}
