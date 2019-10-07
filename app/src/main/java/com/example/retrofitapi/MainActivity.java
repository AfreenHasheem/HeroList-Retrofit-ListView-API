package com.example.retrofitapi;

import android.app.ProgressDialog;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

   // ListView listView;
    RecyclerView recyclerView;
    HeroAdapter heroAdapter;
    ConnectionDetector mConnectionDetector;
    ProgressDialog progressDialog;
    int cacheSize = 10 * 1024 * 1024; // 10 MiB


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mConnectionDetector = new ConnectionDetector(getApplicationContext());


        if (!mConnectionDetector.isConnectingToInternet()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            loadHeroes();


        } else {
            loadHeroes();
        }
    }

    private void loadHeroes(){

        try{
            if (Api.BASE_URL.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
        }

            Cache cache = new Cache(getCacheDir(), cacheSize);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                            Request request = chain.request();
                            if (!mConnectionDetector.isConnectingToInternet()){
                                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                                request = request
                                        .newBuilder()
                                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                        .build();
                            }

                            return chain.proceed(request);
                        }
                    })
                    .build();


        //create retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //build our API
        Api api = retrofit.create(Api.class);

        //now making the call object
        //Here we are using the api method that we created inside the api interface
        Call<List<HeroModel>> call = api.getHeroes();

        //then finallly we are making the call using enqueue()
        //it takes callback interface as an argument
        //and callback is having two methods onRespnose() and onFailure
        //if the request is successfull we will get the correct response and onResponse will be executed
        //if there is some error we will get inside the onFailure() method


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Its loading....");
        progressDialog.setTitle("ProgressDialog bar example");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDialog.show();


        call.enqueue(new Callback<List<HeroModel>>() {
            @Override
            public void onResponse(Call<List<HeroModel>> call, Response<List<HeroModel>> response) {
                //get the list of heroes

                List<HeroModel> heroesList = response.body();

               String[] heroes = new String[heroesList.size()];

                for (int i = 0; i < heroesList.size(); i++) {
                    heroes[i] = heroesList.get(i).getName();
                }

                heroAdapter = new HeroAdapter(heroesList, getApplicationContext());
                recyclerView.setAdapter(heroAdapter);

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<HeroModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

