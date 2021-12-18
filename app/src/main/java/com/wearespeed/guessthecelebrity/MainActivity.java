package com.wearespeed.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> link = new ArrayList<>();
    public static ArrayList<Integer> taken = new ArrayList<>();
    public static ArrayList<Bitmap> images;

    public ProgressBar progressBar;

    class WebScrapingName extends AsyncTask<String,Void,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            String url = strings[0];
            try {
                URL websiteURL = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) websiteURL.openConnection();

                InputStreamReader ir = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(ir);
                int count=0;

                String ans="";
                String temp="";

                while((temp=br.readLine())!=null)
                {
                    if(count>=1400)
                    {
                        ans+=temp;
                    }
                    if(count>=2098)
                    {
                        break;
                    }
                    count++;

                }

                ir.close();
                br.close();

                ArrayList<String> names = new ArrayList<>();

                Pattern p = Pattern.compile("<img alt=\"(.*?)\"");
                Matcher m = p.matcher(ans);

                while(m.find())
                {
                    names.add(m.group(1).trim());
                }
                return names;

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Exception","wtf");
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            for(int i=0;i<strings.size();i++)
            {
                name.add(strings.get(i));
            }
            Log.i("Names: ",name.toString());
        }
    }

    class WebScrapingLink extends AsyncTask<String,Void,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String url = strings[0];
            try {
                URL websiteURL = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) websiteURL.openConnection();

                InputStreamReader ir = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(ir);
                int count=0;

                String ans="";
                String temp="";

                while((temp=br.readLine())!=null)
                {
                    if(count>=1400)
                    {
                        ans+=temp;
                    }
                    if(count>=2098)
                    {
                        break;
                    }
                    count++;

                }

                ir.close();
                br.close();

                ArrayList<String> links = new ArrayList<>();

                Pattern p = Pattern.compile("src=\"(.*?)\"");
                Matcher m = p.matcher(ans);

                while(m.find())
                {
                    links.add(m.group(1).trim());
                }

                return links;

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Exception","wtf");
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            for(int i=0;i<strings.size();i++)
            {
                link.add(strings.get(i));
                taken.add(0);
            }

            GetPhotoLink hmmm = new GetPhotoLink();
            hmmm.execute(link);

        }
    }

    class GetPhotoLink extends  AsyncTask<ArrayList<String>,Void,ArrayList<Bitmap>>
    {
        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... arrayLists) {

            try{
                ArrayList<String> temp = arrayLists[0];

                ArrayList<Bitmap> bms = new ArrayList<>();

                for(int i=0;i<temp.size();i++)
                {
                    URL url = new URL(temp.get(i));
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();

                    InputStream is = httpURLConnection.getInputStream();

                    Bitmap bm = BitmapFactory.decodeStream(is);
                    bms.add(bm);
                    is.close();
                }
                return bms;

            } catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {

            images=bitmaps;

            progressBar.setVisibility(View.INVISIBLE);

            Button go = findViewById(R.id.goButton);
            go.setVisibility(View.VISIBLE);
        }
    }


    public void onClickGo(View view)
    {
        Log.i("Name Count",Integer.toString(name.size()));
        Log.i("Link Count",Integer.toString(link.size()));
        Log.i("Images Count",Integer.toString(images.size()));
        Log.i("Taken Count",Integer.toString(taken.size()));

        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            WebScrapingName hehe = new WebScrapingName();
            WebScrapingLink hehe1 = new WebScrapingLink();

            String URL = "https://www.imdb.com/list/ls052283250/";

            hehe.execute(URL);
            hehe1.execute(URL);

            Button button = findViewById(R.id.goButton);

            progressBar = findViewById(R.id.progressBar);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}