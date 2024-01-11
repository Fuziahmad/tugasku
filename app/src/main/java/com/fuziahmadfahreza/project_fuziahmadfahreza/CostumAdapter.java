package com.fuziahmadfahreza.project_fuziahmadfahreza;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CostumAdapter extends RecyclerView.Adapter<CostumAdapter.MyViewHolder>{

    private Context context;
    private Activity activity;
    private ArrayList task_id, task_title, task_description, task_created, task_deadline;

    public CostumAdapter(Activity activity, Context context, ArrayList task_id, ArrayList task_title, ArrayList task_description,
                       ArrayList task_created, ArrayList task_deadline){
        this.activity = activity;
        this.context = context;
        this.task_id = task_id;
        this.task_title = task_title;
        this.task_description = task_description;
        this.task_created = task_created;
        this.task_deadline = task_deadline;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String createdText = "Dibuat " + String.valueOf(task_created.get(position));

        holder.task_id_txt.setText(String.valueOf(task_id.get(position)));
        holder.task_title_txt.setText(String.valueOf(task_title.get(position)));
        holder.task_description_txt.setText(String.valueOf(task_description.get(position)));
        holder.task_created_txt.setText(createdText);
        holder.task_deadline_txt.setText(String.valueOf(task_deadline.get(position)));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition(); // Mengambil posisi yang diteruskan ke holder
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, UpdateTaskActivity.class);
                    intent.putExtra("id", String.valueOf(task_id.get(clickedPosition)));
                    intent.putExtra("title", String.valueOf(task_title.get(clickedPosition)));
                    intent.putExtra("description", String.valueOf(task_description.get(clickedPosition)));
                    intent.putExtra("created", String.valueOf(task_created.get(clickedPosition)));
                    intent.putExtra("deadline", String.valueOf(task_deadline.get(clickedPosition)));
                    activity.startActivityForResult(intent, 1);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return task_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView task_id_txt, task_title_txt, task_description_txt, task_created_txt, task_deadline_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_id_txt = itemView.findViewById(R.id.task_id_txt);
            task_title_txt = itemView.findViewById(R.id.task_title_txt);
            task_description_txt = itemView.findViewById(R.id.task_description_txt);
            task_created_txt = itemView.findViewById(R.id.task_created_txt);
            task_deadline_txt = itemView.findViewById(R.id.task_deadline_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
