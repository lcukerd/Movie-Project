package lcukerd.com.movieproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String MovieId = intent.getStringExtra(Intent.EXTRA_INDEX);
        String Data = intent.getStringExtra(Intent.EXTRA_TEXT);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("poster");

        Bitmap poster = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        displayDetail(Data,MovieId,poster);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    private void displayDetail(String Data,String MovieId,Bitmap poster)
    {
        int i;
        TextView title = (TextView) findViewById(R.id.title);
        ImageView Poster =(ImageView) findViewById(R.id.poster);
        TextView rating= (TextView) findViewById(R.id.rating) ,release= (TextView) findViewById(R.id.release);
        TextView desc = (TextView) findViewById(R.id.desc);

        try {
            String List = "results";

            String mrelease ="release_date";
            String overview="overview";
            String mrating ="vote_average";
            String mtitle="title";

            JSONObject movie = new JSONObject(Data);
            JSONArray movieArray = movie.getJSONArray(List);
            Log.d("Movie" ,Integer.toString(movieArray.length()));
            JSONObject currentMovie=null;

            for (i=0;i<movieArray.length();i++)
            {
                currentMovie = movieArray.getJSONObject(i);
                String id;
                id=currentMovie.getString("id");
                if (id.equals(MovieId))
                {
                    Log.d("index",Integer.toString(i)+" "+id+" "+MovieId);
                    break;
                }

            }
            String Release,Overview,Rating,Title;

            Release = currentMovie.getString(mrelease);
            Overview = currentMovie.getString(overview);
            Rating = currentMovie.getString(mrating);
            Title = currentMovie.getString(mtitle);
            //Log.d("Movie" + (i+1), Release+" "+Overview+" "+ Rating+" "+Poster+" "+Title);

            title.setText(Title);
            Poster.setImageBitmap(poster);
            desc.setText(Overview);
            rating.setText("Rating : "+Rating);
            release.setText("Release Date : "+Release);
        }
        catch (JSONException e)
        {
            Log.e("JSON","Error",e);
        }
    }

}
