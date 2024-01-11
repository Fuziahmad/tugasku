package com.fuziahmadfahreza.project_fuziahmadfahreza;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class UpdateTaskActivity extends AppCompatActivity {
    EditText title_input, description_input, deadline_input;
    Button update_button, delete_button;

    String id, title, description, deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Aktifkan tombol kembali
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title_input = findViewById(R.id.title_input2);
        description_input = findViewById(R.id.description_input2);
        deadline_input = findViewById(R.id.deadline_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input
                if (TextUtils.isEmpty(title_input.getText().toString().trim()) ||
                        TextUtils.isEmpty(description_input.getText().toString().trim()) ||
                        TextUtils.isEmpty(deadline_input.getText().toString().trim())) {
                    Toast.makeText(UpdateTaskActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
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
                            // Update data
                            MyDatabaseHelper myDb = new MyDatabaseHelper(UpdateTaskActivity.this);
                            title = title_input.getText().toString().trim();
                            description = description_input.getText().toString().trim();
                            deadline = deadline_input.getText().toString().trim();
                            myDb.updateData(id, title, description, deadline);
                        }else{
                            Toast.makeText(UpdateTaskActivity.this, "Tanggal deadline harus setelah tanggal hari ini", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (ParseException e){
                        e.printStackTrace();
                        Toast.makeText(UpdateTaskActivity.this, "Format tanggal tidak valid: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    finish();
                }
            }
        });


        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("deadline")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            deadline = getIntent().getStringExtra("deadline");

            //Setting Intent Data
            title_input.setText(title);
            description_input.setText(description);
            deadline_input.setText(deadline);
            Log.d("fuziAhmad", title+" "+description+" "+deadline);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }


    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Tugas ?");
        builder.setMessage("Anda Yakin Hapus Tugas " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateTaskActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle tombol kembali di sini
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}