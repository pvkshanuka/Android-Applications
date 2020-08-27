package model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.testing.myapplication.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {
    private TextView daddress;
    private TextView pdate;
    private CardView orlayout;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        daddress = itemView.findViewById(R.id.daddress);
        pdate = itemView.findViewById(R.id.pdate);
        orlayout = itemView.findViewById(R.id.or_layout);
    }

    public void setData(String addressof, String dateof, int position) {
        daddress.setText(addressof);
        pdate.setText(dateof);
        orlayout.setTag(position);
    }

}
