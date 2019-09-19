package com.example.retrofitapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewHeroes);

        getHeroes();

    }

    private void getHeroes(){
        //create retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
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

        call.enqueue(new Callback<List<HeroModel>>() {
            @Override
            public void onResponse(Call<List<HeroModel>> call, Response<List<HeroModel>> response) {
                //get the list of heroes

                List<HeroModel> heroesList = response.body();

               String[] heroes = new String[heroesList.size()];

                for (int i = 0; i < heroesList.size(); i++) {
                    heroes[i] = heroesList.get(i).getName();
                }

                //displaying the string array into listview
                listView.setAdapter(
                        new ArrayAdapter<String>(
                                getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                heroes));


                for(HeroModel h: heroesList){
                    Log.d("name", h.getName());
                    Log.d("real name", h.getRealname());
                    Log.d("team", h.getTeam());
                    Log.d("firstappearance", h.getFirstappearance());
                    Log.d("createdby", h.getCreatedby());
                    Log.d("publisher", h.getPublisher());
                    Log.d("imageurl", h.getImageurl());
                    Log.d("bio", h.getBio());

                }

            }

            @Override
            public void onFailure(Call<List<HeroModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

