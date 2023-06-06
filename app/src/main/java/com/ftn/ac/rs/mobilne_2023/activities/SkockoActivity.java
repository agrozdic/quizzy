package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SkockoActivity extends AppCompatActivity {


    private Button field1x1;
    private Button field1x2;
    private Button field1x3;
    private Button field1x4;
    private ImageButton hit1x1;
    private ImageButton hit1x2;
    private ImageButton hit1x3;
    private ImageButton hit1x4;
    private Button field2x1;
    private Button field2x2;
    private Button field2x3;
    private Button field2x4;
    private ImageButton hit2x1;
    private ImageButton hit2x2;
    private ImageButton hit2x3;
    private ImageButton hit2x4;
    private Button field3x1;
    private Button field3x2;
    private Button field3x3;
    private Button field3x4;
    private ImageButton hit3x1;
    private ImageButton hit3x2;
    private ImageButton hit3x3;
    private ImageButton hit3x4;
    private Button field4x1;
    private Button field4x2;
    private Button field4x3;
    private Button field4x4;
    private ImageButton hit4x1;
    private ImageButton hit4x2;
    private ImageButton hit4x3;
    private ImageButton hit4x4;
    private Button field5x1;
    private Button field5x2;
    private Button field5x3;
    private Button field5x4;
    private ImageButton hit5x1;
    private ImageButton hit5x2;
    private ImageButton hit5x3;
    private ImageButton hit5x4;
    private Button field6x1;
    private Button field6x2;
    private Button field6x3;
    private Button field6x4;
    private ImageButton hit6x1;
    private ImageButton hit6x2;
    private ImageButton hit6x3;
    private ImageButton hit6x4;
    private Button field8x1;
    private Button field8x2;
    private Button field8x3;
    private Button field8x4;
    private ImageButton iconSkocko;
    private ImageButton iconTref;
    private ImageButton iconPik;
    private ImageButton iconHerc;
    private ImageButton iconKaro;
    private ImageButton iconZvezda;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_skocko);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), SkockoActivity.this,
                false, R.id.headSkocko);

//        FloatingActionButton fab = findViewById(R.id.fab4);
//        fab.setOnClickListener(view -> fab4());

        initializeView();
    }

    private void initializeView() {

        field1x1 = (Button) findViewById(R.id.field1x1);
        field1x2 = (Button) findViewById(R.id.field1x2);
        field1x3 = (Button) findViewById(R.id.field1x3);
        field1x4 = (Button) findViewById(R.id.field1x4);
        hit1x1 = (ImageButton) findViewById(R.id.hit1x1);
        hit1x2 = (ImageButton) findViewById(R.id.hit1x2);
        hit1x3 = (ImageButton) findViewById(R.id.hit1x3);
        hit1x4 = (ImageButton) findViewById(R.id.hit1x4);
        field2x1 = (Button) findViewById(R.id.field2x1);
        field2x2 = (Button) findViewById(R.id.field2x2);
        field2x3 = (Button) findViewById(R.id.field2x3);
        field2x4 = (Button) findViewById(R.id.field2x4);
        hit2x1 = (ImageButton) findViewById(R.id.hit2x1);
        hit2x2 = (ImageButton) findViewById(R.id.hit2x2);
        hit2x3 = (ImageButton) findViewById(R.id.hit2x3);
        hit2x4 = (ImageButton) findViewById(R.id.hit2x4);
        field3x1 = (Button) findViewById(R.id.field3x1);
        field3x2 = (Button) findViewById(R.id.field3x2);
        field3x3 = (Button) findViewById(R.id.field3x3);
        field3x4 = (Button) findViewById(R.id.field3x4);
        hit3x1 = (ImageButton) findViewById(R.id.hit3x1);
        hit3x2 = (ImageButton) findViewById(R.id.hit3x2);
        hit3x3 = (ImageButton) findViewById(R.id.hit3x3);
        hit3x4 = (ImageButton) findViewById(R.id.hit3x4);
        field4x1 = (Button) findViewById(R.id.field4x1);
        field4x2 = (Button) findViewById(R.id.field4x2);
        field4x3 = (Button) findViewById(R.id.field4x3);
        field4x4 = (Button) findViewById(R.id.field4x4);
        hit4x1 = (ImageButton) findViewById(R.id.hit4x1);
        hit4x2 = (ImageButton) findViewById(R.id.hit4x2);
        hit4x3 = (ImageButton) findViewById(R.id.hit4x3);
        hit4x4 = (ImageButton) findViewById(R.id.hit4x4);
        field5x1 = (Button) findViewById(R.id.field5x1);
        field5x2 = (Button) findViewById(R.id.field5x2);
        field5x3 = (Button) findViewById(R.id.field5x3);
        field5x4 = (Button) findViewById(R.id.field5x4);
        hit5x1 = (ImageButton) findViewById(R.id.hit5x1);
        hit5x2 = (ImageButton) findViewById(R.id.hit5x2);
        hit5x3 = (ImageButton) findViewById(R.id.hit5x3);
        hit5x4 = (ImageButton) findViewById(R.id.hit5x4);
        field6x1 = (Button) findViewById(R.id.field6x1);
        field6x2 = (Button) findViewById(R.id.field6x2);
        field6x3 = (Button) findViewById(R.id.field6x3);
        field6x4 = (Button) findViewById(R.id.field6x4);
        hit6x1 = (ImageButton) findViewById(R.id.hit6x1);
        hit6x2 = (ImageButton) findViewById(R.id.hit6x2);
        hit6x3 = (ImageButton) findViewById(R.id.hit6x3);
        hit6x4 = (ImageButton) findViewById(R.id.hit6x4);
        field8x1 = (Button) findViewById(R.id.field8x1);
        field8x2 = (Button) findViewById(R.id.field8x2);
        field8x3 = (Button) findViewById(R.id.field8x3);
        field8x4 = (Button) findViewById(R.id.field8x4);
        iconSkocko = (ImageButton) findViewById(R.id.iconSkocko);
        iconTref = (ImageButton) findViewById(R.id.iconTref);
        iconPik = (ImageButton) findViewById(R.id.iconPik);
        iconHerc= (ImageButton) findViewById(R.id.iconHerc);
        iconKaro = (ImageButton) findViewById(R.id.iconKaro);
        iconZvezda = (ImageButton) findViewById(R.id.iconZvezda);
        submit = (Button) findViewById(R.id.submitSkocko);




    }

//    protected void fab4() {
//        Intent intent = new Intent(SkockoActivity.this, KorakPoKorakActivity.class);
//        startActivity(intent);
//    }
}