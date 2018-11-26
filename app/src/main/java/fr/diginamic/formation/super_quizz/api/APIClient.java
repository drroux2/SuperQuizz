package fr.diginamic.formation.super_quizz.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.diginamic.formation.super_quizz.model.Question;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIClient {

    //"http://192.168.10.154:3000/questions"
    //http://192.168.10.38:3000/questions
    private final String URL_SERVER = "http://192.168.10.154:3000/questions";

    private final OkHttpClient client = new OkHttpClient();

    private static APIClient sInstance = new APIClient();

    public static APIClient getInstance(){
        return sInstance;
    }

    public void getQuestions(final APIResult<List<Question>> result) {

        Request request = new Request.Builder().url(URL_SERVER).build();

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

    }

    public void updateQuestion(Question question,final APIResult<Question> result){

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject json = new JSONObject();

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder().url(URL_SERVER).put(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    public void addQuestion(String choice,final Question question, final APIResult<Question> result) throws IOException, JSONException {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        json.put("title",question.getNameQuestion());
        json.put("answer_1",question.getAnswers().get(0));
        json.put("answer_2",question.getAnswers().get(1));
        json.put("answer_3",question.getAnswers().get(2));
        json.put("answer_4",question.getAnswers().get(3));
        if(question.getGoodAnswer().equals(question.getAnswers().get(0))){
            json.put("correct_answer",1);
        }
        if(question.getGoodAnswer().equals(question.getAnswers().get(1))){
            json.put("correct_answer",2);
        }
        if(question.getGoodAnswer().equals(question.getAnswers().get(2))){
            json.put("correct_answer",3);
        }
        if(question.getGoodAnswer().equals(question.getAnswers().get(3))){
            json.put("correct_answer",4);
        }

        json.put("author","mathis");

        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = null;
        if(choice.equals("POST")) {
            request = new Request.Builder().url(URL_SERVER).post(body).build();
        }
        if(choice.equals("PUT")) {
            request = new Request.Builder().url(URL_SERVER).put(body).build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result.OnSuccess(question);
            }
        });

    }


    public void deleteQuestion(){

    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void OnSuccess(T object) throws IOException;
    }


}
