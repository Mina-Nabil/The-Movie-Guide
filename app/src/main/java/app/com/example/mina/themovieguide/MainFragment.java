package app.com.example.mina.themovieguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;

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
import java.util.ArrayList;

import app.com.example.mina.themovieguide.Data.Movie;
import app.com.example.mina.themovieguide.adapter.MovieAdapter;
import app.com.example.mina.themovieguide.db.MovieColumns;
import app.com.example.mina.themovieguide.db.MovieProvider;

/**
 * Created by Mina on 30-Dec-15.
 */
public class MainFragment extends Fragment {

    ArrayList<Movie> movieList ;
    public MovieAdapter movieAdapter ;
    int currentPage = 1;
    Cursor mCursor ;
    int startIndex, endIndex;
    ScrollView scrollView;
    private boolean updatable;




    public interface Callback {

        public void onItemSelected(Bundle detData);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        startIndex = 0;
        endIndex = 12;
        updatable = true;

        final View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        //Adding Adapter
        GridView grid = (GridView) rootView.findViewById(R.id.grid_view);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie m = (Movie) movieAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Movie_Object", m);

                ((Callback) getActivity())
                        .onItemSelected(bundle);


            }
        });

        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (updatable && firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount >= endIndex && firstVisibleItem != 0) {
                    startIndex = endIndex;
                    endIndex = endIndex + 6;
                    updatable = false;
                    updateMovies();
                }


            }
        });


        movieList = new ArrayList<Movie>();

        movieAdapter = new MovieAdapter(getActivity(), movieList);
        grid.setAdapter(movieAdapter);
        updateMovies();

        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inf) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inf.inflate(R.menu.main, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_refresh) {

            movieList.clear();
            movieAdapter.notifyDataSetChanged();
            updateMovies();
            movieAdapter.notifyDataSetChanged();
            return true ;
        }


        return super.onOptionsItemSelected(item);
    }



    //Start Getting MovieData
    void updateMovies(){



        FetchMovieTask m = new FetchMovieTask();
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = shared.getString(getString(R.string.pref_sort_key), getString(R.string.pref_value_sort_popular));
        if (!sort.equals(getString(R.string.pref_value_sort_fav))) {
           // String sorting = (shared.getString(sort, getString(R.string.pref_value_sort_popular)));
            m.execute(sort);
        }
        else {

            getFavfromDB() ;
            m.execute(getString(R.string.pref_value_sort_popular));
        }




    }


    private void getFavfromDB() {

        String[] cols = {   MovieColumns.MovieID,
                MovieColumns.MovieTitle,
                MovieColumns.MovieDate,
                MovieColumns.MovieImagePath,
                MovieColumns.MovieOverview,
                MovieColumns.MovieRating};

        mCursor = getActivity().getContentResolver().query(
                MovieProvider.Movies.CONTENT_Uri,
                cols, null ,null ,null );

        String movID, movTitle, movDate, movImagePath, movOverview;
        double  movRat ;
        if(mCursor.getCount() > 0 ) {
            mCursor.moveToFirst();
            do {
                movID = mCursor.getString(0);
                movTitle = mCursor.getString(1);
                movDate = mCursor.getString(2);
                movImagePath = mCursor.getString(3);
                movOverview = mCursor.getString(4);
                movRat = mCursor.getDouble(5);

                movieList.add(new Movie(movID, movTitle, movImagePath,movOverview,movRat, movDate ) );
            } while( mCursor.moveToNext() );


            movieAdapter.notifyDataSetChanged();
        }



    }


    // Get MovieList From JSON function
    ArrayList<Movie> getMovieData(String json, int start, int end) throws JSONException {

        ArrayList<Movie> retMovieList = new ArrayList<Movie>();

        final String m_list = "results";
        final String m_Title = "original_title";
        final String m_imagePath = "poster_path";
        final String m_overView = "overview";
        final String m_rating = "vote_average";
        final String m_date = "release_date";
        final String m_id = "id";



        //
        JSONObject movieJson = new JSONObject(json);
        JSONArray JsonmovieArray = movieJson.getJSONArray(m_list);


        for(int i = start ; i < end ; i++){

            String title, imagepath, overview, date, id;

            double rating ;
            if(i < JsonmovieArray.length()) {
                JSONObject JsonMovie = JsonmovieArray.getJSONObject(i);

                title = JsonMovie.getString(m_Title);
                imagepath = JsonMovie.getString(m_imagePath);
                overview = JsonMovie.getString(m_overView);
                date = JsonMovie.getString(m_date);
                id = JsonMovie.getString(m_id);
                rating = JsonMovie.getDouble(m_rating);

                retMovieList.add(new Movie(id, title, imagepath, overview, rating, date));
            } else
            {
                currentPage++ ;
                startIndex = 0;
                endIndex = 12;
                updateMovies();
                break ;
            }
        }

        for (Movie m : retMovieList) {
            Log.v("Check Movies", "Movie: " + m.getTitle() + " " + m.getDate());
        }
        return retMovieList;

    }


    //Fetch Movie Task inner class

    class FetchMovieTask extends AsyncTask<String, Void , ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null ;
            BufferedReader reader = null ;
            ArrayList<Movie> returnMovie = null ;
            String MovieJsonStr = null ;

            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http");
                builder.authority("api.themoviedb.org");
                builder.appendPath("3");
                builder.appendPath("discover");
                builder.appendPath("movie");
                builder.appendQueryParameter("api_key", "78a5c9be4fe143838cfe2fce360c5439");
                builder.appendQueryParameter("sort_by", params[0]);
                builder.appendQueryParameter("page", currentPage+"");

                builder.build();

                URL url = new URL(builder.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();


                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    MovieJsonStr = null;
                }

                Log.v("Check URL", url.toString());
                MovieJsonStr = buffer.toString();
                returnMovie = getMovieData(MovieJsonStr, startIndex, endIndex);




            }catch (MalformedURLException ex){

                Log.e("URL Problem", "Can't build URL Correctly.. check url builder. "  + ex.getMessage());

            }catch (IOException ex ){

                Log.e("IO Error" , "Check the Input Stream " + ex.getMessage());
            }catch (JSONException ex){
                Log.e("JSON Error", "Check JSON Parser "  + ex.getMessage());
            }


            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            return returnMovie;
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            if(movies.size() > 0) {

                boolean found  ;
                for (int j = 0 ; j < movies.size() ; j++) {
                    found = false ;
                    for (int i = 0; i < movieList.size(); i++) {
                        if (movies.get(j).getId().equals(movieList.get(i).getId()))
                        {
                            found = true;
                            break;
                        }
                    }
                    if(!found) movieList.add(movies.get(j));
                }
                movieAdapter.notifyDataSetChanged();
                updatable = true ;
            }

        }
    }



}
