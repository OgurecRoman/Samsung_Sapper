package com.example.cells;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    Button[][] cells;
    Button restart;
    int WIDTH = 8;
    int HEIGHT = 10;
    int Mines_Count = 10;
    int Mines_Current = 10;
    TextView mines_text;
    TextView worl;
    int[][] mines = new int[HEIGHT][WIDTH];
    int[][] flags = new int[HEIGHT][WIDTH];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generate();

        mines_text = (TextView) findViewById(R.id.Mines);
        mines_text.setText("" + Mines_Count + " / " + Mines_Current);
        worl = (TextView) findViewById(R.id.Worl);
    }

    public void rand() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                mines[i][j] = -1;
            }
        }

        for (int i = 0; i < Mines_Count; i++) {
            int h = ThreadLocalRandom.current().nextInt(0, HEIGHT);
            int w = ThreadLocalRandom.current().nextInt(0, WIDTH);
            while (mines[h][w] == -10){
                h = ThreadLocalRandom.current().nextInt(0, HEIGHT);
                w = ThreadLocalRandom.current().nextInt(0, WIDTH);
            }
            mines[h][w] = -10;
        }
    }

    public boolean lose(int h, int w) {
        if (mines[h][w] == -10) return true;
        return false;
    }

    public boolean win() {
        for (int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                if (mines[i][j] == -10 & flags[i][j] != -10) return false;
            }
        }
        return true;
    }

    public void zero(int h, int w) {
        for (int i = (h - 1); i < (h + 2); i++) {
            for (int j = (w - 1); j < (w + 2); j++) {
                if ((i >= 0) & (i < HEIGHT) & (j >= 0) & (j < WIDTH))
                    if (mines[i][j] == -1) check(i, j);
            }
        }
    }

    public void check(int h, int w) {
        int count = 0;
        for (int i = (h - 1); i < (h + 2); i++) {
            for (int j = (w - 1); j < (w + 2); j++) {
                if ((i >= 0) & (i < HEIGHT) & (j >= 0) & (j < WIDTH)) {
                    if (mines[i][j] == -10) count++;
                }
            }
        }
        mines[h][w] = count;
        if (count == 0) zero(h, w);
    }

    public void generate() {
        cells = new Button[HEIGHT][WIDTH];
        restart = findViewById(R.id.Restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();

                Mines_Current = Mines_Count;
                mines_text.setText("" + Mines_Count + " / " + Mines_Current);
                worl.setText("");
            }
        });

        LayoutInflater inflather = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GridLayout cellslayout = (GridLayout) findViewById(R.id.Grid);
        cellslayout.removeAllViews();
        cellslayout.setColumnCount(WIDTH);

        rand();

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cells[i][j] = (Button) inflather.inflate(R.layout.cell, cellslayout, false);
            }
        }

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                /*if (mines[i][j] == -10) {
                    cells[i][j].setBackgroundColor(Color.RED);
                }*/
                cells[i][j].setTag(new TableData(i, j, 0));
                cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TableData tableData = (TableData) view.getTag();
                        int h = tableData.RowIndex, w = tableData.ColumnIndex;
                        if (flags[h][w] != -10){
                            if (lose(h, w)) {
                                for (int y = 0; y < HEIGHT; y++) {
                                    for (int x = 0; x < WIDTH; x++) {
                                        if (mines[y][x] == -10) cells[y][x].setBackgroundColor(Color.RED);
                                        worl.setText("Ты проиграл!");
                                    }
                                }
                            } else {
                                check(tableData.RowIndex, tableData.ColumnIndex);
                                for (int y = 0; y < HEIGHT; y++) {
                                    for (int x = 0; x < WIDTH; x++) {
                                        if (mines[y][x] > 0) cells[y][x].setText("" + mines[y][x]);
                                    }
                                }
                            }
                        }
                    }
                });

                cells[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        TableData tableData = (TableData) v.getTag();
                        int h = tableData.RowIndex, w = tableData.ColumnIndex;
                        if (mines[h][w] < 0){
                            if (flags[h][w] == -10){
                                Mines_Current++;
                                mines_text.setText("" + Mines_Count + " / " + Mines_Current);
                                flags[tableData.RowIndex][tableData.ColumnIndex] = -1;
                                cells[h][w].setBackgroundColor(Color.LTGRAY);
                                return false;
                            }
                            flags[h][w] = -10;
                            v.setBackgroundColor(Color.BLUE);
                            Mines_Current--;
                            mines_text.setText("" + Mines_Count + " / " + Mines_Current);
                            if (Mines_Current == 0){
                                if (win()){
                                    worl.setText("Ты выиграл!");
                                }
                            }
                        }
                        return false;
                    }
                });
                cellslayout.addView(cells[i][j]);
            }
        }

    }
}
