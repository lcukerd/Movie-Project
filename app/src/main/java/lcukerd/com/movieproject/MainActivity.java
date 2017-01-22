package lcukerd.com.movieproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.drawable.btn_minus;
import static lcukerd.com.movieproject.R.mipmap.ic_launcher;

public class MainActivity extends AppCompatActivity {

    String Data=null;
    int i=2;
    TableLayout linear;
    TableRow row;
    TableRow.LayoutParams param;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linear = (TableLayout) findViewById(R.id.table);
        param = new TableRow.LayoutParams(500,750);
        param.rightMargin=30;
        param.leftMargin=20;

        CloudConnect cloudconnect = new CloudConnect();
        cloudconnect.execute("popular");





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    class CloudConnect extends AsyncTask<String, Void , String>
    {
        private HttpURLConnection urlconnection = null;
        private URL url;

        protected String doInBackground(String[] task)
        {
            String DATA=null;
            String baseAddress="https://api.themoviedb.org/3/movie/";
            String apiKey="225b36fd29826b4c9821dd90bfc4e055";
            Uri Url = Uri.parse(baseAddress).buildUpon().appendEncodedPath(task[0]).appendQueryParameter("api_key",apiKey).build();
            Log.d("built URL",Url.toString());
            try
            {
                url= new URL(Url.toString());
                urlconnection= (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("GET");
                urlconnection.connect();

                InputStream inputStream = urlconnection.getInputStream();
                if (inputStream==null)
                {
                    return null;
                }
                BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));

                DATA=reader.readLine();

            }
            catch(IOException e)
            {
                Log.e("cloudConnect", "Error ", e);
                return null;
            }

            return DATA;


        }

        protected void onPostExecute(String encodedData)
        {
            Data=encodedData;
            try {
                String List = "results";
                String poster="poster_path";

                JSONObject movie = new JSONObject(encodedData);
                JSONArray movieArray = movie.getJSONArray(List);

                for (int i=0;i<movieArray.length();i++)
                {
                    String Poster;
                    JSONObject currentMovie = movieArray.getJSONObject(i);

                    Poster = currentMovie.getString(poster);
                    //Log.d("Movie Poster link " + (i+1), Poster);
                    moviePoster PosterUrl = new moviePoster();
                    PosterUrl.execute(Poster);

                }

            }
            catch (JSONException e)
            {
                Log.e("JSON","Error",e);
            }
        }


    }
    class moviePoster extends AsyncTask<String, Void ,Bitmap>
    {
        private String baseURL="http://image.tmdb.org/t/p/";
        private String size="w500";
        HttpURLConnection getposter = null;
        protected Bitmap doInBackground(String[] link)
        {
            Uri Url = Uri.parse(baseURL).buildUpon().appendEncodedPath(size).appendEncodedPath(link[0]).build();
            Log.d("URL for Poster",Url.toString());
            Bitmap Poster;
            try
            {
                URL url = new URL(Url.toString());

                getposter=(HttpURLConnection) url.openConnection();
                getposter.setRequestMethod("GET");
                getposter.connect();

                Poster = BitmapFactory.decodeStream(getposter.getInputStream());
                return Poster;


            }
            catch(IOException e)
            {
                Log.e("Poster","URL exception",e);
                return null;
            }

        }
        protected void onPostExecute(Bitmap Poster)
        {
            ImageButton image;
            if(i%2==0)
            {
                row = new TableRow(context);
                row.setPaddingRelative(0,0,0,10);

                image = new ImageButton(context);
                //image.setBackgroundResource(R.drawable.pic);
                image.setImageBitmap(Poster);

                row.addView(image, param);
            }
            else
            {
                ImageButton image = new ImageButton(context);
                //image.setBackgroundResource(R.drawable.pic2);
                image.setImageBitmap(Poster);

                row.addView(image, new TableRow.LayoutParams(500,750));
                linear.addView(row);
            }
            i++;

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
