package fr.diginamic.formation.super_quizz.ui.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.model.Question;

public class QuestionActivity extends AppCompatActivity {


    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        question = getIntent().getParcelableExtra("question");

        TextView textViewQuestion;
        Button buttonRep1, buttonRep2, buttonRep3, buttonRep4;
        textViewQuestion = findViewById(R.id.textview_question);
        buttonRep1 = findViewById(R.id.button_rep_1);
        buttonRep2 = findViewById(R.id.button_rep_2);
        buttonRep3 = findViewById(R.id.button_rep_3);
        buttonRep4 = findViewById(R.id.button_rep_4);

        textViewQuestion.setText(question.getIntitule());

        buttonRep1.setText(question.getPropositions().get(0));
        buttonRep2.setText(question.getPropositions().get(1));
        buttonRep3.setText(question.getPropositions().get(2));
        buttonRep4.setText(question.getPropositions().get(3));

    }

    public void openResultat(View view) {
        Button buttonTap = (Button)view;
        String buttonText = buttonTap.getText().toString();

        Intent i = new Intent(this, ResultatActivity.class);
        if(buttonText.equals(question.getBonneReponse())){
            i.putExtra("succes", "1");
        }else{
            i.putExtra("succes", "0");
        }

        startActivityForResult(i,1);
        finish();


    }
}
