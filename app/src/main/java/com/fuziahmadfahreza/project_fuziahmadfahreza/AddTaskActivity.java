package com.fuziahmadfahreza.project_fuziahmadfahreza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    EditText title_input, description_input, deadline_input;
    Button create_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Aktifkan tombol kembali
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_input = findViewById(R.id.title_input);
        description_input = findViewById(R.id.description_input);
        deadline_input = findViewById(R.id.deadline_input);
        create_button = findViewById(R.id.create_button);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title_input.getText().toString().trim()) ||
                        TextUtils.isEmpty(description_input.getText().toString().trim()) ||
                        TextUtils.isEmpty(deadline_input.getText().toString().trim())) {
                    Toast.makeText(AddTaskActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                } else {
                    // tanggal dan waktu saat ini
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    String currentDateTime = dateFormat.format(calendar.getTime());

                    String userDeadline = deadline_input.getText().toString().trim();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    try {
                        Date userDeadlineDate = sdf.parse(userDeadline);
                        Date currentDate = new Date();

                        // Cek apakah tanggal deadline melewati tanggal hari ini
                        if (userDeadlineDate != null && userDeadlineDate.after(currentDate)) {
                            // Tanggal deadline belum melewati tanggal hari ini, simpan data ke database
                            MyDatabaseHelper myDB = new MyDatabaseHelper(AddTaskActivity.this);
                            myDB.addTask(
                                    title_input.getText().toString().trim(),
                                    description_input.getText().toString().trim(),
                                    currentDateTime,
                                    userDeadline
                            );
                        } else {
                            Toast.makeText(AddTaskActivity.this, "Tanggal deadline harus setelah tanggal hari ini", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(AddTaskActivity.this, "Format tanggal tidak valid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    finish();
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle tombol kembali
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        deadline_input.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );
        // Set batasan tanggal maksimum yang dapat dipilih (setelah tanggal hari ini)
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}

