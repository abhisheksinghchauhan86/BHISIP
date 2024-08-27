package com.firstapp.weatherforecastapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "1f3f196d163cc534054c8e08bcc1979c"; // Replace with your actual API key
    private WeatherApiService weatherApiService;
    private EditText cityInput;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        weatherApiService = retrofit.create(WeatherApiService.class);

        cityInput = findViewById(R.id.cityInput);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityInput.getText().toString();
                if (!cityName.isEmpty()) {
                    fetchWeather(cityName);
                }
            }
        });
    }

    private void fetchWeather(String cityName) {
        Call<WeatherResponse> call = weatherApiService.getWeather(cityName, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weather = response.body();
                    if (weather != null) {
                        Log.d("WeatherApp", "Temperature: " + weather.getMain().getTemp());
                        Log.d("WeatherApp", "Min Temp: " + weather.getMain().getTempMin());
                        Log.d("WeatherApp", "Max Temp: " + weather.getMain().getTempMax());
                        Log.d("WeatherApp", "Pressure: " + weather.getMain().getPressure());
                        Log.d("WeatherApp", "Wind Direction: " + weather.getWind().getDeg());
                        updateUI(weather);
                    }
                } else {
                    // Log and display the error body and status code
                    try {
                        String errorBody = response.errorBody().string();
                        ((TextView) findViewById(R.id.cityName)).setText("Error fetching data: " + response.code() + " " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        ((TextView) findViewById(R.id.cityName)).setText("Error fetching data: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
                ((TextView) findViewById(R.id.cityName)).setText("Error fetching data: " + t.getMessage());
            }
        });
    }

    private void updateUI(WeatherResponse weather) {
        ((TextView) findViewById(R.id.cityName)).setText(weather.getName());
        ((TextView) findViewById(R.id.temp)).setText("Temperature: " + weather.getMain().getTemp() + "°C");
        ((TextView) findViewById(R.id.description)).setText("Description: " + weather.getWeather().get(0).getDescription());
        ((TextView) findViewById(R.id.tempMin)).setText("Min Temp: " + weather.getMain().getTempMin() + "°C");
        ((TextView) findViewById(R.id.tempMax)).setText("Max Temp: " + weather.getMain().getTempMax() + "°C");
        ((TextView) findViewById(R.id.feelsLike)).setText("Feels Like: " + weather.getMain().getFeelsLike() + "°C");
        ((TextView) findViewById(R.id.humidity)).setText("Humidity: " + weather.getMain().getHumidity() + "%");
        ((TextView) findViewById(R.id.pressure)).setText("Pressure: " + weather.getMain().getPressure() + " hPa");
        ((TextView) findViewById(R.id.windSpeed)).setText("Wind Speed: " + weather.getWind().getSpeed() + " m/s");
        ((TextView) findViewById(R.id.windDirection)).setText("Wind Direction: " + weather.getWind().getDeg() + "°");
    }
}



