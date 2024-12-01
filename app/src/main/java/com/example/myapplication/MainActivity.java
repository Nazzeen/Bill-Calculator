package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    EditText NumberKwH;
    Button CalculateBtn;
    TextView textView1, textView2;
    Spinner rebateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NumberKwH = findViewById(R.id.NumberKwH);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        CalculateBtn = findViewById(R.id.CalculateBtn);
        CalculateBtn.setOnClickListener(this);

        rebateSpinner = findViewById(R.id.RebateSelection);


        Toolbar myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        // Spinner / droplist setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rebate_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rebateSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // this section is responsible for clicking to other section eg; about, settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        } else if (selected == R.id.menuSettings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == CalculateBtn) {
            String number2 = NumberKwH.getText().toString().trim();

            if (number2.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double num2 = Double.parseDouble(number2);
                double charge = 0;

                // Calculate charge based on usage
                if (num2 <= 200) {
                    charge = 0.218 * num2;
                } else if (num2 > 200 && num2 <= 300) {
                    charge = (0.218) * 200 + (0.334) * (num2 - 200);
                } else if (num2 > 300 && num2 <= 600) {
                    charge = (0.218) * 200 + (0.334) * 100 + (0.516) * (num2 - 300);
                } else {
                    charge = (0.218) * 200 + (0.334) * 100 + (0.516) * 300 + (0.546) * (num2 - 600);
                }

                // Get rebate from spinner
                String selectedRebate = rebateSpinner.getSelectedItem().toString().trim();
                if (!selectedRebate.isEmpty()) {
                    selectedRebate = selectedRebate.replace("%", "").trim();
                    int rebatePercentage = Integer.parseInt(selectedRebate);

                    // Calculate discount and total
                    double discount = charge * rebatePercentage / 100;
                    charge -= discount;
                }

                // Display result
                textView2.setText(String.format("Total: RM %.2f", charge));

            } catch (NumberFormatException nfe) {
                Toast.makeText(getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
