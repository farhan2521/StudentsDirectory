package com.example.studentsdirectory;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentsdirectory.database.DbAccessObject;
import com.example.studentsdirectory.database.DetailsContract;
import com.example.studentsdirectory.pojo.StudentDetails;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DbAccessObject dao;
    StudentDetailsAdapter studentDetailsAdapter;
    ArrayList<StudentDetails> studentDetails;
    EditText etSearch;
    ListView listview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new DbAccessObject(this);
        dao.openDb();
        listview = findViewById(R.id.lvSearchDetails);
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    listview.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                studentDetailsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                studentDetailsAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addStudent(View view) {
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_student_details, (ViewGroup)null);
        AlertDialog.Builder mbuilder = (new AlertDialog.Builder(this,R.style.alertDialog).setView(dialogView));
        AlertDialog alertDialog = mbuilder.create();

        EditText etName = dialogView.findViewById(R.id.etStudentName);
        EditText etDOB = dialogView.findViewById(R.id.etDOB);
        EditText etDept = dialogView.findViewById(R.id.etDepartment);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Save content in db
                if (!etName.getText().toString().equals("") && !etDOB.getText().toString().equals("") && !etDept.getText().toString().equals("")) {
                    String name = etName.getText().toString();
                    String dob = etDOB.getText().toString();
                    String dept = etDept.getText().toString();
                    //put the data into db
                    dao.createRow(name, dob, dept);
                    populateRecyclerView();
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(MainActivity.this,"All fields are mandatory",Toast.LENGTH_LONG).show();
                }
            }
        });
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void populateRecyclerView(){
        recyclerView = findViewById(R.id.rvStudentDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        studentDetails = dao.getStudentDetails();
        studentDetailsAdapter = new StudentDetailsAdapter(this, this,studentDetails);
        recyclerView.setAdapter(studentDetailsAdapter);
        registerForContextMenu(recyclerView);

        /*Cursor cursor = dao.getAllRows();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.student_item_list,   //layout of each row
                cursor,                                //data
                new String[]{DetailsEntry.COLUMN_NAME_STUDENT,DetailsEntry.COLUMN_NAME_DOB,DetailsEntry.COLUMN_NAME_DEPARTMENT}, // column from which we want to get the data
                new int[]{R.id.tvStudentName,R.id.tvDOB,R.id.tvDepartment} );             //textview in which we want to put the data

        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        int position = ((StudentDetailsAdapter)recyclerView.getAdapter()).getPosition();
        if(item.getItemId()==R.id.edit_menuItem){
            editDetails(position);
        }
        else if(item.getItemId()==R.id.delete_menuItem){
            dao.deleteRow(studentDetails.get(position).getId());
            populateRecyclerView();
            Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }


    private void editDetails(int position) {
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_student_details, (ViewGroup) null);
        AlertDialog.Builder mbuilder = (new AlertDialog.Builder(this, R.style.alertDialog).setView(dialogView));
        AlertDialog alertDialog = mbuilder.create();

        EditText etName = dialogView.findViewById(R.id.etStudentName);
        EditText etDOB = dialogView.findViewById(R.id.etDOB);
        EditText etDept = dialogView.findViewById(R.id.etDepartment);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Save content in db
                if (etName.getText().toString().equals("") && etDOB.getText().toString().equals("") && etDept.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "No new details are entered", Toast.LENGTH_LONG).show();
                }
                else {
                    String name = etName.getText().toString();
                    String dob = etDOB.getText().toString();
                    String dept = etDept.getText().toString();
                    //put the data into db
                    dao.updateRow(name, dob, dept, studentDetails.get(position).getId());
                    populateRecyclerView();
                    alertDialog.dismiss();
                }
            }
        });
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public void searchStudent(View view) {
        etSearch = findViewById(R.id.etSearch);
        if (!etSearch.getText().toString().isEmpty()) {
            Cursor cursor = dao.searchRow(etSearch.getText().toString());
            if(listview == null){
                listview = findViewById(R.id.lvSearchDetails);
                listview.setVisibility(View.VISIBLE);
            } else{
                listview.setVisibility(View.VISIBLE);
            }
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.student_item_list,
                    cursor,
                    new String[]{DetailsContract.DetailsEntry.COLUMN_NAME_STUDENT,
                            DetailsContract.DetailsEntry.COLUMN_NAME_DOB,
                            DetailsContract.DetailsEntry.COLUMN_NAME_DEPARTMENT}, // column from which we want to get the data
                    new int[]{R.id.tvStudentName,R.id.tvDOB,R.id.tvDepartment}
            );
            listview.setAdapter(adapter);
        } else{
            if(listview == null){
                listview = findViewById(R.id.lvSearchDetails);
                listview.setVisibility(View.GONE);
            } else{
                listview.setVisibility(View.GONE);
            }
        }
    }
}