package lcukerd.com.movieproject;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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



class cloudconnect extends AsyncTask <String, Void , String>
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
            StringBuffer buffer=null;
            String line;

            while ((line=reader.readLine())!=null)
            {
                buffer.append(line+'\n');
            }
            DATA=buffer.toString();

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
        String Data=encodedData;
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

