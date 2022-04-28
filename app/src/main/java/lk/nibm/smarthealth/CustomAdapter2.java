package lk.nibm.smarthealth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.myViewHolder> {

    private Context context;
    private ArrayList id, title, category, note, date, time;
    private int position;

    CustomAdapter2(Context context,
                   ArrayList id,
                   ArrayList title,
                   ArrayList category,
                   ArrayList note,
                   ArrayList date,
                   ArrayList time) {

        this.context = context;
        this.id = id;
        this.title = title;
        this.category = category;
        this.note = note;
        this.date = date;
        this.time = time;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_notes, parent, false);
        return new CustomAdapter2.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomAdapter2.myViewHolder holder, int position) {
        this.position = position;

        holder.txtDate.setText(String.valueOf(date.get(position)));
        holder.txtTime.setText(String.valueOf(time.get(position)));
        holder.txtTitle.setText(String.valueOf(title.get(position)));
        holder.txtCategory.setText(String.valueOf(category.get(position)));
        holder.txtNote.setText(String.valueOf(note.get(position)));
        holder.noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateMedicalNoteActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("title", String.valueOf(title.get(position)));
                intent.putExtra("category", String.valueOf(category.get(position)));
                intent.putExtra("note", String.valueOf(note.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime, txtTitle, txtCategory, txtNote;
        ConstraintLayout noteLayout;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDateNote);
            txtTime = itemView.findViewById(R.id.txtTimeNote);
            txtTitle = itemView.findViewById(R.id.txtTitle2Note);
            txtCategory = itemView.findViewById(R.id.txtCategory2Note);
            txtNote = itemView.findViewById(R.id.txtNoteNote);
            noteLayout = itemView.findViewById(R.id.noteLayout);
        }
    }
}
