package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnSignIn, btnSignUp;
    private EditText edtTxtEmail,edtTxtPassword;
    private DatabaseHelper newDB;
    private ArrayList<String> email, password;
    public static ArrayList<String> id;
    //public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //context = this;

        initViews();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTxtEmail.getText().toString().trim().equals("") == true
                        || edtTxtPassword.getText().toString().trim().equals("") == true) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Enter email address and password").setPositiveButton("Ok", dialogClickListener).show();
                } else {
                        newDB = new DatabaseHelper(MainActivity.this);
                        id = new ArrayList<>();
                        email = new ArrayList<>();
                        password = new ArrayList<>();
                        Cursor cursor = newDB.signIn(edtTxtEmail.getText().toString().trim());

                        if (cursor.getCount() == 0) {
                            Toast.makeText(MainActivity.this,"Invalid email address",Toast.LENGTH_SHORT).show();
                        } else {
                            while (cursor.moveToNext()) {
                                id.add(cursor.getString(0));
                                email.add(cursor.getString(1));
                                password.add(cursor.getString(2));

                                if (edtTxtEmail.getText().toString().trim().equals(email.get(0)) == true
                                        && edtTxtPassword.getText().toString().trim().equals(password.get(0)) == true) {
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
            }
        });
    }

    private void initViews() {
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtTxtEmail = findViewById(R.id.edttxt_email);
        edtTxtPassword = findViewById(R.id.edttxt_password);
    }
}