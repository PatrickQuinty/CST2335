package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {

    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progress = findViewById(R.id.weatherProgress);
        progress.setVisibility(View.VISIBLE);
        new ForecastQuery().execute();
    }


    private class ForecastQuery extends AsyncTask<String, Integer, String>
    {

        private String uv, min, max, current, weatherCond;
        private Bitmap weather = null;

        @Override
        protected String doInBackground(String... strings) {

            String ret = null;
            String queryTemp = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&" +
                    "APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric",
            queryUV = "http://api.openweathermap.org/data/2.5/uvi?" +
                    "appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

            try {       // Connect to the server:
                URL url = new URL(queryTemp);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                ///String value, min, max;

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if(tagName.equals("temperature"))
                            {
                                current = xpp.getAttributeValue(null,"value");
                                publishProgress(25);
                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                            else if(tagName.equals("weather"))
                            {
                                weatherCond = xpp.getAttributeValue(null, "icon");
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }

                URL urlUv = new URL(queryUV);
                HttpURLConnection urlConnectionUv = (HttpURLConnection) urlUv.openConnection();
                InputStream inStreamUv = urlConnectionUv.getInputStream();
                ///String value, min, max;
                BufferedReader read = new BufferedReader(new InputStreamReader(inStreamUv, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = read.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                //Set up the XML parser:
                JSONObject jsonObject = new JSONObject(result);
                double value = jsonObject.getDouble("value");
                uv = Double.toString(value);


                FileInputStream fis;
                try
                {
                    fis = openFileInput(weatherCond + ".png");
                    Log.i("Weather Forecast", "Looking for file: "+ weatherCond + ".png");
                    weather = BitmapFactory.decodeStream(fis);
                }
                catch (FileNotFoundException e) {    e.printStackTrace();  }

                if(weather == null)
                {
                    Log.i("Weather Forecast", "Image was downloaded");
                    String urlString= "http://openweathermap.org/img/w/" + weatherCond + ".png";
                    URL imgURL = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) imgURL.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        Log.i("Weather Forecast", "RESPONSE CODE IF STATEMENT WORKS");
                    weather = BitmapFactory.decodeStream(connection.getInputStream());

                    }
                }
                else
                    Log.i("Weather Forecast", "Image found locally");
                publishProgress(100);

                FileOutputStream outputStream = openFileOutput( weatherCond + ".png", Context.MODE_PRIVATE);
                weather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();




            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; return ret;}
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?"; return ret;}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ; return ret;}
            catch (JSONException e) {
                e.printStackTrace();
            }
            //What is returned here will be passed as a parameter to onPostExecute:


            return ret;
        }

        public boolean fileExistance(String fName)
        {
            File file = getBaseContext().getFileStreamPath(fName);
            return file.exists();
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(values[0]);
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //ForecastQuery thisApp = this;
            if(sentFromDoInBackground!= null)
                Toast.makeText(getApplicationContext(), sentFromDoInBackground, Toast.LENGTH_LONG).show();


            TextView uvView = findViewById(R.id.uvRating),
                    currentView = findViewById(R.id.tempCurrent),
            minView = findViewById(R.id.tempMin),
            maxView = findViewById(R.id.tempMax);

            ImageView imgView = findViewById(R.id.weatherPic);
            imgView.setImageBitmap(weather);
            uvView.setText("The UV rating is: " +uv);
            currentView.setText("The current temperature is: " + current + "C");
            minView.setText("The min temeperature is: " + min + "C");
            maxView.setText("The max temperature is: " +max + "C");
            progress.setVisibility(View.INVISIBLE);
            //update GUI Stuff:

        }
    }


}

