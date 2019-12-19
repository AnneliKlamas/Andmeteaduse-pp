package com.example.andmeteadusProjekt;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
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
import java.util.List;

public class SmallerGrant extends Fragment implements View.OnClickListener{
    Context context;
    Button findSmallest;
    TableLayout smallestFunding;
    TextView applicationTitle;
    TextView amount;
    TextView managingOrganisation;
    TextView name;
    TextView projectName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewSmallestGrant = inflater.inflate(R.layout.fragment_nonmain_smaller, container, false);
        findSmallest = viewSmallestGrant.findViewById(R.id.findSmallestButton);
        smallestFunding = viewSmallestGrant.findViewById(R.id.smallestFunding);
        applicationTitle = viewSmallestGrant.findViewById(R.id.applicationTitle);
        amount = viewSmallestGrant.findViewById(R.id.amount);
        managingOrganisation = viewSmallestGrant.findViewById(R.id.managingOrganization);
        name = viewSmallestGrant.findViewById(R.id.name);
        projectName = viewSmallestGrant.findViewById(R.id.projectName);
        context = this.getContext();

        findSmallest.setOnClickListener(this);
        smallestFunding.setVisibility(View.INVISIBLE);

        return viewSmallestGrant;
    }

    @Override
    public void onClick(View view) {
        findSmallest.setEnabled(true);

        try {
            writeFile(MainActivity.data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.findSmallestButton:

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.txt";
                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(context));
                    Python py = Python.getInstance();
                    PyObject pym = py.getModule("proov");
                    PyObject pyf = pym.callAttr("smallestFunding", path);
                    String[] pieces = pyf.toString().split(",");
                    applicationTitle.setText(pieces[0]);
                    amount.setText(pieces[1]);
                    managingOrganisation.setText(pieces[2]);
                    name.setText(pieces[3]);
                    projectName.setText(pieces[4]);
                    smallestFunding.setVisibility(View.VISIBLE);
                    findSmallest.setEnabled(true);
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
