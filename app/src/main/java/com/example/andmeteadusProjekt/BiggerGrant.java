package com.example.andmeteadusProjekt;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class BiggerGrant extends Fragment implements View.OnClickListener{

    Context context;
    Button findBiggest;
    TableLayout biggestFunding;
    TextView applicationTitle;
    TextView amount;
    TextView managingOrganisation;
    TextView name;
    TextView projectName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewBiggestGrant = inflater.inflate(R.layout.fragment_nonmain_bigger, container, false);
        findBiggest = viewBiggestGrant.findViewById(R.id.biggestButton);

        biggestFunding = viewBiggestGrant.findViewById(R.id.biggestFunding);

        applicationTitle = viewBiggestGrant.findViewById(R.id.applicationTitle2);
        amount = viewBiggestGrant.findViewById(R.id.amount2);
        managingOrganisation = viewBiggestGrant.findViewById(R.id.managingOrganization2);
        name = viewBiggestGrant.findViewById(R.id.name2);
        projectName = viewBiggestGrant.findViewById(R.id.projectName2);
        context = this.getContext();

        findBiggest.setOnClickListener(this);
        biggestFunding.setVisibility(View.INVISIBLE);

        return viewBiggestGrant;
    }

    @Override
    public void onClick(View view) {
        findBiggest.setEnabled(true);

        try {
            writeFile(MainActivity.data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.biggestButton:

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.txt";
                if (!Python.isStarted()) {
                    Python.start(new AndroidPlatform(context));
                    Python py = Python.getInstance();
                    PyObject pym = py.getModule("proov");
                    PyObject pyf = pym.callAttr("biggestFunding", path);
                    String[] pieces = pyf.toString().split(",");
                    applicationTitle.setText(pieces[0]);
                    amount.setText(pieces[1]);
                    managingOrganisation.setText(pieces[2]);
                    name.setText(pieces[3]);
                    projectName.setText(pieces[4]);
                    biggestFunding.setVisibility(View.VISIBLE);
                    findBiggest.setEnabled(true);
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
