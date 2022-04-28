package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MedicalNotesActivity extends AppCompatActivity {

    private Button btnHome, btnExit;
    private FloatingActionButton btnAddNote;
    private RecyclerView notesRecyclerView;
    private DatabaseHelper newDB;
    private ArrayList id, title, category, note, date, time;
    private CustomAdapter2 customadapter2;
    private TextView txtNoNotes;
    private ImageView imgNoNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_notes);

        initViews();

        newDB = new DatabaseHelper(MedicalNotesActivity.this);

        id = new ArrayList<>();
        title = new ArrayList();
        category = new ArrayList();
        note = new ArrayList();
        date = new ArrayList();
        time = new ArrayList();

        displayData();

        customadapter2 = new CustomAdapter2(MedicalNotesActivity.this, id, title, category, note, date, time);
        notesRecyclerView.setAdapter(customadapter2);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(MedicalNotesActivity.this));

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalNotesActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                finishAffinity();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to exit?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalNotesActivity.this, AddMedicalNoteActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        btnHome = findViewById(R.id.btnHomeMedicalNotes);
        btnExit = findViewById(R.id.btnExitMedicalNotes);
        btnAddNote = findViewById(R.id.btnAddMedicalNote);
        notesRecyclerView = findViewById(R.id.recyclerViewMedicalNotes);
        txtNoNotes = findViewById(R.id.txtNoNotes);
        imgNoNotes = findViewById(R.id.imageViewNoNotes);
    }

    private void displayData () {
        Cursor cursor = newDB.displayNotes(Integer.valueOf(MainActivity.id.get(0)));

        if (cursor.getCount() == 0) {
            imgNoNotes.setVisibility(View.VISIBLE);
            txtNoNotes.setVisibility(View.VISIBLE);
        } else {
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                category.add(cursor.getString(2));
                note.add(cursor.getString(3));
                date.add(cursor.getString(4));
                time.add(cursor.getString(5));
            }
            imgNoNotes.setVisibility(View.GONE);
            txtNoNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            newDB.deleteAllMedicalNotes(Integer.valueOf(MainActivity.id.get(0)));
                            Intent intent = new Intent(MedicalNotesActivity.this, MedicalNotesActivity.class);
                            startActivity(intent);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete all notes?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }
}