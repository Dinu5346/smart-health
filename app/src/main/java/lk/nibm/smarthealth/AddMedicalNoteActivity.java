package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicalNoteActivity extends AppCompatActivity {

    EditText edttxtTitle, edttxtCategory, edttxtNote;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_note);

        initViews();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxtTitle.getText().toString().trim().equals("") == true ||
                        edttxtCategory.getText().toString().trim().equals("") == true ||
                        edttxtNote.getText().toString().trim().equals("") == true) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMedicalNoteActivity.this);
                    builder.setMessage("Please fill all information").setPositiveButton("Ok", dialogClickListener).show();
                } else {
                    DatabaseHelper newDB = new DatabaseHelper(AddMedicalNoteActivity.this);

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();

                    newDB.saveNote(Integer.valueOf(MainActivity.id.get(0)),
                            edttxtTitle.getText().toString().trim(),
                            edttxtCategory.getText().toString().trim(),
                            edttxtNote.getText().toString().trim(),
                            dateFormatter.format(date),
                            timeFormatter.format(date));

                    Intent intent = new Intent(AddMedicalNoteActivity.this, MedicalNotesActivity.class);
                    startActivity(intent);
                    finish();
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
                                Intent intent = new Intent(AddMedicalNoteActivity.this, MedicalNotesActivity.class);
                                startActivity(intent);
                                finish();
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
        edttxtTitle = findViewById(R.id.edttxtNoteTitle);
        edttxtCategory = findViewById(R.id.edttxtNoteCategory);
        edttxtNote = findViewById(R.id.edttxtNote);
        btnSave = findViewById(R.id.btnSaveNote);
        btnCancel = findViewById(R.id.btnCancelNote);
    }
}