package com.example.emb_32;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View v) {
        EditText ex1 = findViewById(R.id.x1);
        EditText ex2 = findViewById(R.id.x2);
        EditText ey1 = findViewById(R.id.y1);
        EditText ey2 = findViewById(R.id.y2);
        EditText ep = findViewById(R.id.p);

        Spinner ss = findViewById(R.id.spSigma);
        Spinner si = findViewById(R.id.spIter);
        Spinner st = findViewById(R.id.spTime);

        TextView resX = findViewById(R.id.resX);
        TextView resY = findViewById(R.id.resY);

        int x1 = 0, x2 = 0, y1 = 0, y2 = 0, p = 0, iterations = 0;
        double sigma = 0;
        long time = 0;
        double[] res = new double[2];

        try {
            x1 = Integer.parseInt(ex1.getText().toString());
            x2 = Integer.parseInt(ex2.getText().toString());
            y1 = Integer.parseInt(ey1.getText().toString());
            y2 = Integer.parseInt(ey2.getText().toString());
            p = Integer.parseInt(ep.getText().toString());
            iterations = Integer.parseInt(si.getSelectedItem().toString());
            sigma = Double.parseDouble(ss.getSelectedItem().toString());
            time = Long.parseLong(st.getSelectedItem().toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        if (ex1.getText().length() == 0 || ex2.getText().length() == 0
                || ey1.getText().length() == 0 || ey2.getText().length() == 0 || ep.getText().length() == 0) {
            Toast.makeText(
                    MainActivity.this, "Incorrect input!", Toast.LENGTH_LONG
            ).show();
        } else {
            res = neural(x1, x2, y1, y2, p, sigma, iterations, time);
        }

        resX.setText(Double.toString(res[0]));
        resY.setText(Double.toString(res[1]));
    }

    public double[] neural(int x1, int x2, int y1, int y2, int p, double sigma, int iterations, long time) {
        double[] res = new double[2];
        double W1 = 0, W2 = 0, delta;
        double p1 = 0, p2 = 0;
        int count = 0;
        long start = System.currentTimeMillis();

        while(p1 >= p || p2 <= p) {
            if (p1 >= p) {
                delta = p - p1;
                W1 += delta * x1 * sigma;
                W2 += delta * y1 * sigma;
            } else if (p2 <= p) {
                delta = p - p2;
                W1 += delta * x2 * sigma;
                W2 += delta * y2 * sigma;
            }
            p1 = x1 * W1 + y1 * W2;
            p2 = x2 * W1 + y2 * W2;
            count++;
            if (count >= iterations) {
                Toast.makeText(
                        MainActivity.this, "Max iterations reached!", Toast.LENGTH_LONG
                ).show();
                break;
            }

            long finish = System.currentTimeMillis();
            if (time * 1000 <= finish - start) {
                Toast.makeText(
                        MainActivity.this, "Max time reached!", Toast.LENGTH_LONG
                ).show();
                break;
            }
        }

        res[0] = W1;
        res[1] = W2;
        return res;
    }
}
