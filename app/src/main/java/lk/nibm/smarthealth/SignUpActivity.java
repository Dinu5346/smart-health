package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUpSignUp, btnCancelSignUp;
    private EditText edtTxtFirstName, edtTxtLastName, edtTxtEmail, edtTxtPassword, edtTxtRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();

        btnCancelSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
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

        btnSignUpSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTxtFirstName.getText().toString().trim().equals("") == true ||
                    edtTxtLastName.getText().toString().trim().equals("") == true ||
                    edtTxtEmail.getText().toString().trim().equals("") == true ||
                    edtTxtPassword.getText().toString().trim().equals("") == true ||
                    edtTxtRepeatPassword.getText().toString().trim().equals("") == true) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Please fill all information").setPositiveButton("Ok", dialogClickListener).show();
                } else if (emailValidator(edtTxtEmail.getText().toString().trim()) == false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Please enter valid email address").setPositiveButton("Ok", dialogClickListener).show();
                } else if (edtTxtPassword.getText().toString().trim().equals(edtTxtRepeatPassword.getText().toString().trim()) == false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage("Repeat password does not match").setPositiveButton("Ok", dialogClickListener).show();
                } else {
                    DatabaseHelper newDB = new DatabaseHelper(SignUpActivity.this);

                    ArrayList emailArray = new ArrayList();

                    Cursor cursor = newDB.checkExistingEmail(edtTxtEmail.getText().toString().trim());

                    if (cursor.getCount() == 0) {
                        newDB.signUp(edtTxtFirstName.getText().toString().trim(),
                                edtTxtLastName.getText().toString().trim(),
                                edtTxtEmail.getText().toString().trim(),
                                edtTxtPassword.getText().toString().trim());

                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        while (cursor.moveToNext()) {
                            emailArray.add(cursor.getString(0));

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked
                                            edtTxtEmail.setText("");
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage(emailArray.get(0).toString() + " is already used. Enter another email address").setPositiveButton("Ok", dialogClickListener).show();
                        }
                    }
                }
            }
        });
    }

    private void initViews() {
        btnSignUpSignUp = findViewById(R.id.btnSignUpSignUp);
        btnCancelSignUp = findViewById(R.id.btnCancelSignUp);
        edtTxtFirstName = findViewById(R.id.edttxtFirstName);
        edtTxtLastName = findViewById(R.id.edttxtLastName);
        edtTxtEmail = findViewById(R.id.edttxtEmailSignUp);
        edtTxtPassword = findViewById(R.id.edttxtPasswordSignUp);
        edtTxtRepeatPassword = findViewById(R.id.edttxtRepeatPassword);
    }

    private boolean emailValidator(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}