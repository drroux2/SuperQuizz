package fr.diginamic.formation.super_quizz.api;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.diginamic.formation.super_quizz.model.Question;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIClient {

    private final OkHttpClient client = new OkHttpClient();

    private static APIClient sInstance = new APIClient();

    public static APIClient getInstance(){
        return sInstance;
    }

    public void getQuestions(final APIResult<List<Question>> result) {

        Request request = new Request.Builder().url("http://192.168.10.38:3000/questions").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                result.onFailure(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Question> questions = new ArrayList<>();

                // TODO : Lire les questions depuis la reponse et les ajouter à la liste
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Question question = new Question();

                        question.setIdQuestion(jsonObject.getInt("id"));
                        question.setNameQuestion(jsonObject.getString("title"));

                        List<String> listAnswers = new ArrayList<>();
                        listAnswers.add(jsonObject.getString("answer_1"));
                        listAnswers.add(jsonObject.getString("answer_2"));
                        listAnswers.add(jsonObject.getString("answer_3"));
                        listAnswers.add(jsonObject.getString("answer_4"));

                        switch(jsonObject.getInt("correct_answer")){

                            case 1:
                                question.setGoodAnswer(jsonObject.getString("answer_1"));
                                break;
                            case 2:
                                question.setGoodAnswer(jsonObject.getString("answer_2"));
                                break;
                            case 3:
                                question.setGoodAnswer(jsonObject.getString("answer_3"));
                                break;
                            case 4:
                                question.setGoodAnswer(jsonObject.getString("answer_4"));
                                break;
                        }

                        question.setAnswers(listAnswers);
                        questions.add(question);
                    }
                } catch (JSONException e) {

                }
                result.OnSuccess(questions);
            }
        });


        //TODO : Faire un update
        //TODO : Faire un delete
        //TODO : Faire un Create
    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }


}
