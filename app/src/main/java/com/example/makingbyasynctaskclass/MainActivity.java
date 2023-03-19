package com.example.makingbyasynctaskclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.makingbyasynctaskclass.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        binding.rollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requiredTimes = Integer.parseInt((binding.rollingNumberEditText.
                        getText().toString().trim()));

                new processDiceInBackground().execute(requiredTimes);

            }
        });
    }

    public class processDiceInBackground extends AsyncTask<Integer, Integer, String>
    {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax( Integer.parseInt((binding.rollingNumberEditText.
                    getText().toString().trim())) );
            dialog.show();
            //
            binding.resultTextView.setVisibility(View.GONE);
        }


        @Override
        protected String doInBackground(Integer... integers) {


            int ones = 0, two = 0, threes = 0, fours = 0, fives = 0, sixes = 0, randomNumber;

            Random random = new Random();
            //
            String results;

            double currentProgress = 0;
            double previousProgress = 0;
            //
            for(int i = 0; i < integers[0]; ++i) {
                currentProgress = (double) i/integers[0];
                if(currentProgress-previousProgress >= 0.02) {
                    publishProgress(i);
                    previousProgress = currentProgress;
                }
                //
                randomNumber = random.nextInt(6)+1;
                //
                switch (randomNumber) {
                    case 1:
                        ones++;
                        break;
                    case 2:
                        two++;
                        break;
                    case 3:
                        threes++;
                        break;
                    case 4:
                        fours++;
                        break;
                    case 5:
                        fives++;
                        break;
                    case 6:
                        sixes++;
                        break;
                    default:
                        Toast.makeText(MainActivity.this, R.string.No_predicted_outcomes,
                                Toast.LENGTH_SHORT).show();
                }
            }

            results = "Results: \n1: " + ones + "\n2: " + two + "\n3: " + threes
                    +"\n4: " + fours + "\n5: " + fives + "\n6: " + sixes;


            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            binding.resultTextView.setText(s);
            Toast.makeText(MainActivity.this, getString(R.string.done),
                    Toast.LENGTH_SHORT).show();
            binding.resultTextView.setVisibility(View.VISIBLE);
        }

    }
}