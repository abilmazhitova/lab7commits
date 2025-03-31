package com.example.lab7;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_PERM = 401;
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = findViewById(R.id.tvData);

        Button btnWriteFile = findViewById(R.id.btnWriteFile);
        btnWriteFile.setOnClickListener(view -> writeFile("Hello: " + new Date().toString()));

        Button btnReadFile = findViewById(R.id.btnReadFile);
        btnReadFile.setOnClickListener(view -> tvData.setText(readFile()));

        requestNeededPermission();
    }

    private void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_PERM);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission NOT granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void writeFile(String data) {
        String file = Environment.getExternalStorageDirectory() + "/test.txt";
        try (FileOutputStream os = new FileOutputStream(file)) {
            os.write(data.getBytes());
            os.flush();
            Toast.makeText(this, "Data written to file", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFile() {
        String result = "";
        String file = Environment.getExternalStorageDirectory() + "/test.txt";
        try (FileInputStream is = new FileInputStream(file)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            result = bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
