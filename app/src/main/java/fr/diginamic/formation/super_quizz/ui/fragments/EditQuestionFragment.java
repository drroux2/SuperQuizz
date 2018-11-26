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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.api.APIClient;
import fr.diginamic.formation.super_quizz.database.QuestionDatabaseHelper;
import fr.diginamic.formation.super_quizz.model.Question;
import fr.diginamic.formation.super_quizz.ui.activities.MainActivity;


public class EditQuestionFragment extends Fragment {


    private RadioButton radioButtonAnswer1, radioButtonAnswer2, radioButtonAnswer3, radioButtonAnswer4;
    private EditText editAnswer1, editAnswer2, editAnswer3, editAnswer4, editNameQuestion;
    private FloatingActionButton fabEditQuestion;


    private String goodAnswer = "";

    public OnEditQuestionListener mListener;

    private boolean isEditMode = false;

    private Question editQuestion = null;

    private int idEditQuestion;

    public EditQuestionFragment() {
        // Required empty public constructor
    }

    public static EditQuestionFragment newCreateQuestionInstance() {
        EditQuestionFragment fragment = new EditQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static EditQuestionFragment newEditQuestionInstance(Question q) {
        EditQuestionFragment fragment = new EditQuestionFragment();
        Bundle args = new Bundle();

        args.putParcelable("QUESTION",q);

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            editQuestion = getArguments().getParcelable("QUESTION");
            isEditMode = (editQuestion != null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_question, container, false);
        radioButtonAnswer1 = v.findViewById(R.id.radio_button_answer_1);
        radioButtonAnswer1.setChecked(true);
        radioButtonAnswer2 = v.findViewById(R.id.radio_button_answer_2);
        radioButtonAnswer3 = v.findViewById(R.id.radio_button_answer_3);
        radioButtonAnswer4 = v.findViewById(R.id.radio_button_answer_4);
        editAnswer1 = v.findViewById(R.id.edit_text_answer_1);
        editAnswer2 = v.findViewById(R.id.edit_text_answer_2);
        editAnswer3 = v.findViewById(R.id.edit_text_answer_3);
        editAnswer4 = v.findViewById(R.id.edit_text_answer_4);
        fabEditQuestion = v.findViewById(R.id.fab_edit_question);
        editNameQuestion = v.findViewById(R.id.edit_text_question_name);



        if(isEditMode){
            idEditQuestion = editQuestion.getIdQuestion();
            editNameQuestion.setText(editQuestion.getNameQuestion());
            editAnswer1.setText(editQuestion.getAnswers().get(0));
            editAnswer2.setText(editQuestion.getAnswers().get(1));
            editAnswer3.setText(editQuestion.getAnswers().get(2));
            editAnswer4.setText(editQuestion.getAnswers().get(3));
            if(editAnswer1.equals(editQuestion.getGoodAnswer())){
                radioButtonAnswer1.setChecked(true);
            }
            if(editAnswer2.equals(editQuestion.getGoodAnswer())){
                radioButtonAnswer2.setChecked(true);
            }
            if(editAnswer3.equals(editQuestion.getGoodAnswer())){
                radioButtonAnswer3.setChecked(true);
            }
            if(editAnswer4.equals(editQuestion.getGoodAnswer())){
                radioButtonAnswer4.setChecked(true);
            }
        }


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

                    if(isEditMode){
                        editQuestion.setNameQuestion(editNameQuestion.getText().toString());
                        editQuestion.setAnswers(listAnswers);
                        editQuestion.setGoodAnswer(goodAnswer);
                        mListener.updateQuestion(editQuestion);
                    }else {
                        try {
                            APIClient.getInstance().addQuestion("POST",question, new APIClient.APIResult<Question>() {
                                @Override
                                public void onFailure(IOException e) {
                                    Log.d("fail",e.getMessage());
                                    mListener.returnToList();
                                }

                                @Override
                                public void OnSuccess(Question object) throws IOException {
                                    Log.d("success","success");
                                    APIClient.getInstance().getQuestions(new APIClient.APIResult<List<Question>>() {
                                        @Override
                                        public void onFailure(IOException e) {
                                            // TODO : Nothing
                                        }

                                        @Override
                                        public void OnSuccess(List<Question> questions) throws IOException {
                                            QuestionDatabaseHelper helper = QuestionDatabaseHelper.getInstance(getContext());
                                            helper.synchroniseDatabaseQuestions(questions);
                                            mListener.returnToList();
                                        }
                                    });

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            e.getMessage();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
        void returnToList();
        void updateQuestion(Question q);
    }

}
