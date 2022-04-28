package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WaterMinderActivity extends AppCompatActivity {

    TextView txtWaterPercentage;
    EditText edttxtWaterCapacity, edttxtWaterNow, edttxtWaterCup;
    Button btnSaveWater, btnUpdateWater, btnHome, btnExit;
    FloatingActionButton btnAddWater, btnRemoveWater;
    ImageView imgMeterWaterLevel;
    DatabaseHelper newDB = new DatabaseHelper(WaterMinderActivity.this);
    boolean savedToday = false;
    int waterPercentage, waterCapacity, waterCup, waterCurrent;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_minder);

        initViews();

        initValues();

        btnSaveWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(savedToday == true) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                    builder.setMessage("Already saved today! Tap update button to update capacity.").setPositiveButton("Ok", dialogClickListener).show();
                } else if (edttxtWaterCapacity.getText().toString().trim().equals("") == true) {
                    msgEnterCapacity();
                } else if (Integer.valueOf(edttxtWaterCapacity.getText().toString().trim()) < 1000 || Integer.valueOf(edttxtWaterCapacity.getText().toString().trim()) > 4000) {
                    msgCapacityValidation();
                } else {
                    newDB.saveWaterCapacity(Integer.valueOf(MainActivity.id.get(0)), formatter.format(date), Integer.valueOf(edttxtWaterCapacity.getText().toString().trim()));
                    savedToday = true;
                    initValues();
                    edttxtWaterCup.setText("");
                }
            }
        });

        btnUpdateWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedToday == false) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                    builder.setMessage("Nothing to update. Tap save button to save capacity.").setPositiveButton("Ok", dialogClickListener).show();
                } else if (edttxtWaterCapacity.getText().toString().trim().equals("") == true) {
                    msgEnterCapacity();
                } else if (Integer.valueOf(edttxtWaterCapacity.getText().toString().trim()) < 1000 || Integer.valueOf(edttxtWaterCapacity.getText().toString().trim()) > 4000) {
                    msgCapacityValidation();
                } else if (edttxtWaterCapacity.getText().toString().trim().equals(String.valueOf(waterCapacity)) == true){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    break;
                            }
                        }};

                    AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                    builder.setMessage("Already up to date!").setPositiveButton("Ok", dialogClickListener).show();
                } else {
                    newDB.updateWaterCapacity(Integer.valueOf(MainActivity.id.get(0)),
                            formatter.format(date),
                            Integer.valueOf(edttxtWaterCapacity.getText().toString().trim()));
                    initValues();
                }
            }
        });

        btnAddWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxtWaterCup.getText().toString().trim().equals("") == true) {
                    msgEnterCup();
                } else if (edttxtWaterCup.getText().toString().trim().equals("0") == true) {
                    msgEnterCup();
                } else {
                    if (savedToday == true) {
                        waterCup = Integer.valueOf(edttxtWaterCup.getText().toString().trim());

                        if (waterCurrent < waterCapacity) {
                            waterCurrent += waterCup;
                            waterPercentage = (waterCurrent * 100) / waterCapacity;

                            showWaterPercentage(waterPercentage);

                            newDB.addWater(Integer.valueOf(MainActivity.id.get(0)),
                                    formatter.format(date),
                                    waterCup,
                                    waterCurrent,
                                    waterPercentage);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                            builder.setMessage("Water capacity exceeded!").setPositiveButton("Ok", dialogClickListener).show();
                        }
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                        builder.setMessage("Please enter water capacity first").setPositiveButton("Ok", dialogClickListener).show();
                    }

                    initValues();
                }
            }
        });

        btnRemoveWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxtWaterCup.getText().toString().trim().equals("") == true) {
                    msgEnterCup();
                } else if (edttxtWaterCup.getText().toString().trim().equals("0") == true) {
                    msgEnterCup();
                } else {
                    if (savedToday == true) {
                        waterCup = Integer.valueOf(edttxtWaterCup.getText().toString().trim());

                        if (waterCurrent <= waterCapacity + 250) {
                            waterCurrent -= waterCup;
                            waterPercentage = (waterCurrent * 100) / waterCapacity;

                            showWaterPercentage(waterPercentage);

                            newDB.removeWater(Integer.valueOf(MainActivity.id.get(0)),
                                    formatter.format(date),
                                    waterCup,
                                    waterCurrent,
                                    waterPercentage);
                        } else {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                            builder.setMessage("Water capacity exceeded!").setPositiveButton("Ok", dialogClickListener).show();
                        }
                    } else {
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
                        builder.setMessage("Please enter water capacity first").setPositiveButton("Ok", dialogClickListener).show();
                    }

                    initValues();
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaterMinderActivity.this, HomeActivity.class);
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

    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.water_minder_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.show_all_water_history) {
            Intent intent = new Intent(WaterMinderActivity.this, WaterMinderHistoryActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.show_how_to_use_water_minder) {
            waterminderWelcomeMessage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initValues() {
        ArrayList waterInfoArray = new ArrayList();
        ArrayList waterPercentageArray = new ArrayList();
        ArrayList waterCapacityArray = new ArrayList();
        ArrayList waterCupArray = new ArrayList();
        ArrayList waterCurrentArray = new ArrayList();

        Cursor waterinfocursor = newDB.checkWaterInfo(Integer.valueOf(MainActivity.id.get(0)));

        while (waterinfocursor.moveToNext()) {
            waterInfoArray.add(waterinfocursor.getString(0));

            if (Integer.valueOf(String.valueOf(waterInfoArray.get(0))) == 0) {
                waterminderWelcomeMessage();
                newDB.updateWaterInfo(Integer.valueOf(MainActivity.id.get(0)));
            } else {
                Cursor waterpercentagecursor = newDB.checkWaterPercentage(Integer.valueOf(MainActivity.id.get(0)),formatter.format(date));

                if (waterpercentagecursor.getCount() != 0) {
                    while (waterpercentagecursor.moveToNext()) {
                        waterPercentageArray.add(waterpercentagecursor.getString(0));
                        waterCapacityArray.add(waterpercentagecursor.getString(1));
                        waterCupArray.add(waterpercentagecursor.getString(2));
                        waterCurrentArray.add(waterpercentagecursor.getString(3));
                    }

                    waterPercentage = Integer.valueOf(String.valueOf(waterPercentageArray.get(0)));
                    waterCapacity = Integer.valueOf(String.valueOf(waterCapacityArray.get(0)));
                    waterCup = Integer.valueOf(String.valueOf(waterCupArray.get(0)));
                    waterCurrent = Integer.valueOf(String.valueOf(waterCurrentArray.get(0)));

                    showWaterPercentage(waterPercentage);

                    edttxtWaterCapacity.setText(Integer.toString(waterCapacity));
                    edttxtWaterCup.setText(Integer.toString(waterCup));
                    edttxtWaterNow.setText(Integer.toString(waterCurrent));

                    savedToday = true;
                }
            }
        }
    }

    private void msgEnterCapacity() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;
                }
            }};

        AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
        builder.setMessage("Please enter water capacity").setPositiveButton("Ok", dialogClickListener).show();
    }

    private void msgEnterCup() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;
                }
            }};

        AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
        builder.setMessage("Please enter the amount of water in a water cup").setPositiveButton("Ok", dialogClickListener).show();
    }

    private void msgCapacityValidation() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;
                }
            }};

        AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
        builder.setMessage("Please enter a value between 1000 mL and 4000 mL").setPositiveButton("Ok", dialogClickListener).show();
    }

    private void showWaterPercentage(int waterPercentage) {
        if (waterPercentage >= 95) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_10);
        } else if (waterPercentage >= 85) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_9);
        } else if (waterPercentage >= 75) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_8);
        } else if (waterPercentage >= 60) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_7);
        } else if (waterPercentage >= 50) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_6);
        } else if (waterPercentage >= 40) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_5);
        } else if (waterPercentage >= 30) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_4);
        } else if (waterPercentage >= 20) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_3);
        } else if (waterPercentage >= 10) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_2);
        } else if (waterPercentage > 0) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_1);
        } else if (waterPercentage == 0) {
            imgMeterWaterLevel.setImageResource(R.mipmap.water_bottle_0);
        }

        txtWaterPercentage.setText(waterPercentage + "%");
    }

    private void waterminderWelcomeMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;
                }
            }};

        AlertDialog.Builder builder = new AlertDialog.Builder(WaterMinderActivity.this);
        builder.setTitle("How to use Water Minder")
                .setMessage("First, set a daily amount of water to drink and the amount of water in your water cup. " +
                        "Then, every time you drink a cup of water, tap the plus button. " +
                        "Use Water Minder everyday to track your daily water status.")
                .setPositiveButton("Ok", dialogClickListener).show();
    }

    private void initViews() {
        txtWaterPercentage = findViewById(R.id.txtWaterPercentage);
        edttxtWaterCapacity = findViewById(R.id.edttxtWaterCapacity);
        edttxtWaterNow = findViewById(R.id.edttxtWaterNow);
        edttxtWaterCup = findViewById(R.id.edttxtWaterCup);
        btnSaveWater = findViewById(R.id.btnSaveWater);
        btnUpdateWater = findViewById(R.id.btnUpdateWater);
        btnAddWater = findViewById(R.id.btnWaterAdd);
        btnRemoveWater = findViewById(R.id.btnWaterRemove);
        btnHome = findViewById(R.id.btnHomeWater);
        btnExit = findViewById(R.id.btnExitWater);
        imgMeterWaterLevel = findViewById(R.id.imgMeterWaterLevel);
    }
}