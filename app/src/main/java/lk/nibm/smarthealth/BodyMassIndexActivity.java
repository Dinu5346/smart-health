package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BodyMassIndexActivity extends AppCompatActivity {

    EditText edttxtHeight, edttxtWeight, edttxtResult;
    TextView txtResult;
    Button btnCalculate, btnUpdate, btnHistory, btnHome, btnExit;
    ImageView resultMeter;
    DatabaseHelper newDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_mass_index);

        initViews();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edttxtHeight.getText().toString().trim().equals("") == true ||
                        edttxtWeight.getText().toString().trim().equals("") == true) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                    builder.setMessage("Please fill all information").setPositiveButton("Ok", dialogClickListener).show();
                } else if (Float.valueOf(String.valueOf(edttxtHeight.getText())) < 60 ||
                        Float.valueOf(String.valueOf(edttxtHeight.getText())) > 260) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                    builder.setMessage("Please enter a height between 60 cm and 260 cm").setPositiveButton("Ok", dialogClickListener).show();
                } else if (Float.valueOf(String.valueOf(edttxtWeight.getText())) < 30 ||
                        Float.valueOf(String.valueOf(edttxtWeight.getText())) > 260) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                    builder.setMessage("Please enter a weight between 30 kg and 260 kg").setPositiveButton("Ok", dialogClickListener).show();
                } else {

                    newDB = new DatabaseHelper(BodyMassIndexActivity.this);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();

                    Cursor cursor = newDB.checkBmiToday(formatter.format(date), Integer.valueOf(MainActivity.id.get(0)));

                    if (cursor.getCount() == 0) {
                        float height, weight, result;
                        String stringResult;

                        height = Float.valueOf(edttxtHeight.getText().toString().trim());
                        weight = Float.valueOf(edttxtWeight.getText().toString().trim());

                        result = weight / ((height / 100) * (height / 100));

                        DecimalFormat df = new DecimalFormat("####.##");
                        stringResult = df.format(result);

                        edttxtResult.setText(stringResult);

                        if (result > 34.99) {
                            txtResult.setText("Extremely Obese!");
                            txtResult.setTextColor(getResources().getColor(R.color.red));
                            resultMeter.setImageResource(R.mipmap.meter4);
                        } else if (result > 29.99) {
                            txtResult.setText("Obese!");
                            txtResult.setTextColor(getResources().getColor(R.color.red));
                            resultMeter.setImageResource(R.mipmap.meter4);
                        } else if (result > 24.99) {
                            txtResult.setText("Overweight!");
                            txtResult.setTextColor(getResources().getColor(R.color.orange));
                            resultMeter.setImageResource(R.mipmap.meter3);
                        } else if (result > 18.49) {
                            txtResult.setText("Healthy!");
                            txtResult.setTextColor(getResources().getColor(R.color.primaryLight));
                            resultMeter.setImageResource(R.mipmap.meter2);
                        } else {
                            txtResult.setText("Underweight!");
                            txtResult.setTextColor(getResources().getColor(R.color.yellow));
                            resultMeter.setImageResource(R.mipmap.meter1);
                        }

                        newDB.saveBmi(Integer.valueOf(MainActivity.id.get(0)), formatter.format(date), height, weight, result);
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        break;
                                }
                            }};

                        AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                        builder.setMessage("Already saved today! Tap update button to update").setPositiveButton("Ok", dialogClickListener).show();
                    }
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxtHeight.getText().toString().trim().equals("") == true ||
                        edttxtWeight.getText().toString().trim().equals("") == true) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                    builder.setMessage("Please fill all information").setPositiveButton("Ok", dialogClickListener).show();
                } else if (Float.valueOf(String.valueOf(edttxtHeight.getText())) < 60 ||
                        Float.valueOf(String.valueOf(edttxtHeight.getText())) > 260) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                    builder.setMessage("Please enter a height between 60 cm and 260 cm").setPositiveButton("Ok", dialogClickListener).show();
                } else if (Float.valueOf(String.valueOf(edttxtWeight.getText())) < 30 ||
                        Float.valueOf(String.valueOf(edttxtWeight.getText())) > 260) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                    builder.setMessage("Please enter a weight between 30 kg and 260 kg").setPositiveButton("Ok", dialogClickListener).show();
                } else {

                    newDB = new DatabaseHelper(BodyMassIndexActivity.this);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();

                    ArrayList dateArray = new ArrayList<>();
                    ArrayList heightArray = new ArrayList();
                    ArrayList weightArray = new ArrayList();
                    Cursor cursor = newDB.checkBmiToday(formatter.format(date), Integer.valueOf(MainActivity.id.get(0)));

                    if (cursor.getCount() == 0) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        break;
                                }
                            }};

                        AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                        builder.setMessage("Nothing to update").setPositiveButton("Ok", dialogClickListener).show();
                    } else {

                        while (cursor.moveToNext()) {
                            dateArray.add(cursor.getString(0));
                            heightArray.add(cursor.getString(1));
                            weightArray.add(cursor.getString(2));

                            if (edttxtHeight.getText().toString().trim().equals(heightArray.get(0)) == true &&
                                edttxtWeight.getText().toString().trim().equals(weightArray.get(0)) == true) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                break;
                                        }
                                    }};

                                AlertDialog.Builder builder = new AlertDialog.Builder(BodyMassIndexActivity.this);
                                builder.setMessage("Already up to date").setPositiveButton("Ok", dialogClickListener).show();
                            } else {
                                float height, weight, result;
                                String stringResult;

                                height = Float.valueOf(edttxtHeight.getText().toString().trim());
                                weight = Float.valueOf(edttxtWeight.getText().toString().trim());

                                result = weight / ((height / 100) * (height / 100));

                                DecimalFormat df = new DecimalFormat("####.##");
                                stringResult = df.format(result);

                                edttxtResult.setText(stringResult);

                                if (result > 34.99) {
                                    txtResult.setText("Extremely Obese!");
                                    txtResult.setTextColor(getResources().getColor(R.color.red));
                                    resultMeter.setImageResource(R.mipmap.meter4);
                                } else if (result > 29.99) {
                                    txtResult.setText("Obese!");
                                    txtResult.setTextColor(getResources().getColor(R.color.red));
                                    resultMeter.setImageResource(R.mipmap.meter4);
                                } else if (result > 24.99) {
                                    txtResult.setText("Overweight!");
                                    txtResult.setTextColor(getResources().getColor(R.color.orange));
                                    resultMeter.setImageResource(R.mipmap.meter3);
                                } else if (result > 18.49) {
                                    txtResult.setText("Healthy!");
                                    txtResult.setTextColor(getResources().getColor(R.color.primaryLight));
                                    resultMeter.setImageResource(R.mipmap.meter2);
                                } else {
                                    txtResult.setText("Underweight!");
                                    txtResult.setTextColor(getResources().getColor(R.color.yellow));
                                    resultMeter.setImageResource(R.mipmap.meter1);
                                }

                                newDB.updateBmi(Integer.valueOf(MainActivity.id.get(0)), formatter.format(date), height, weight, result);
                            }
                        }
                    }
                }
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyMassIndexActivity.this, BmiHistoryActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BodyMassIndexActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
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
    }

    private void initViews() {
        edttxtHeight = findViewById(R.id.edttxtHeight);
        edttxtWeight = findViewById(R.id.edttxtWeight);
        edttxtResult = findViewById(R.id.edttxtBmi);
        txtResult = findViewById(R.id.txtBmiResult);
        btnCalculate = findViewById(R.id.btnCalculateBmi);
        btnUpdate = findViewById(R.id.btnUpdateBmi);
        btnHistory = findViewById(R.id.btnHistoryBmi);
        btnHome = findViewById(R.id.btnHomeBmi);
        btnExit = findViewById(R.id.btnExitBmi);
        resultMeter = findViewById(R.id.imgMeterBmi);
    }
}