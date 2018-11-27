package fr.diginamic.formation.super_quizz.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.database.QuestionDatabaseHelper;
import fr.diginamic.formation.super_quizz.model.Question;
import fr.diginamic.formation.super_quizz.ui.threads.DelayTask;

public class QuestionActivity extends AppCompatActivity implements DelayTask.OnDelayTaskListener {


    private Question question;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question = getIntent().getParcelableExtra("question");
        DelayTask delayTask = new DelayTask(QuestionActivity.this);
        TextView textViewQuestion;
        Button buttonRep1, buttonRep2, buttonRep3, buttonRep4;
        textViewQuestion = findViewById(R.id.textview_question);
        buttonRep1 = findViewById(R.id.button_rep_1);
        buttonRep2 = findViewById(R.id.button_rep_2);
        buttonRep3 = findViewById(R.id.button_rep_3);
        buttonRep4 = findViewById(R.id.button_rep_4);

        textViewQuestion.setText(question.getNameQuestion());

        buttonRep1.setText(question.getAnswers().get(0));
        buttonRep2.setText(question.getAnswers().get(1));
        buttonRep3.setText(question.getAnswers().get(2));
        buttonRep4.setText(question.getAnswers().get(3));

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        buttonRep1.startAnimation(rotate);
        buttonRep2.startAnimation(rotate);
        buttonRep3.startAnimation(rotate);
        buttonRep4.startAnimation(rotate);

        pb = findViewById(R.id.progressBar_time);
        delayTask.execute();
    }


    public void openResultat(View view) {
        Button buttonTap = (Button)view;
        String buttonText = buttonTap.getText().toString();
        QuestionDatabaseHelper databaseHelper = QuestionDatabaseHelper.getInstance(this);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        if(mSettings.getBoolean("keepAnswers",true)){
            if(buttonText.equals(question.getGoodAnswer())){
                question.setUserAnswer("1");
            }else{
                question.setUserAnswer("0");
            }
        }

        databaseHelper.updateUserAnswer(question);
        Intent i = new Intent(this, ResultatActivity.class);
        if(buttonText.equals(question.getGoodAnswer())){
            i.putExtra("succes", "1");
        }else{
            i.putExtra("succes", "0");
        }
        startActivity(i);
        finish();


    }

    @Override
    public void willStart() {
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgress(int progress) {
        pb.setProgress(progress);
    }

}
