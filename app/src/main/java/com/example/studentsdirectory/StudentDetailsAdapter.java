package com.example.studentsdirectory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsdirectory.pojo.StudentDetails;

import java.util.ArrayList;

public class StudentDetailsAdapter extends RecyclerView.Adapter<StudentDetailsAdapter.StudentDetailsViewHolder> implements Filterable {
    LayoutInflater mInflater;
    Activity mActivity;
    ArrayList<StudentDetails> mStudentDetails;
    ArrayList<StudentDetails> mStudentDetailsFull;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public StudentDetailsAdapter(Context context, Activity activity, ArrayList<StudentDetails> studentDetails) {
        mInflater = LayoutInflater.from(context);
        mActivity = activity;
        mStudentDetails = studentDetails;
        mStudentDetailsFull = new ArrayList<>(studentDetails);
    }

    @NonNull
    @Override
    public StudentDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.student_item_list, parent, false);
        return new StudentDetailsViewHolder(mItemView,mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailsViewHolder holder, int position) {
        holder.studentName.setText(mStudentDetails.get(position).getStudentName());
        holder.studentDOB.setText(mStudentDetails.get(position).getStudentDOB());
        holder.studentDepartment.setText(mStudentDetails.get(position).getStudentDepartment());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStudentDetails.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<StudentDetails> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length()==0) {
                    filteredList.addAll(mStudentDetailsFull);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (StudentDetails row : mStudentDetailsFull) {
                        Log.e("tag",row.getStudentName());
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getStudentName().toLowerCase().contains(filterPattern.toLowerCase()) ||
                                row.getStudentDepartment().toLowerCase().startsWith(filterPattern.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mStudentDetails.clear();
                mStudentDetails.addAll((ArrayList)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }


    public static class StudentDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView studentName;
        TextView studentDOB;
        TextView studentDepartment;
        Activity mActivity;
        public StudentDetailsViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            mActivity = activity;
            studentName = itemView.findViewById(R.id.tvStudentName);
            studentDOB = itemView.findViewById(R.id.tvDOB);
            studentDepartment = itemView.findViewById(R.id.tvDepartment);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {//TODO context menu

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = mActivity.getMenuInflater();
            inflater.inflate(R.menu.student_item_context_menu, menu);
        }
    }
}