package com.example.andmeteadusProjekt;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GrantOracle extends Fragment implements View.OnClickListener {
    Context context;
    TextView answer;
    Spinner category;
    EditText sum;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewGrant = inflater.inflate(R.layout.fragment_nonmain_grant, container, false);
        Button Oracle = viewGrant.findViewById(R.id.grantButton);
        answer = viewGrant.findViewById(R.id.grantAnswer);
        category = viewGrant.findViewById(R.id.grantSpinner);
        sum = viewGrant.findViewById(R.id.grantSum);
        context = this.getContext();
        Oracle.setOnClickListener(this);
        answer.setVisibility(View.INVISIBLE);

        return viewGrant;


    }

    @Override
    public void onClick(View v) {

        try {
            writeFile(MainActivity.data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (v.getId()) {
            case R.id.grantButton:
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.txt";
                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(context));
                    Python py = Python.getInstance();
                    PyObject pym = py.getModule("proov");
                    PyObject pyf = pym.callAttr("guessData",
                            String.valueOf(category.getSelectedItem()), sum.getText(), path);
                    answer.setText(pyf.toString());
                    answer.setVisibility(View.VISIBLE);

                }
        }
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
