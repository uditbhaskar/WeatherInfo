package com.example.weatherinfo.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.weatherinfo.R;
import com.example.weatherinfo.services.model.DataModel;
import com.example.weatherinfo.viewModel.WeatherInfoViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_search;
    EditText et_location;
    TextView tv_temperature;
    TextView tv_sunrise;
    TextView tv_sunset;
    TextView tv_weatherDescription;
    ImageView iv_WeatherIcon;
    LinearLayout linearLayout;
    ProgressBar progressBar;
    String cityName;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        callViewModel("Bengaluru");

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = et_location.getText().toString();
                callViewModel(cityName);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void callViewModel(String cityName) {
        WeatherInfoViewModel infoViewModel = ViewModelProviders.of(this).get(WeatherInfoViewModel.class);
        infoViewModel.getWeatherInfo(cityName).observe(this, new Observer<DataModel>() {
            @Override
            public void onChanged(DataModel dataModels) {
                Boolean result = infoViewModel.getSuccess().getValue();

                if (result && dataModels!=null) {
                    animationView.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    Double temp = dataModels.getMain().getTemp();
                    int temperature = (int) Math.round(temp - 273);
                    String tempString = String.valueOf(temp);
                    String locationName = dataModels.getName();
                    String sunrise = timeStamp(dataModels.getSys().getSunrise());
                    String sunSet = timeStamp(dataModels.getSys().getSunset());
                    String description = dataModels.getWeather().get(0).getDescription();
                    String iconURL = dataModels.getWeather().get(0).getIcon();
                    String imageUrl = "https://openweathermap.org/img/w/" + iconURL + ".png";

                    if ((tempString!=null && !tempString.isEmpty()) && (locationName != null && !locationName.isEmpty()) && (sunrise != null && !sunrise.isEmpty()) &&
                            (sunSet != null && !sunSet.isEmpty()) &&
                            (description != null && !description.isEmpty()) &&
                            (iconURL != null && !iconURL.isEmpty()) &&
                            (imageUrl != null && !imageUrl.isEmpty())) {
                        Glide.with(MainActivity.this).load(imageUrl).into(iv_WeatherIcon);
                        et_location.setText(locationName);
                        tv_sunrise.setText(sunrise);
                        tv_sunset.setText(sunSet);
                        tv_temperature.setText(String.valueOf(temperature) + "\u00B0");
                        tv_weatherDescription.setText(description);
                    } else {
                        Toast.makeText(MainActivity.this, "Enter valid credentials.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Enter valid city name!", Toast.LENGTH_SHORT).show();
                    linearLayout.setVisibility(View.INVISIBLE);
                    animationView.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    public String timeStamp(int timeStamp) {

        Date date = new Date(timeStamp * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("HH:mm a");
        jdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String java_date = jdf.format(date);
        return java_date;

    }

    private void init() {
        animationView = findViewById(R.id.animation_view);
        btn_search = findViewById(R.id.btn_search);
        et_location = findViewById(R.id.tv_location_current);
        tv_temperature = findViewById(R.id.tv_temperature_current);
        tv_sunrise = findViewById(R.id.tv_sunrise_current);
        tv_sunset = findViewById(R.id.tv_sunset_current);
        tv_weatherDescription = findViewById(R.id.tv_temp_description_current);
        iv_WeatherIcon = findViewById(R.id.iv_icon);
        linearLayout = findViewById(R.id.linear);
        progressBar = findViewById(R.id.progressbar);
    }
}
