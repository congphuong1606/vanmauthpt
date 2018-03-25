package com.phuongnv.vanmauthpt.vanmauthpt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phuongnv.vanmauthpt.vanmauthpt.R;
import com.phuongnv.vanmauthpt.vanmauthpt.data.Lesson;
import com.phuongnv.vanmauthpt.vanmauthpt.event.OnClickLessonAdapter;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Ominext on 9/22/2017.
 */

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    OnClickLessonAdapter mListener;
    ArrayList<Lesson> lessons;


    public LessonAdapter(ArrayList<Lesson> lessons, OnClickLessonAdapter mListener) {
        this.lessons = lessons;
        this.mListener = mListener;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.tvDesLesson.setText(lesson.getDes());
        holder.tvNameLesson.setText(lesson.getName());
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public void filter(String charText,ArrayList<Lesson>  listSeach) {
        charText = charText.toLowerCase(Locale.getDefault());
        lessons.clear();
        if(charText.length()==0){
            lessons.addAll(listSeach);
        }
        else {
            for(Lesson l : listSeach){
                if(l.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    lessons.add(l);
                }
            }
        }
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_des_lesson)
        TextView tvDesLesson;
        @BindView(R.id.tv_name_lesson)
        TextView tvNameLesson;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(lessons.get(getAdapterPosition()).getPath(),lessons.get(getAdapterPosition()).getLink(),
                            lessons.get(getAdapterPosition()).getName());
                }
            });

        }
    }
}