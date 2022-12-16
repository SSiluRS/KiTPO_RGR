package com.example.rgr;

import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import model.data.List;
import model.data.UserFactory;
import model.data.builder.DoubleObjectBuilder;
import model.data.builder.PolarVectorObjectBuilder;
import model.data.builder.UserTypeBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public UserFactory userFactory;
    public UserTypeBuilder builder;
    public List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userFactory = new UserFactory();
        builder = new DoubleObjectBuilder();
        list = new List();

        setContentView(R.layout.activity_main);
        RadioButtonInitialization();
        ButtonsInitialization();
    }

    public void RadioButtonInitialization(){

        ListView listView = findViewById(R.id.listView);
        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonPolar:
                        builder = new PolarVectorObjectBuilder();
                        list = new List();

                        break;
                    case R.id.radioButtonDouble:
                        builder = new DoubleObjectBuilder();
                        list = new List();
                        break;
                }

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
                adapter.clear();
            }
        });

    }

    public void ButtonsInitialization(){
        Button insertBtn = findViewById(R.id.insertBtn);
        Button removeBtn = findViewById(R.id.RemoveBtn);
        Button sortBtn = findViewById(R.id.sortBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        Button loadBtn = findViewById(R.id.loadBtn);

        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.size() > 1) {
                    list.remove(position);
                    adapter.clear();
                    for (int i = 0; i < list.size(); i++) {
                        adapter.add((list.get(i)).toString());
                    }
                }
            }
        });

        insertBtn.setOnClickListener((view)->{
            list.add(builder.create());
            adapter.add(list.get(list.size()-1).toString());
        });

        removeBtn.setOnClickListener((view)->{
            if(list.size() > 1) {
                list.remove(list.size()-1);
                adapter.clear();
                for (int i = 0; i < list.size(); i++) {
                    adapter.add((list.get(i)).toString());
                }
            }
        });

        sortBtn.setOnClickListener((view)->{
            list.sort(builder.getComparator());
            adapter.clear();
            for (int i = 0; i < list.size(); i++) {
                adapter.add((list.get(i)).toString());
            }
        });

        saveBtn.setOnClickListener((view)->{
            BufferedWriter bufferedWriter = null;
            try {
                Log.d("MY_TAG", builder.typeName());
                if (builder.typeName().equals("Double")) {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput("double.txt", MODE_PRIVATE))));
                } else {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput("polar.txt", MODE_PRIVATE))));
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            try {
                bufferedWriter.write(builder.typeName() + "\n");
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            BufferedWriter finalBufferedWriter = bufferedWriter;
            list.forEach(el -> {
                try {
                    finalBufferedWriter.write(el.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Toast.makeText(getBaseContext(), "Список успешно сохранен в файл!", Toast.LENGTH_LONG).show();
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadBtn.setOnClickListener((view) -> {
            BufferedReader bufferedReader;
            try {
                Log.d("MY_TAG", builder.typeName());
                if (builder.typeName().equals("Double")) {
                    bufferedReader = new BufferedReader(new InputStreamReader((openFileInput("double.txt"))));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader((openFileInput("polar.txt"))));
                }
            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                return;
            }
            String line;
            try {
                line = bufferedReader.readLine();
                if (line == null) {
                    Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!builder.typeName().equals(line)) {
                    Toast.makeText(getBaseContext(), "Неправильный формат файла!", Toast.LENGTH_LONG).show();
                    return;
                }
                list = new List();

                while ((line = bufferedReader.readLine()) != null) {
                    try {
                        list.add(builder.createFromString(line));
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < list.size(); i++) {
                adapter.add((list.get(i)).toString());
            }
        });

    }

}