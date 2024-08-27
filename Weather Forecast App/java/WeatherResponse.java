package com.firstapp.weatherforecastapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("wind")
    private Wind wind;

    // Getters
    public String getName() { return name; }
    public Main getMain() { return main; }
    public List<Weather> getWeather() { return weather; }
    public Wind getWind() { return wind; }

    public static class Main {
        @SerializedName("temp")
        private double temp;

        @SerializedName("temp_min")
        private double tempMin;

        @SerializedName("temp_max")
        private double tempMax;

        @SerializedName("feels_like")
        private double feelsLike;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("pressure")
        private int pressure;

        // Getters
        public double getTemp() { return temp; }
        public double getTempMin() { return tempMin; }
        public double getTempMax() { return tempMax; }
        public double getFeelsLike() { return feelsLike; }
        public int getHumidity() { return humidity; }
        public int getPressure() { return pressure; }
    }

    public static class Weather {
        @SerializedName("description")
        private String description;

        // Getter
        public String getDescription() { return description; }
    }

    public static class Wind {
        @SerializedName("speed")
        private double speed;

        @SerializedName("deg")
        private double deg;

        // Getters
        public double getSpeed() { return speed; }
        public double getDeg() { return deg; }
    }
}

