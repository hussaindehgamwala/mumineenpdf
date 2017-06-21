package com.mumineendownloads.mumineenpdf.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.mumineendownloads.mumineenpdf.R;

import java.io.File;

public class PDFActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String value = intent.getStringExtra("title");
        getSupportActionBar().setTitle(value);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        pdfView = (PDFView) findViewById(R.id.pdfView);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Mumineen/"+value+".pdf");
        pdfView.fromFile(file)
                .enableSwipe(true)
                .spacing(25)
                .load();
        pdfView.useBestQuality(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
