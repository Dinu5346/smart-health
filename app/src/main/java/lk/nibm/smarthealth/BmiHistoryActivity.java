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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BmiHistoryActivity extends AppCompatActivity {

    private DatabaseHelper newDB;
    private ArrayList<String> date;
    private ArrayList<Float> height;
    private ArrayList<Float> weight;
    private ArrayList<String> bmi;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;

    private Button btnHome, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_history);

        initViews();

        newDB = new DatabaseHelper(BmiHistoryActivity.this);

        date = new ArrayList<>();
        height = new ArrayList<Float>();
        weight = new ArrayList<Float>();
        bmi = new ArrayList<String>();

        displayData();

        customAdapter = new CustomAdapter(BmiHistoryActivity.this, date,height,weight,bmi);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BmiHistoryActivity.this));

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BmiHistoryActivity.this, HomeActivity.class);
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
        recyclerView = findViewById(R.id.recviewRecview);
        btnHome = findViewById(R.id.btnHomeBmiHistory);
        btnExit = findViewById(R.id.btnExitBmiHistory);
    }

    void displayData() {
        Cursor cursor = newDB.bmiHistory(Integer.valueOf(MainActivity.id.get(0)));

        if (cursor.getCount() == 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent intent = new Intent(BmiHistoryActivity.this, BodyMassIndexActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No data to display").setPositiveButton("Ok", dialogClickListener).show();
        } else {
            while (cursor.moveToNext()) {
                date.add(cursor.getString(0));
                height.add(cursor.getFloat(1));
                weight.add(cursor.getFloat(2));

                DecimalFormat df = new DecimalFormat("####.##");
                String stringResult = df.format(cursor.getFloat(3));

                bmi.add(stringResult);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bmi_history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.delete_all_bmi_history) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            newDB.deleteBmiHistory(Integer.valueOf(MainActivity.id.get(0)));

                            Intent intent = new Intent(BmiHistoryActivity.this, BodyMassIndexActivity.class);
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
            builder.setMessage("Delete all BMI history?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No",dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }
}