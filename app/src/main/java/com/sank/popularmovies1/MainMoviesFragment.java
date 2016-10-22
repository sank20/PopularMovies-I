package com.sank.popularmovies1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;


/*
*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
*/
public class MainMoviesFragment extends Fragment {
  //  private OnFragmentInteractionListener mListener;
    /*ArrayList<String> movieTitleList;
    final String popular = "popular";
    final String top_rated="top_rated";
    public GridView movieGrid;
    private final String LOG_TAG= getClass().getSimpleName();
    SharedPreferences sharedPreferences;
    public MainMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_movies, container, false);
        movieGrid = (GridView) rootView.findViewById(R.id.movies_gridview);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container,new MovieDetailFragment(),"details_fragment");
                ft.commit();
                getActivity().setTitle(movieTitleList.get(position));

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        new GetMovieData(getContext()).execute(sharedPreferences.getString("movies_sorting_preference",popular));

    }

    *//*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//**//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*//*
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
                try {
                    JSONArray popularMoviesArrays = new JSONObject(popularJson).getJSONArray("results");
                    for (int i = 0; i < popularMoviesArrays.length(); i++) {
                        JSONObject obj1 = popularMoviesArrays.getJSONObject(i);
                        String poster = obj1.getString("poster_path");
                        poster = poster.substring(1);
                        movieTitleList.add(obj1.getString("original_title"));
                        imageURLsList.add(getImageUrl("w185", poster));
                        Log.i(LOG_TAG, "poster:" + poster);
                    }
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    if (sharedPreferences.getString("movies_sorting_preference",popular).equals("popular"))
                       getActivity().setTitle("Most Popular Movies");
                    else if(sharedPreferences.getString("movies_sorting_preference",popular).equals("top_rated"))
                        getActivity().setTitle("Top Rated Movies");

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
*/

}
