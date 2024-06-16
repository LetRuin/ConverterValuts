package com.example.converter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    private final String url = "https://www.cbr.ru/scripts/XML_daily.asp";
    RecyclerView elementsList; //лист со всеми валютами
    EditText value; //введеное значение
    ValuteAdapter adapter; // адаптер для отображения валют
    ParseXML parseXML;
    List<Valute> listOfValute = new ArrayList<Valute>();
    TextView charCode;
    TextView name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        elementsList = findViewById(R.id.elementsList);
        value = findViewById(R.id.value);
        charCode = findViewById(R.id.charCode);
        name = findViewById(R.id.name);

        adapter = new ValuteAdapter(this, listOfValute, valuteClickListener);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        elementsList.setLayoutManager(layoutManager);
        elementsList.setAdapter(adapter);

        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double temp = 0;
                if (isNumber(charSequence.toString())) {
                    temp = Double.parseDouble(charSequence.toString());
                }
                adapter.setValueValut(temp);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Thread myThread = new Thread(new ThreadloadValutes());
        myThread.start();
    }

    private String download(String urlPath) throws IOException {
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpsURLConnection) url.openConnection();
            stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream, "windows-1251"));

            String line;
            while ((line = reader.readLine()) != null) {
                xmlResult.append(line);
            }
            return xmlResult.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    } //Проверка на число

    ValuteAdapter.OnValuteClickListener valuteClickListener = new ValuteAdapter.OnValuteClickListener() {
        @Override
        public void onValuteClick(Valute valute, int position) {
            name.setText(valute.name);
            charCode.setText(valute.charCode);
            adapter.setVunitRate(valute.vunitRate);
        }
    };

    class ThreadloadValutes implements Runnable {
        @SuppressLint("ResourceAsColor")
        public void run() {
            try {
                String text = download(url);

                parseXML = new ParseXML();
                parseXML.parseXML(text);
                listOfValute = parseXML.listOfValute;

                runOnUiThread(() -> {
                    adapter.load(listOfValute);
                    name.setText("Дата котировок - " + parseXML.data);
                    name.setTextColor(charCode.getTextColors());
                });
            } catch (IOException e) {
                name.setText("Нет интернета чтобы загрузить котировки!");
                name.setTextColor(Color.RED);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                run();
            }
        }
    }
}

