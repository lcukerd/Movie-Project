package lcukerd.com.movieproject;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
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
import java.net.URI;
import java.net.URL;



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
        String Data=encodedData;                //supposed to be data memeber of outer class
        try {
            String List = "results";

            String release ="release_date";
            String overview="overview";
            String rating ="vote_average";
            String poster="poster_path";
            String title="title";

            JSONObject movie = new JSONObject(encodedData);
            JSONArray movieArray = movie.getJSONArray(List);
            Log.d("Movie" ,Integer.toString(movieArray.length()));

            /*TableLayout linear = (TableLayout) findViewById(R.id.table);
            TableRow.LayoutParams param = new TableRow.LayoutParams(500,930);
            param.rightMargin=10;
            for (int i=1;i<20;i++) {
                TableRow row = new TableRow(this);
                row.setPaddingRelative(0,0,0,10);

                ImageButton image = new ImageButton(this);
                image.setBackgroundResource(R.drawable.pic);

                row.addView(image, param);



                ImageButton images = new ImageButton(this);
                images.setBackgroundResource(R.drawable.pic2);

                row.addView(images, new TableRow.LayoutParams(540,540));
                linear.addView(row);

            }

            */
            for (int i=0;i<movieArray.length();i++)
            {
                String Release,Overview,Rating,Poster,Title;

                JSONObject currentMovie = movieArray.getJSONObject(i);

                Release = currentMovie.getString(release);
                Overview = currentMovie.getString(overview);
                Rating = currentMovie.getString(rating);
                Poster = currentMovie.getString(poster);
                Title = currentMovie.getString(title);
                Log.d("Movie" + (i+1), Release+" "+Overview+" "+ Rating+" "+Poster+" "+Title);

            }

        }
        catch (JSONException e)
        {
            Log.e("JSON","Error",e);
        }
    }


}