package com.example.retrofitapi;



import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {


    private List<HeroModel> heroList;
    private Context context;
    private static int currentPosition = 0;

    public HeroAdapter(List<HeroModel> heroList, Context context) {
        this.heroList = heroList;
        this.context = context;
    }

    @Override
    public HeroAdapter.HeroViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout_heroes, viewGroup,false);
        return new HeroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HeroAdapter.HeroViewHolder heroViewHolder, final int i) {
        HeroModel heroModel = heroList.get(i);
        heroViewHolder.textViewName.setText(heroModel.getName());
        heroViewHolder.textViewRealName.setText(heroModel.getRealname());
        heroViewHolder.textViewTeam.setText(heroModel.getTeam());
        heroViewHolder.textViewFirstAppearance.setText(heroModel.getFirstappearance());
        heroViewHolder.textViewCreatedBy.setText(heroModel.getCreatedby());
        heroViewHolder.textViewPublisher.setText(heroModel.getPublisher());
        heroViewHolder.textViewBio.setText(heroModel.getBio().trim());


        Glide.with(context).load(heroModel.getImageurl()).into(heroViewHolder.imageView);
        heroViewHolder.linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded

        if (currentPosition == i){
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.anim);

            //toggling visibility
            heroViewHolder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            heroViewHolder.linearLayout.startAnimation(slideDown);
        }

        heroViewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the position of the item to expand it
                currentPosition = i;

                //reloding the list
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    public class HeroViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, textViewRealName, textViewTeam, textViewFirstAppearance,
                textViewCreatedBy, textViewPublisher, textViewBio;

        ImageView imageView;
        LinearLayout linearLayout;

        HeroViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewRealName =  itemView.findViewById(R.id.textViewRealName);
            textViewTeam =  itemView.findViewById(R.id.textViewTeam);
            textViewFirstAppearance = itemView.findViewById(R.id.textViewFirstAppearance);
            textViewCreatedBy = itemView.findViewById(R.id.textViewCreatedBy);
            textViewPublisher = itemView.findViewById(R.id.textViewPublisher);
            textViewBio = itemView.findViewById(R.id.textViewBio);
            imageView = itemView.findViewById(R.id.imageView);

            linearLayout = itemView.findViewById(R.id.linearLayout);

        }
    }
}
