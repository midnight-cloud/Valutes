package com.evg_ivanoff.valutes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evg_ivanoff.valutes.models.DailyJSON;
import com.evg_ivanoff.valutes.models.Valute;
import com.evg_ivanoff.valutes.models.ValuteAdapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String jsonURL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private Map<String, Valute> valutes = null;
    private List<Valute> valutesList = null;

    private Button btnConvert;
    private TextView editConvertValue;
    private RecyclerView recyclerValutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConvert = findViewById(R.id.btnConvert);
        editConvertValue = findViewById(R.id.editConvertValue);
        recyclerValutes = findViewById(R.id.recyclerValutes);



        DownloadJSON downloadJSON = new DownloadJSON();
        downloadJSON.execute(jsonURL);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = null;
                str = editConvertValue.getText().toString();
                double convertValue = 0;
                try {
                    convertValue = Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    convertValue = 0;
                }
                Iterator<Valute> valuteIterator = valutesList.iterator();
                while(valuteIterator.hasNext()){
                    Valute val = valuteIterator.next();
                    Double valuteValue = val.getValue();
                    val.setConvertValue(convertValue * valuteValue);
                }
                recyclerValutes.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private class DownloadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null){
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            DailyJSON dailyJSON = gson.fromJson(s, DailyJSON.class);
            valutes = dailyJSON.valutes;
            valutesList = new ArrayList<Valute>(valutes.values());

            ValuteAdapter valuteAdapter = new ValuteAdapter(MainActivity.this, valutesList);
            recyclerValutes.addItemDecoration(new DividerItemDecoration(recyclerValutes.getContext(), DividerItemDecoration.VERTICAL));
            recyclerValutes.setAdapter(valuteAdapter);
        }
    }
}