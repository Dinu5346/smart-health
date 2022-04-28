package lk.nibm.smarthealth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;

public class WaterMinderHistoryActivity extends AppCompatActivity {

    Button btnHome, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_minder_history);

        initViews();

        AnyChartView anyChartView = findViewById(R.id.water_minder_barchart);

        Cartesian cartesian = AnyChart.column();

        ArrayList<DataEntry> data = new ArrayList<>();

        DatabaseHelper newDB = new DatabaseHelper(WaterMinderHistoryActivity.this);
        Cursor cursor = newDB.checkWaterHistory(Integer.valueOf(MainActivity.id.get(0)));

        if (cursor.getCount() == 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent intent = new Intent(WaterMinderHistoryActivity.this, WaterMinderActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No data to display").setPositiveButton("Ok", dialogClickListener).show();
        } else {
            int n = 0;
            while (cursor.moveToNext()) {
                data.add(new ValueDataEntry(cursor.getString(0), cursor.getInt(1)));
                n += 1;

                if (n >= 7) {
                    break;
                }
            }
        }

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Water Minder Activity History");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date");
        cartesian.yAxis(0).title("Water intake");

        anyChartView.setChart(cartesian);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaterMinderHistoryActivity.this, HomeActivity.class);
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
        btnHome = findViewById(R.id.btnHomeWaterMinderHistory);
        btnExit = findViewById(R.id.btnExitWaterMinderHistory);
    }
}