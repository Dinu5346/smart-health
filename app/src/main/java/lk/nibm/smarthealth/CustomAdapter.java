package lk.nibm.smarthealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList date, height, weight, bmi;

    CustomAdapter(Context context, ArrayList date, ArrayList height, ArrayList weight, ArrayList bmi) {
        this.context = context;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_bmi,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull CustomAdapter.MyViewHolder holder, int position) {
        holder.txtDate.setText(String.valueOf(date.get(position)));
        holder.txtHeight.setText(String.valueOf(height.get(position)));
        holder.txtWeight.setText(String.valueOf(weight.get(position)));
        holder.txtBmi.setText(String.valueOf(bmi.get(position)));
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtHeight, txtWeight, txtBmi;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDateBmiRow);
            txtHeight = itemView.findViewById(R.id.txtHeightBmiRow1);
            txtWeight = itemView.findViewById(R.id.txtWeightBmiRow1);
            txtBmi = itemView.findViewById(R.id.txtBmiBmiRow);
        }
    }
}
