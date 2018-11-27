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

    private final String KEY_ID = "id";
    private final String KEY_TITLE = "title";
    private final String KEY_ANSWER_1 = "answer_1";
    private final String KEY_ANSWER_2 = "answer_2";
    private final String KEY_ANSWER_3 = "answer_3";
    private final String KEY_ANSWER_4 = "answer_4";
    private final String KEY_CORRECT_ANSWER = "correct_answer";
    private final String KEY_AUTHOR = "author";
    private final String KEY_URL = "author_img_url";
    private final String KEY_URL_IMAGE ="https://avatars3.githubusercontent.com/u/14200799?s=400&v=4";

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

                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Question question = new Question();

                        question.setIdQuestion(jsonObject.getInt(KEY_ID));
                        question.setNameQuestion(jsonObject.getString(KEY_TITLE));

                        List<String> listAnswers = new ArrayList<>();
                        listAnswers.add(jsonObject.getString(KEY_ANSWER_1));
                        listAnswers.add(jsonObject.getString(KEY_ANSWER_2));
                        listAnswers.add(jsonObject.getString(KEY_ANSWER_3));
                        listAnswers.add(jsonObject.getString(KEY_ANSWER_4));

                        switch(jsonObject.getInt(KEY_CORRECT_ANSWER)){

                            case 1:
                                question.setGoodAnswer(jsonObject.getString(KEY_ANSWER_1));
                                break;
                            case 2:
                                question.setGoodAnswer(jsonObject.getString(KEY_ANSWER_2));
                                break;
                            case 3:
                                question.setGoodAnswer(jsonObject.getString(KEY_ANSWER_3));
                                break;
                            case 4:
                                question.setGoodAnswer(jsonObject.getString(KEY_ANSWER_4));
                                break;
                        }

                        question.setAnswers(listAnswers);

                        question.setImageURL(jsonObject.getString(KEY_URL));

                        //set author apres avoir changé le model

                        question.setUserAnswer(null);
                        questions.add(question);
                    }
                } catch (JSONException e) {

                }
                result.OnSuccess(questions);
            }
        });

    }

    public void updateQuestion(final Question question, final APIResult<Question> result) throws JSONException {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject json = createJsonObject(question);

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder().url(URL_SERVER+"/"+question.getIdQuestion()).put(body).build();

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


    public void addQuestion(final Question question, final APIResult<Question> result) throws JSONException {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject json = createJsonObject(question);

        RequestBody body = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder().url(URL_SERVER).post(body).build();


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

    private JSONObject createJsonObject(Question question) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_TITLE,question.getNameQuestion());
        json.put(KEY_ANSWER_1,question.getAnswers().get(0));
        json.put(KEY_ANSWER_2,question.getAnswers().get(1));
        json.put(KEY_ANSWER_3,question.getAnswers().get(2));
        json.put(KEY_ANSWER_4,question.getAnswers().get(3));
        if(question.getGoodAnswer().equals(question.getAnswers().get(0))){
            json.put(KEY_CORRECT_ANSWER,1);
        }
        if(question.getGoodAnswer().equals(question.getAnswers().get(1))){
            json.put(KEY_CORRECT_ANSWER,2);
        }
        if(question.getGoodAnswer().equals(question.getAnswers().get(2))){
            json.put(KEY_CORRECT_ANSWER,3);
        }
        if(question.getGoodAnswer().equals(question.getAnswers().get(3))){
            json.put(KEY_CORRECT_ANSWER,4);
        }

        //json.put(KEY_AUTHOR,"mathis");
        json.put(KEY_URL, KEY_URL_IMAGE);

        return json;
    }

}
