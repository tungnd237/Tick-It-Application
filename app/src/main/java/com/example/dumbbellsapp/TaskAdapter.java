package com.example.dumbbellsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskAdapter extends BaseAdapter {
    private MainActivity2 context;
    private int layout;
    private List<Task> taskList;

    public TaskAdapter(MainActivity2 context, int layout, List<Task> taskList) {
        this.context = context;
        this.layout = layout;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtName;
        ImageView imgRemove, imgEdit;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtName = (TextView) view.findViewById(R.id.textViewName);
            holder.imgRemove = (ImageView) view.findViewById(R.id.imageViewRemove);
            holder.imgEdit = (ImageView) view.findViewById(R.id.imageViewEdit);
            //anh xa
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        Task task = taskList.get(position);
        holder.txtName.setText(task.getName());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.dialogEdit(task.getName(), task.getId());
            }
        });


        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.tickIt(task.getName(), task.getId());
            }
        });


        return view;
    }
}