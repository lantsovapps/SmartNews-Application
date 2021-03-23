package com.lantsovapps.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lantsovapps.newsapplication.Model.Articles;
import com.lantsovapps.newsapplication.Model.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    final String API_KEY = "your api key";
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private List<Articles> articles = new ArrayList<>();
    private String country;
    private TextView tvLogo;
    private EditText edtsearch;
    private Button btnSearch, btnGeneral, btnHealth, btnBusiness, btnScience, btnSport, btnTechnology, btnEntertainment;
    private String text;
    ArrayList <Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        country = getCountry();
        edtsearch = (EditText) findViewById(R.id.editTextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);

        //init menu buttons
        btnGeneral = (Button) findViewById(R.id.btnGeneral);
        btnHealth = (Button) findViewById(R.id.btnHealth);
        btnBusiness = (Button) findViewById(R.id.btnBusiness);
        btnScience = (Button) findViewById(R.id.btnScience);
        btnSport = (Button) findViewById(R.id.btnSport);
        btnTechnology = (Button) findViewById(R.id.btnTechnology);
        btnEntertainment = (Button) findViewById(R.id.btnEntertainment);

        buttons.add(btnGeneral);
        buttons.add(btnHealth);
        buttons.add(btnBusiness);
        buttons.add(btnScience);
        buttons.add(btnSport);
        buttons.add(btnTechnology);
        buttons.add(btnEntertainment);


        tvLogo = (TextView) findViewById(R.id.tVHeader);
        text = "<font color=\"#E4841D\">S</font>News";
        tvLogo.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Common.isConnectedToInternet(MainActivity.this))
                    retrieveJson("", country, API_KEY);
            }
        });

        if(Common.isConnectedToInternet(this)) {
            retrieveJson("", country, API_KEY);
        } else {
            Toast.makeText(this, "Проверьте подключение к сети", Toast.LENGTH_SHORT).show();
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtsearch.getText().toString().equals("")){
                    if(Common.isConnectedToInternet(MainActivity.this))
                        retrieveJson(edtsearch.getText().toString(), country, API_KEY);
                } else {
                    if(Common.isConnectedToInternet(MainActivity.this))
                        retrieveJson("", country, API_KEY);
                }
            }
        });

        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(0);
                if(Common.isConnectedToInternet(MainActivity.this))
                    retrieveJson("", country, API_KEY);
            }
        });

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(1);
                if(Common.isConnectedToInternet(MainActivity.this))
                    getJsonCategory("health", country, API_KEY);
            }
        });

        btnBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(2);
                if(Common.isConnectedToInternet(MainActivity.this))
                    getJsonCategory("business", country, API_KEY);
            }
        });

        btnScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(3);
                if(Common.isConnectedToInternet(MainActivity.this))
                    getJsonCategory("science", country, API_KEY);
            }
        });

        btnSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(4);
                if(Common.isConnectedToInternet(MainActivity.this))
                    getJsonCategory("sport", country, API_KEY);
            }
        });

        btnTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(5);
                if(Common.isConnectedToInternet(MainActivity.this))
                    getJsonCategory("technology", country, API_KEY);
            }
        });

        btnEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus(6);
                if(Common.isConnectedToInternet(MainActivity.this))
                    getJsonCategory("entertainment", country, API_KEY);
            }
        });

    }

    void getJsonCategory(String category, String country, String apiKey){
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call = ApiClient.getInstance().getApi().getCategory(country, category, apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveJson(String query, String country, String apiKey){
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if(!edtsearch.getText().toString().equals("")){
            call = ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        } else {
            call = ApiClient.getInstance().getApi().getHeadlines(country, 100, apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    void setFocus(int position) {
        for (int i = 0; i < buttons.size(); i++){
            if (i != position) {
                buttons.get(i).setBackgroundResource(R.drawable.first_background);
            } else {
                buttons.get(i).setBackgroundResource(R.drawable.orange_background);
            }
        }
    }
}
