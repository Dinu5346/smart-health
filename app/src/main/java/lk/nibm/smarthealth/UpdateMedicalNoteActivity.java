package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateMedicalNoteActivity extends AppCompatActivity {

    private EditText edttxtTitle, edttxtCategory, edttxtNote;
    private Button btnSave, btnCancel;
    private String id, title, category, note;
    private DatabaseHelper newDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical_note);

        initViews();

        getAndSetIntentData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxtTitle.getText().toString().trim().equals("") == true ||
                        edttxtCategory.getText().toString().trim().equals("") == true ||
                        edttxtNote.getText().toString().trim().equals("") == true) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMedicalNoteActivity.this);
                    builder.setMessage("Please fill all information").setPositiveButton("Ok", dialogClickListener).show();
                } else {
                    newDB = new DatabaseHelper(UpdateMedicalNoteActivity.this);
                    newDB.updateNote(Integer.valueOf(id),
                            edttxtTitle.getText().toString().trim(),
                            edttxtCategory.getText().toString().trim(),
                            edttxtNote.getText().toString().trim());

                    Intent intent = new Intent(UpdateMedicalNoteActivity.this, MedicalNotesActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent intent = new Intent(UpdateMedicalNoteActivity.this, MedicalNotesActivity.class);
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to cancel?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    private void initViews() {
        edttxtTitle = findViewById(R.id.edttxtNoteTitle2);
        edttxtCategory = findViewById(R.id.edttxtNoteCategory2);
        edttxtNote = findViewById(R.id.edttxtNote2);
        btnSave = findViewById(R.id.btnSaveNote2);
        btnCancel = findViewById(R.id.btnCancelNote2);
    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("title") &&
                getIntent().hasExtra("category")
                && getIntent().hasExtra("note")) {

            // getting data from intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            category = getIntent().getStringExtra("category");
            note = getIntent().getStringExtra("note");

            // setting intent data
            edttxtTitle.setText(title);
            edttxtCategory.setText(category);
            edttxtNote.setText(note);

        } else {
            Toast.makeText(this, "Cannot get data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.delete_note) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            newDB = new DatabaseHelper(UpdateMedicalNoteActivity.this);
                            newDB.deleteMedicalNote(Integer.valueOf(id));
                            Intent intent = new Intent(UpdateMedicalNoteActivity.this, MedicalNotesActivity.class);
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete note?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }
}