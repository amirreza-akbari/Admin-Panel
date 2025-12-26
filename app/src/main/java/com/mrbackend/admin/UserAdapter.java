package com.mrbackend.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> users){
        this.context = context;
        this.userList = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.txtName.setText(user.name + " " + user.surname);
        holder.txtEmail.setText("ایمیل: " + user.email);
        holder.txtScore.setText("امتیاز: " + user.score);
        holder.txtCreatedAt.setText("ثبت شده در: " + user.created_at);

        holder.btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(context, AddEditUserActivity.class);
            i.putExtra("id", user.id);
            i.putExtra("name", user.name);
            i.putExtra("surname", user.surname);
            i.putExtra("email", user.email);
            i.putExtra("score", user.score);
            context.startActivity(i);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (context instanceof HomeActivity) {
                ((HomeActivity) context).deleteUser(user.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtScore, txtCreatedAt;
        Button btnEdit, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtScore = itemView.findViewById(R.id.txtScore);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
