package lcukerd.com.movieproject;

import android.net.Uri;
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

import static android.R.drawable.btn_minus;
import static lcukerd.com.movieproject.R.mipmap.ic_launcher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       //NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll);

        TableLayout linear = (TableLayout) findViewById(R.id.table);
        TableRow.LayoutParams param = new TableRow.LayoutParams(540,540);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
