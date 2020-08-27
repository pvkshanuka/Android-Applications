package CustomClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wooker.DBClasses.Notification;
import com.app.wooker.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RVNotificationAdapter extends RecyclerView.Adapter<RVNotificationAdapter.DataObjectHolder> {

    private Context context;
    private List<Notification> notifications;
    Notification notification;

    public RVNotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder {

        private TextView tv_notification, tv_norti_title, tv_nor_date;
        public ConstraintLayout notification_content;

        public DataObjectHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_notification = itemView.findViewById(R.id.tv_notifiication_content);
            this.tv_norti_title = itemView.findViewById(R.id.tv_norti_title);
            this.tv_nor_date = itemView.findViewById(R.id.tv_nor_date);
            this.notification_content = itemView.findViewById(R.id.cons_lay_notification_content);
        }
    }

    public void removeItem(int position) {
        notifications.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Notification notification, int position) {
        notifications.add(position, notification);
        notifyItemInserted(position);
    }


    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.notifications, viewGroup, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, int position) {

        notification = notifications.get(position);

        if (notification.getStatus().equals("2")) {
            holder.notification_content.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.tv_notification.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.tv_nor_date.setText(Validations.dateObjToString(notification.getDate(), "yyyy-MM-dd"));
        holder.tv_norti_title.setText(notification.getTitle());
        holder.tv_notification.setText(notification.getMessage());

//        Glide.with(context).load(listaTests.get(position).getImg()).into(holder.img);

//        holder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Position: " +
//                        holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public List<Notification> getNortificationList() {
        return notifications;
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

}
