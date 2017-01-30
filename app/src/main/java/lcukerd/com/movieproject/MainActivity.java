package lcukerd.com.movieproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.util.DisplayMetrics;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.drawable.btn_minus;
import static lcukerd.com.movieproject.R.mipmap.ic_launcher;

public class MainActivity extends AppCompatActivity {

    private String size="w342";
    String Data=null;
    float scale ;
    int i=2;
    TableLayout linear;
    TableRow row;
    TableRow.LayoutParams param;
    Context context=this;
    String defaultValue;
    private CloudConnect cloudconnect;
    private moviePoster PosterUrl;
    private int times=0;
    int width;

    private void displayStart()
    {
        scale = this.getResources().getDisplayMetrics().density;
        Log.d("scale",Float.toString(scale));
        setImagesize();
        linear = (TableLayout) findViewById(R.id.table);
        param = new TableRow.LayoutParams((int) (width-11*scale), (int) (3*(width-11*scale)/2));
        param.rightMargin = (int) (10 * scale + 0.5f);
        param.leftMargin = (int) (6 * scale + 0.5f);

        row = new TableRow(this);
        TextView heading = new TextView(this);
        heading.setText(defaultValue.toUpperCase());
        heading.setTextSize((int) (10 * scale + 0.5f));
        row.addView(heading, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        linear.addView(row);

        Log.d("preference", defaultValue);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        defaultValue = preferences.getString("pref_movie_order","popular");


        displayStart();

        cloudconnect = new CloudConnect();
        cloudconnect.execute(defaultValue,Integer.toString(times));


    }
    @Override
    public void onStart()
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String order = preferences.getString("pref_movie_order","popular");

        if (!order.equals(defaultValue)) {
            times++;
            defaultValue=order;
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            super.onStart();

            displayStart();

            cloudconnect.cancel(true);
            PosterUrl.cancel(true);
            cloudconnect = new CloudConnect();
            cloudconnect.execute(order,Integer.toString(times));
        }
        else
            super.onStart();
    }


    class CloudConnect extends AsyncTask<String, Void , String>
    {
        private HttpURLConnection urlconnection = null;
        private URL url;
        private String time;

        protected String doInBackground(String[] task)
        {
            String DATA=null;
            time=task[1];
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
                    String Poster,id;
                    JSONObject currentMovie = movieArray.getJSONObject(i);

                    Poster = currentMovie.getString(poster);
                    id = currentMovie.getString("id");
                    //Log.d("Movie Poster link " + (i+1), Poster);
                    PosterUrl = new moviePoster();
                    PosterUrl.execute(Poster,id,time);

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
        String MovieId;
        int timee;
        HttpURLConnection getposter = null;
        protected Bitmap doInBackground(String[] link)
        {

            MovieId=link[1];
            Uri Url = Uri.parse(baseURL).buildUpon().appendEncodedPath(size+link[0]).build();
            Log.d("URL for Poster",Url.toString());
            timee=Integer.parseInt(link[2]);
            if (timee!=times)
                return null;

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
        protected void onPostExecute(final Bitmap Poster)
        {
            Bitmap rPoster = Bitmap.createScaledBitmap(Poster,(int) (width-11*scale), (int) (3*(width-11*scale)/2), true);
            if (timee==times) {
                ImageButton image;
                if (i % 2 == 0) {
                    row = new TableRow(context);
                    row.setPaddingRelative(0, 0, 0, (int) (2 * scale + 0.5f));

                    image = new ImageButton(context);
                    //image.setBackgroundResource(R.drawable.pic);
                    image.setImageBitmap(rPoster);

                    row.addView(image, param);
                } else {
                    image = new ImageButton(context);
                    //image.setBackgroundResource(R.drawable.pic2);
                    image.setImageBitmap(rPoster);

                    row.addView(image, new TableRow.LayoutParams((int) (width-11*scale), (int) (3*(width-11*scale)/2)));
                    try
                    {
                        linear.addView(row);
                    }
                    catch (IllegalStateException e)
                    {
                        Log.e("adding row in table","ERROR",e);
                    }
                }
                i++;

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, MovieId, Toast.LENGTH_SHORT).show();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Poster.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        Intent detail = new Intent(context, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, Data).putExtra(Intent.EXTRA_INDEX, MovieId).putExtra("poster", byteArray);
                        startActivity(detail);
                    }
                });

            }
            else
                Log.d("stop","true");

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
            Intent setting = new Intent(this,Setting.class);
            startActivity(setting);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImagesize()
    {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        width = dm.widthPixels / 2;
        Log.d("screen width",Integer.toString(width));
        if (width<92)
            Log.e("screen width too small",Integer.toString(width));
        else if (width<154)
        {
            //size="w92";
            width=92;
        }
        else if (width<185)
        {
            //width=154;
            size="w154";
        }
        else if (width<342)
        {
            //width=185;
            size="w185";
        }
        else if (width<500)
        {
            width=342;
            size="w342";
        }
        else if (width<780)
        {
            //width=500;
            size="w500";
        }
        else
        {
            //width=780;
            size="w780";
        }
    }

}
