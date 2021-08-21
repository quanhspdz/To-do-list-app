package com.example.todo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> arrayTask;
    Button btnAddTask;
    EditText edtAddTask;
    ArrayAdapter adapter;
    ListView lvTask;
    PopupMenu popupMenu;
    Dialog editDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayTask = SaveTask.getTaskArray(this);

        btnAddTask = findViewById(R.id.btnAddTask);
        edtAddTask = findViewById(R.id.edtAddTask);
        lvTask = findViewById(R.id.lvTask);

        adapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayTask);
        lvTask.setAdapter(adapter);

        //to show the menu when user click to item
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupMenu = new PopupMenu(MainActivity.this, view);
                getMenuInflater().inflate(R.menu.menu_option, popupMenu.getMenu());
                    showPopupMenu(popupMenu, i);
            }
        });

        //to add the new task
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = edtAddTask.getText().toString();
                if (task.length() > 0) {
                    addNewTask(task);
                    edtAddTask.setText("");
                }
                else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập nhiệm vụ!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //show the menu options when user click to item
    private void showPopupMenu(PopupMenu popupMenu, int position) {
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete: {
                        removeTask(position);
                        break;
                    }
                    case R.id.menu_edit: {
                        dialogEdit(position);
                        break;
                    }
                    case R.id.menu_done: {
                        doneTask(position);
                        break;
                    }
                }
                return false;
            }
        });
    }
    //show a dialog to edit task
    private void dialogEdit(int position) {
        editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.dialog_edit);
        editDialog.show();

        EditText edtEdit = editDialog.findViewById(R.id.edtEditTask);
        Button btnEdit = editDialog.findViewById(R.id.btnEdit);

        String task = arrayTask.get(position);
        String smallTask = splitTask(task);
        edtEdit.setText(smallTask);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = edtEdit.getText().toString();
                editTask(position, task);
            }
        });
    }
    //function split string
    private String splitTask(String task) {
        String[] smallTask = task.split("(?<=:)", 2);

        if (smallTask.length > 1) return smallTask[1];
        else return null;
    }
    //function add a new task
    private void addNewTask(String newTask) {
        int i;
        arrayTask.add("");
        for (i = arrayTask.size() -  1; i > 0; i--)
            arrayTask.set(i, arrayTask.get(i-1));
        //add new task to the position 0
        arrayTask.set(0, "#" + 1 + ": " + newTask);

        String[] task;
        //reset the number of each task
        for (i = 0; i < arrayTask.size(); i++) {
            task = arrayTask.get(i).split("(?<=:)", 2);
            arrayTask.set(i, "#" + (i + 1) + ":" + task[1]);
        }

        adapter.notifyDataSetChanged();
        SaveTask.saveTaskArray(MainActivity.this, arrayTask);
    }
    //function delete the task
    private void removeTask(int position) {
        arrayTask.remove(position);
        int i;
        String[] task;

        //reset the number of each task
        for (i = 0; i < arrayTask.size(); i++) {
            task = arrayTask.get(i).split("(?<=:)", 2);
            arrayTask.set(i, "#" + (i + 1) + ":" + task[1]);
        }

        adapter.notifyDataSetChanged();
        SaveTask.saveTaskArray(MainActivity.this, arrayTask);
    }
    //function edit task
    private void editTask(int position, String newTask) {
        if (newTask.length() > 0) {
            arrayTask.set(position, "#" + (position + 1) + ": " + newTask);

            int i;
            String[] task;
            //reset the number of each task
            for (i = 0; i < arrayTask.size(); i++) {
                task = arrayTask.get(i).split("(?<=:)", 2);
                arrayTask.set(i, "#" + (i + 1) + ":" + task[1]);
            }

            adapter.notifyDataSetChanged();
            SaveTask.saveTaskArray(MainActivity.this, arrayTask);
            editDialog.dismiss();
        }
        else {
            Toast.makeText(MainActivity.this, "Vui lòng nhập nhiệm vụ!", Toast.LENGTH_SHORT).show();
        }
    }
    private void doneTask(int position) {
        String taskDone = arrayTask.get(position);
        taskDone = splitTask(taskDone);
        removeTask(position);

        arrayTask.add(arrayTask.size(), "#" + (arrayTask.size() + 1) + ": " + " (ĐÃ XONG) " + taskDone);
        adapter.notifyDataSetChanged();
        SaveTask.saveTaskArray(MainActivity.this, arrayTask);
    }

}