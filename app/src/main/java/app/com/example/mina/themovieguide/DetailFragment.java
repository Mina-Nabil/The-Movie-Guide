package app.com.example.mina.themovieguide;

import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import app.com.example.mina.themovieguide.Data.Movie;
import app.com.example.mina.themovieguide.Data.Review;
import app.com.example.mina.themovieguide.Data.Trailer;
import app.com.example.mina.themovieguide.adapter.ReviewAdapter;
import app.com.example.mina.themovieguide.adapter.TrailerAdapter;
import app.com.example.mina.themovieguide.db.MovieColumns;
import app.com.example.mina.themovieguide.db.MovieProvider;
import app.com.example.mina.themovieguide.db.ReviewColumns;
import app.com.example.mina.themovieguide.db.TrailerColumns;

/**
 * Created by Mina on 03-Jan-16.
 */
public class DetailFragment extends Fragment {


    private Movie detMovie;
    protected Bundle detBundle;
    private TextView detTitle;
    private TextView detYear;
    private TextView detOverview;
    private TextView detTrailerLabel;
    private TextView detReviewLabel;
    private ImageView detImageview;
    private RatingBar detRate;
    private ListView detRevList;
    private ListView detTrailList;
    private Button detFav;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private ArrayList<Review> reviewList;
    private ArrayList<Trailer> trailerList;
    private boolean foundinDB;
    private Cursor mCursor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_fragment, container, false);

        foundinDB = false;
        detTitle = (TextView) rootView.findViewById(R.id.details_title);
        detYear = (TextView) rootView.findViewById(R.id.details_year);
        detOverview = (TextView) rootView.findViewById(R.id.details_overview);
        detImageview = (ImageView) rootView.findViewById(R.id.details_image);
        detRate = (RatingBar) rootView.findViewById(R.id.details_rating);
        detRevList = (ListView) rootView.findViewById(R.id.list_reviews);
        detTrailList = (ListView) rootView.findViewById(R.id.list_trailers);
        detFav = (Button) rootView.findViewById(R.id.but_Fav);
        detTrailerLabel = (TextView) rootView.findViewById(R.id.txt_trailerlabel);
        detReviewLabel = (TextView) rootView.findViewById(R.id.txt_reviewlabel);
        detBundle = getArguments();
        Bundle Movie = detBundle.getBundle("DATA");

        if (Movie !=null) {

            detMovie = (Movie) Movie.getSerializable("Movie_Object");
            if(detMovie != null){
            updateFragment(Movie);
            }


        }else {
            HideLabels();
        }

        return rootView;

    }

    private void HideLabels() {
        detTitle.setVisibility(View.INVISIBLE);
        detYear.setVisibility(View.INVISIBLE);
        detOverview.setVisibility(View.INVISIBLE);
        detImageview.setVisibility(View.INVISIBLE);
        detRate.setVisibility(View.INVISIBLE);
        detRevList.setVisibility(View.INVISIBLE);
        detTrailList.setVisibility(View.INVISIBLE);
        detFav.setVisibility(View.INVISIBLE);
        detReviewLabel.setVisibility(View.INVISIBLE);
        detTrailerLabel.setVisibility(View.INVISIBLE);
    }

    void updateFragment(Bundle b) {

        detMovie = (Movie) b.getSerializable("Movie_Object");
        ShowLabels();
        detTitle.setText(detMovie.getTitle());
        detYear.setText(detMovie.getDate());
        detOverview.setText(detMovie.getOverView());
        detRate.setRating(((float) detMovie.getRating()));
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + detMovie.getImagePath()).into(detImageview);


        detFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMovieData();
            }
        });

        foundinDB = isMovieinDB();


        reviewList = new ArrayList<Review>();
        reviewAdapter = new ReviewAdapter(reviewList, getActivity());
        detRevList.setAdapter(reviewAdapter);
        updateReviews();

        trailerList = new ArrayList<Trailer>();
        trailerAdapter = new TrailerAdapter(trailerList, getActivity());
        detTrailList.setAdapter(trailerAdapter);
        updateTrailers();
    }

    private void ShowLabels() {
        detTitle.setVisibility(View.VISIBLE);
        detYear.setVisibility(View.VISIBLE);
        detOverview.setVisibility(View.VISIBLE);
        detImageview.setVisibility(View.VISIBLE);
        detRate.setVisibility(View.VISIBLE);
        detRevList.setVisibility(View.VISIBLE);
        detTrailList.setVisibility(View.VISIBLE);
        detFav.setVisibility(View.VISIBLE);
        detReviewLabel.setVisibility(View.VISIBLE);
        detTrailerLabel.setVisibility(View.VISIBLE);
    }


    private boolean isMovieinDB() {
        boolean found = false ;

        String[] cols = { MovieColumns.MovieID};
        mCursor = getActivity().getContentResolver().query(
                MovieProvider.Movies.withId(detMovie.getId()),
                cols, null ,null ,null );

        if (mCursor.getCount() > 0)
            found = true ;
        else found = false ;

        return found ;
    }

    private void insertMovieData() {

        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        ContentProviderOperation.Builder builder = ContentProviderOperation
                .newInsert(MovieProvider.Movies.CONTENT_Uri);
        builder.withValue(MovieColumns.MovieID,detMovie.getId());
        builder.withValue(MovieColumns.MovieDate, detMovie.getDate());
        builder.withValue(MovieColumns.MovieImagePath, detMovie.getImagePath());
        builder.withValue(MovieColumns.MovieOverview, detMovie.getOverView());
        builder.withValue(MovieColumns.MovieRating, detMovie.getRating());
        builder.withValue(MovieColumns.MovieTitle, detMovie.getTitle());

        ContentProviderOperation.Builder revbuilder = ContentProviderOperation
                .newInsert(MovieProvider.Reviews.CONTENT_Uri);
        for (Review it: reviewList) {
            revbuilder.withValue(ReviewColumns.ReviewAuthor, it.getAuthor());
            revbuilder.withValue(ReviewColumns.ReviewContent, it.getContent());
            revbuilder.withValue(ReviewColumns.ReviewMovieID, detMovie.getId());
        }

        ContentProviderOperation.Builder trailbuilder = ContentProviderOperation
                .newInsert(MovieProvider.Trailers.CONTENT_Uri);
        for (Trailer it : trailerList){
            trailbuilder.withValue(TrailerColumns.TrailerKey, it.getKey());
            trailbuilder.withValue(TrailerColumns.TrailerMovieID, detMovie.getId());
            trailbuilder.withValue(TrailerColumns.TrailerTitle, it.getName());
        }

        batchOperations.add(builder.build());
        if(reviewList.size() > 0 ) batchOperations.add(revbuilder.build());
        if(trailerList.size() > 0) batchOperations.add(trailbuilder.build());

        try {
            getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

    }


    void updateReviews() {

        if(!foundinDB) {
            FetchMovieReviews fetchMovieReviews = new FetchMovieReviews();
            fetchMovieReviews.execute(detMovie.getId());
        }
        else
        {
            getRevDataFromDB();
        }
    }




    private void getRevDataFromDB() {

        String [] cols = {ReviewColumns.ReviewAuthor, ReviewColumns.ReviewContent};

        mCursor = getActivity().getContentResolver().query(
                MovieProvider.Reviews.CONTENT_Uri,
                cols,
                ReviewColumns.ReviewMovieID + " = " + detMovie.getId(),
                null, null
        ) ;

        if(mCursor.getCount() > 0){

            mCursor.moveToFirst();
            do {
                reviewList.add(new Review(mCursor.getString(0), mCursor.getString(1)));
            }while(mCursor.moveToNext());

            reviewAdapter.notifyDataSetChanged();
        }

    }


    void updateTrailers() {
        if(!foundinDB) {
            FetchMovieTrailers fetchMovieTrailers = new FetchMovieTrailers();
            fetchMovieTrailers.execute(detMovie.getId());
        } else {
            getTrailerDataFromDB();
        }
    }

    private void getTrailerDataFromDB() {
        String [] cols = {TrailerColumns.TrailerTitle, TrailerColumns.TrailerKey};

        mCursor = getActivity().getContentResolver().query(
                MovieProvider.Trailers.CONTENT_Uri,
                cols,
                TrailerColumns.TrailerMovieID + " = " + detMovie.getId(),
                null, null
        ) ;

        if(mCursor.getCount() > 0){

            mCursor.moveToFirst();
            do {
                trailerList.add(new Trailer(mCursor.getString(0), mCursor.getString(1)));
            }while(mCursor.moveToNext());

            trailerAdapter.notifyDataSetChanged();
        }
    }


    class FetchMovieReviews extends AsyncTask<String, Void, ArrayList<Review>> {


        @Override
        protected ArrayList<Review> doInBackground(String... params) {

            HttpURLConnection urlConnection = null ;
            BufferedReader reader = null ;
            ArrayList<Review> returnReview = null ;
            String ReviewJsonStr = null ;

            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http");
                builder.authority("api.themoviedb.org");
                builder.appendPath("3");
                builder.appendPath("movie");
                builder.appendPath(detMovie.getId());
                builder.appendPath("reviews");
                builder.appendQueryParameter("api_key", "78a5c9be4fe143838cfe2fce360c5439");


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
                    ReviewJsonStr = null;
                }

                ReviewJsonStr = buffer.toString();
                returnReview = getReviewData(ReviewJsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return returnReview;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);

            if(reviews.size() > 0) {
                reviewList.addAll(reviews);
                reviewAdapter.notifyDataSetChanged();
            }
        }

        private ArrayList<Review> getReviewData(String reviewJsonStr) throws JSONException{

            String JSONresult = "results";
            String author = new String();
            String content = new String();
            JSONObject review = new JSONObject();
            ArrayList<Review> retArray = new ArrayList<Review>();
            JSONObject data = new JSONObject(reviewJsonStr);
            JSONArray result_Array = data.getJSONArray(JSONresult);

            for (int i = 0; i<result_Array.length() ; i++)
            {
                review = result_Array.getJSONObject(i);
                author = review.getString("author");
                content = review.getString("content");
                retArray.add(new Review(author, content));

            }

            return retArray;

        }
    }



    class FetchMovieTrailers extends AsyncTask<String, Void, ArrayList<Trailer>> {


        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null ;
            BufferedReader reader = null ;
            ArrayList<Trailer> returnTrailer = null ;
            String trailerJsonStr = null ;

            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http");
                builder.authority("api.themoviedb.org");
                builder.appendPath("3");
                builder.appendPath("movie");
                builder.appendPath(detMovie.getId());
                builder.appendPath("videos");
                builder.appendQueryParameter("api_key", "78a5c9be4fe143838cfe2fce360c5439");


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
                    trailerJsonStr = null;
                }

                trailerJsonStr = buffer.toString();
                returnTrailer = getTrailerData(trailerJsonStr);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return returnTrailer;
        }


        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            super.onPostExecute(trailers);
            if (trailers.size() > 0) {


                trailerList.addAll(trailers);
                trailerAdapter.notifyDataSetChanged();
            }
        }

        private ArrayList<Trailer> getTrailerData(String trailerJsonStr) throws JSONException{

            JSONObject data = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = data.getJSONArray("results");
            ArrayList<Trailer> retArray = new ArrayList<Trailer>();

            for (int i = 0 ; i < trailerArray.length() ; i++)
            {
                String key = trailerArray.getJSONObject(i).getString("key");
                String Title = trailerArray.getJSONObject(i).getString("name");
                retArray.add(new Trailer(Title, key));

            }



            return retArray;
        }
    }









}
