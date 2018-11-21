package fr.diginamic.formation.super_quizz.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.model.Question;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link EditQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditQuestionFragment extends Fragment {

    private String goodAnswer = "";

    public OnEditQuestionListener mListener;

    public EditQuestionFragment() {
        // Required empty public constructor
    }

    public static EditQuestionFragment newInstance() {
        EditQuestionFragment fragment = new EditQuestionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_question, container, false);
        final RadioButton radioButtonAnswer1 = v.findViewById(R.id.radio_button_answer_1);
        radioButtonAnswer1.setChecked(true);
        final RadioButton radioButtonAnswer2 = v.findViewById(R.id.radio_button_answer_2);
        final RadioButton radioButtonAnswer3 = v.findViewById(R.id.radio_button_answer_3);
        final RadioButton radioButtonAnswer4 = v.findViewById(R.id.radio_button_answer_4);

        final EditText editAnswer1 = v.findViewById(R.id.edit_text_answer_1);
        final EditText editAnswer2 = v.findViewById(R.id.edit_text_answer_2);
        final EditText editAnswer3 = v.findViewById(R.id.edit_text_answer_3);
        final EditText editAnswer4 = v.findViewById(R.id.edit_text_answer_4);

        View.OnClickListener radioButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton b = (RadioButton) v;
                if(b.isChecked()){
                    radioButtonAnswer1.setChecked(false);
                    radioButtonAnswer2.setChecked(false);
                    radioButtonAnswer3.setChecked(false);
                    radioButtonAnswer4.setChecked(false);
                    b.setChecked(true);
                }
            }
        };

        radioButtonAnswer1.setOnClickListener(radioButtonListener);
        radioButtonAnswer2.setOnClickListener(radioButtonListener);
        radioButtonAnswer3.setOnClickListener(radioButtonListener);
        radioButtonAnswer4.setOnClickListener(radioButtonListener);


        final FloatingActionButton fabEditQuestion = v.findViewById(R.id.fab_edit_question);
        final EditText editNameQuestion = v.findViewById(R.id.edit_text_question_name);
        fabEditQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNullOrEmpty(editNameQuestion.getText().toString()) || isNullOrEmpty(editAnswer1.getText().toString()) || isNullOrEmpty(editAnswer2.getText().toString()) ||
                        isNullOrEmpty(editAnswer3.getText().toString()) || isNullOrEmpty(editAnswer4.getText().toString())){
                    Toast.makeText(getContext(), "Un ou plusieurs champd textes sont vides", Toast.LENGTH_LONG).show();
                }else{
                    if(radioButtonAnswer1.isChecked()){
                        goodAnswer = editAnswer1.getText().toString();
                    }
                    if(radioButtonAnswer2.isChecked()){
                        goodAnswer = editAnswer2.getText().toString();
                    }
                    if(radioButtonAnswer3.isChecked()){
                        goodAnswer = editAnswer3.getText().toString();
                    }
                    if(radioButtonAnswer4.isChecked()){
                        goodAnswer = editAnswer4.getText().toString();
                    }
                    List<String> listAnswers = new ArrayList<>();
                    listAnswers.add(editAnswer1.getText().toString());
                    listAnswers.add(editAnswer2.getText().toString());
                    listAnswers.add(editAnswer3.getText().toString());
                    listAnswers.add(editAnswer4.getText().toString());

                    Question question = new Question(editNameQuestion.getText().toString(),listAnswers, goodAnswer);
                    mListener.saveQuestion(question);

                    //TODO : effectuer l'ajout ou la modification en base de données
                }

            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public interface OnEditQuestionListener {
        void saveQuestion(Question q);
    }

}
