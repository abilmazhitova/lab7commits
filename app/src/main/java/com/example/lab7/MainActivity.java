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

    public static final int REQUEST_CODE_WRITE_PERM = 401;
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = findViewById(R.id.tvData);

        Button btnWriteFile = findViewById(R.id.btnWriteFile);
        btnWriteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile("Hello: " + new Date(System.currentTimeMillis()).toString());
            }
        });

        Button btnReadFile = findViewById(R.id.btnReadFile);
        btnReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = readFile();
                tvData.setText(data);
            }
        });

        requestNeededPermission();
    }

    public void requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_PERM);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "WRITE_EXTERNAL_STORAGE permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "WRITE_EXTERNAL_STORAGE permission NOT granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void writeFile(String data) {
        String file = Environment.getExternalStorageDirectory() + "/test.txt";
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes());
            os.flush();
            Toast.makeText(this, "Data written to file", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readFile() {
        String result = "";
        String file = Environment.getExternalStorageDirectory() + "/test.txt";
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            result = bos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
