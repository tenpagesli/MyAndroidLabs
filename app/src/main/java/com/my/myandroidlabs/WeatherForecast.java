package com.my.myandroidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    TextView currentTemp;
    TextView minTemp;
    TextView maxTemp;
    TextView uvRating;
    ImageView bmView;
    Bitmap bm; // store the picture for current weather
    String url1 = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    String url2 = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
    String url3Pre = "http://openweathermap.org/img/w/";
    String iconName = "";
    String url3Post = ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        currentTemp = findViewById(R.id.currentTemp);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        uvRating = findViewById(R.id.uvRating);
        bmView = findViewById(R.id.weatherIcon);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar
        ForecastQuery fq = new ForecastQuery();
        //this starts doInBackground on other thread
        fq.execute( url1, url2, url3Pre + iconName + url3Post);

    }

    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class ForecastQuery extends AsyncTask<String, Integer, String>
    {
        double uv;
        String min;
        String max;
        String currTemp;

        // We don't use namespaces
        String ns = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                this.processUVJson(params);
                this.processWeatherXML(params);
                // load over UC rating xml
                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }
            //return type 3, which is String:
            return "Finished task";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTaskExample", "update:" + values[0]);
            if (values[0] == 25) {
                currentTemp.setText("Current temperature is: "+currTemp);
            }
            if (values[0] == 50) {
                minTemp.setText("Minimum temperature is: " + min);
            }
            if (values[0] == 75) {
                maxTemp.setText("Maximum temperature is: " + max);
                uvRating.setText("UV Rating is: " + uv);
            }
            if (values[0] == 100) {
                bmView.setImageBitmap(bm);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27

          //  messageBox.setText("Finished all tasks");
           progressBar.setVisibility(View.INVISIBLE);
        }

        private void processUVJson(String... params) {
            try {
                //get the string url:
                String myUrl = params[1];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                uv = jObject.getDouble("value");
                Log.i("UV is:", "" + uv);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        private void processWeatherXML(String... params) throws XmlPullParserException, IOException{
            //get the string url:
            String myUrl = params[0];

            //create the network connection:
            URL url = new URL(myUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inStream = urlConnection.getInputStream();
            //create a pull parser:
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( inStream  , "UTF-8");  //inStream comes from line 46

            //loop over the weather XML:
            while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                if(xpp.getEventType() == XmlPullParser.START_TAG)
                {
                    String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                    if(tagName.equals("temperature"))
                    {
                        try {
                            currTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25); //tell android to call onProgressUpdate with 25 as parameter
                            progressBar.setProgress(25);
                            Thread.sleep(2000);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50); //tell android to call onProgressUpdate with 50 as parameter
                            progressBar.setProgress(50);
                            Thread.sleep(2000);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75); //tell android to call onProgressUpdate with 75 as parameter
                            progressBar.setProgress(75);
                            Thread.sleep(2000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    else if(tagName.equals("weather"))
                    {
                        iconName = xpp.getAttributeValue(null, "icon");
                        Log.e("Weather img: ", "Found parameter icon: "+ iconName);
                        Log.e("Weather URL: ", url3Pre+iconName+url3Post);
                        // get bitmap
                        bm = getImage(url3Pre, iconName, url3Post);
                        if(bm!=null){
                            publishProgress(100); //tell android to call onProgressUpdate with 100 as parameter
                            progressBar.setProgress(100);
                        }
                    }

                    /**
                     else if(tagName.equals("Temperature")) {
                     xpp.next(); //move to the text between opening and closing tags:
                     String temp = xpp.getText();
                     publishProgress(3); //tell android to call onProgressUpdate with 3 as parameter
                     }
                     **/
                }
                xpp.next(); //advance to next XML event
            }
        }

        private Bitmap getImage(String urlPre, String imgName, String urlPost){
            Bitmap bmp = null;
            String fileName = imgName+".png";
            // check if file exist
            if(this.fileExistance(fileName)){ // if exist, reload file
                Log.e("Weather image: ", "the image is already downloaded in local folder");
                FileInputStream fis = null;
                try {
                    fis = openFileInput(fileName);
                }catch (FileNotFoundException e) {    e.printStackTrace();  }
                bmp = BitmapFactory.decodeStream(fis);
            }else{ // if not exist, download and save
                Log.e("Weather image: ", "the image is not in local folder, starting to download it.");
                String newUrl = urlPre + imgName + urlPost;
                // download image
                try {
                    URL url = new URL(newUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        bmp = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                    // save image
                    if (bmp != null) {
                        FileOutputStream outputStream = openFileOutput(imgName + ".png", Context.MODE_PRIVATE);
                        bmp.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Log.e("Weather image: ", "the image is downloaded successfully and saved in local folder.");
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return bmp;
        }

        // check if file existed
        private boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
    }

}
