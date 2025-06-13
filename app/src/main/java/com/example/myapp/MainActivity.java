package com.example.projecta2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerSem, spinnerBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDatePicker, btnSubmit;
        EditText etPeriod, etTopics;
        TextView tvSelectedDate;

        btnDatePicker = findViewById(R.id.button);
        spinnerSem = findViewById(R.id.spinner2);
        spinnerBatch = findViewById(R.id.spinner3);
        tvSelectedDate = findViewById(R.id.textView8);
        etPeriod = findViewById(R.id.editTextText);
        etTopics = findViewById(R.id.editTextText2);
        btnSubmit = findViewById(R.id.button2);

        btnDatePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        tvSelectedDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        String[] semesters = {"Sem V", "Sem VI", "Sem VII", "Sem VIII"};
        ArrayAdapter<String> semAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesters);
        spinnerSem.setAdapter(semAdapter);

        // Batch Spinner
        String[] batches = {"Batch A1", "Batch A2", "Batch A3"};
        ArrayAdapter<String> batchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, batches);
        spinnerBatch.setAdapter(batchAdapter);

        btnSubmit.setOnClickListener(v -> {
            String date = tvSelectedDate.getText().toString();
            String sem = spinnerSem.getSelectedItem().toString();
            String batch = spinnerBatch.getSelectedItem().toString();
            String period = etPeriod.getText().toString().trim();
            String topics = etTopics.getText().toString().trim();

            if (date.isEmpty() || period.isEmpty() || topics.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (countTopics(topics) > 2) {
                Toast.makeText(MainActivity.this, "Error: Only 2 topics can be covered.", Toast.LENGTH_SHORT).show();
            } else {
                saveDataToFile(date, sem, batch, period, topics);
            }
        });
    }

    private int countTopics(String topics) {
        // Split the topics by comma to count them
        String[] topicArray = topics.split(",");
        return topicArray.length;
    }

    private void saveDataToFile(String date, String sem, String batch, String period, String topics) {
        String fileName = "LabFormData.txt";
        String data = "Date: " + date + "\n"
                + "Semester: " + sem + "\n"
                + "Batch: " + batch + "\n"
                + "Period: " + period + "\n"
                + "Topics Covered: " + topics + "\n\n";

        try (FileOutputStream fos = openFileOutput(fileName, MODE_APPEND)) {
            fos.write(data.getBytes());
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving data!", Toast.LENGTH_SHORT).show();
        }
    }

    private String readDataFromFile() {
        String fileName = "LabFormData.txt";
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fis = openFileInput(fileName)) {
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
