package com.evg_ivanoff.valutes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String jsonURL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private Map<String, Valute> valutes = null;
    private List<Valute> valutesList = null;
    private SharedPreferences preferences;
    private double toConvertValue;

    private Button btnUpdate;
    private TextView editConvertValue;
    private RecyclerView recyclerValutes;
    private TextView textViewConvertValue;
    private TextView textViewConvertName;
    private ValuteAdapter.OnValuteClickListener valuteClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnUpdate = findViewById(R.id.btnUpdate);
        editConvertValue = findViewById(R.id.editConvertValue);
        recyclerValutes = findViewById(R.id.recyclerValutes);
        textViewConvertValue = findViewById(R.id.textViewConvertValue);
        textViewConvertName = findViewById(R.id.textViewConvertName);

        textViewConvertName.setText("Валюта");
        textViewConvertValue.setText("0");

        valuteClickListener = new ValuteAdapter.OnValuteClickListener() {
            @Override
            public void onValuteClick(Valute valute, int position) {
                Toast.makeText(MainActivity.this, "выбран "+valute.getCharCode(),Toast.LENGTH_SHORT).show();
                textViewConvertName.setText(valute.getCharCode());
                textViewConvertValue.setText("");
                toConvertValue = valute.getValue();

                if(toConvertValue != 0) {
                    String str = null;
                    str = editConvertValue.getText().toString();
                    double convertValue = 0;
                    try {
                        convertValue = Double.parseDouble(str);
                    } catch (NumberFormatException e) {
                        editConvertValue.setText("0");
                        convertValue = 0;
                    }
                    textViewConvertValue.setText(new DecimalFormat("#.####").format(convertValue * toConvertValue));
                } else {
                    Toast.makeText(MainActivity.this, "Выберите валюту!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        editConvertValue.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && (i == KeyEvent.KEYCODE_ENTER)) {
                    editConvertValue.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editConvertValue.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String s = preferences.getString("jsonLine", null);
        if (s == null){
            DownloadJSON downloadJSON = new DownloadJSON();
            downloadJSON.execute(jsonURL);
            Log.i("prefLog", "Префы были пустые");
        } else {
            DownloadDataSet(s);
            Log.i("prefLog", "Префы загружены");
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadJSON downloadJSON = new DownloadJSON();
                downloadJSON.execute(jsonURL);
                Toast.makeText(MainActivity.this, "Данные обновлены", Toast.LENGTH_SHORT).show();
                Log.i("prefLog", "Префы обновлены");
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
                preferences.edit().putString("jsonLine", result.toString()).apply();
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
            DownloadDataSet(s);
        }
    }

    private void DownloadDataSet(String s) {
        Gson gson = new Gson();
        DailyJSON dailyJSON = gson.fromJson(s, DailyJSON.class);
        valutes = dailyJSON.valutes;
        valutesList = new ArrayList<Valute>(valutes.values());

        ValuteAdapter valuteAdapter = new ValuteAdapter(MainActivity.this, valutesList, valuteClickListener);
        recyclerValutes.addItemDecoration(new DividerItemDecoration(recyclerValutes.getContext(), DividerItemDecoration.VERTICAL));
        recyclerValutes.setAdapter(valuteAdapter);
    }


}