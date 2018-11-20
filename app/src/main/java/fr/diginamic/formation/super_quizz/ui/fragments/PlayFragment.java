package fr.diginamic.formation.super_quizz.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.model.Question;
import fr.diginamic.formation.super_quizz.model.TypeQuestion;
import fr.diginamic.formation.super_quizz.ui.activities.QuestionActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * Use the {@link PlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment {
    private static Question q;

    private Button buttonPlay;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_QUESTION = "question";


    public PlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, q);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         *  creation de listes de reponses
         *  ajout de questions
         */
        List<String> listp = new ArrayList<>();
        listp.add("mathis");
        listp.add("corentin");
        listp.add("toto");
        listp.add("bruno");
        q = new Question("Quel est mon prénom ?", listp,"mathis",TypeQuestion.SIMPLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);
        rootView.findViewById(R.id.button_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), QuestionActivity.class);
                i.putExtra("question", q);
                startActivity(i);
            }
        });
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
}
