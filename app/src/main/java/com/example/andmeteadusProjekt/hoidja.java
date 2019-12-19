package com.example.andmeteadusProjekt;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class hoidja extends AppCompatActivity implements Informer{
    Context context = this;
    Button button;

    public static TextView text;
    public static List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //button = findViewById(R.id.button);
        text = findViewById(R.id.text);

        new fetchData(this).execute();
        button.setText("Getting data");
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                button.setText("Loading data");
                try {
                    writeFile(data.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/data.txt";
                if (! Python.isStarted()) {
                    Python.start(new AndroidPlatform(context));
                    Python py = Python.getInstance();
                    PyObject pym =py.getModule("proov");
                    PyObject pyf = pym.callAttr("biggestFunding", path);
                    text.setText(pyf.toString());
                    button.setText("Ready");
                    button.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onTaskDone(String output) {
        data.add(output);
        button.setText("Ready");
        button.setEnabled(true);
    }

    public void writeFile(String content) throws IOException {
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File file = new File(path, "data.txt");
        FileOutputStream stream = new FileOutputStream(file);
        try {
            String trueContent = content.substring(0, content.length() - 4);
            stream.write(trueContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
    }

}
