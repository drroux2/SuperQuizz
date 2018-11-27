package fr.diginamic.formation.super_quizz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.diginamic.formation.super_quizz.model.Question;

import static android.content.ContentValues.TAG;

public class QuestionDatabaseHelper extends SQLiteOpenHelper {

    private static QuestionDatabaseHelper sInstance;

    private static final String DATABASE_NAME = "qcmDatabase";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_QUESTIONS = "QUESTION";

    private static final String KEY_QUESTION_ID = "id_question";
    private static final String KEY_QUESTION_ID_SERVER ="id_question_server";
    private static final String KEY_QUESTION_NAME = "question_name";
    private static final String KEY_QUESTION_ANSWER_1 = "answer1";
    private static final String KEY_QUESTION_ANSWER_2 = "answer2";
    private static final String KEY_QUESTION_ANSWER_3 = "answer3";
    private static final String KEY_QUESTION_ANSWER_4 = "answer4";
    private static final String KEY_QUESTION_GOOD_ANSWER = "good_answer";
    private static final String KEY_QUESTION_USER_ANSWER = "user_answer";
    private static final String KEY_QUESTION_USER_IMAGE = "image_url";



    //SINGLETON
    public static synchronized QuestionDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new QuestionDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private QuestionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_QUESTION_TABLE = "CREATE TABLE " +
                TABLE_QUESTIONS + "(" +
                KEY_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_QUESTION_ID_SERVER + " INTEGER," +
                KEY_QUESTION_NAME + " VARCHAR(100)," +
                KEY_QUESTION_ANSWER_1 + " VARCHAR(100)," +
                KEY_QUESTION_ANSWER_2 + " VARCHAR(100)," +
                KEY_QUESTION_ANSWER_3 + " VARCHAR(100)," +
                KEY_QUESTION_ANSWER_4 + " VARCHAR(100)," +
                KEY_QUESTION_GOOD_ANSWER + " VARCHAR(100)," +
                KEY_QUESTION_USER_ANSWER + " CHAR(1)," +
                KEY_QUESTION_USER_IMAGE + " VARCHAR(250)" +
                ")";

        db.execSQL(CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
            onCreate(db);
        }
    }

    public List<Question> getAllQuestions(){
        List<Question> questions = new ArrayList<>();

        String QUESTION_SELECT_QUERY = String.format("SELECT id_question, question_name, answer1, answer2, answer3, answer4, good_answer, image_url FROM %s", TABLE_QUESTIONS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUESTION_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Question newQuestion = new Question();
                    newQuestion.setIdQuestion(cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID)));
                    newQuestion.setNameQuestion(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_NAME)));
                    String answer1 = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWER_1));
                    String answer2 = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWER_2));
                    String answer3 = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWER_3));
                    String answer4 = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_ANSWER_4));
                    newQuestion.setGoodAnswer(cursor.getString((cursor.getColumnIndex(KEY_QUESTION_GOOD_ANSWER))));
                    newQuestion.setImageURL(cursor.getString(cursor.getColumnIndex(KEY_QUESTION_USER_IMAGE)));
                    List<String> listAnswers = new ArrayList<>();
                    listAnswers.add(answer1);
                    listAnswers.add(answer2);
                    listAnswers.add(answer3);
                    listAnswers.add(answer4);
                    questions.add(newQuestion);
                    newQuestion.setAnswers(listAnswers);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return questions;
    }

    public void addQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_NAME,question.getNameQuestion());
            values.put(KEY_QUESTION_ID_SERVER, question.getIdServerQuestion());
            values.put(KEY_QUESTION_ANSWER_1, question.getAnswers().get(0));
            values.put(KEY_QUESTION_ANSWER_2, question.getAnswers().get(1));
            values.put(KEY_QUESTION_ANSWER_3, question.getAnswers().get(2));
            values.put(KEY_QUESTION_ANSWER_4, question.getAnswers().get(3));
            values.put(KEY_QUESTION_GOOD_ANSWER, question.getGoodAnswer());
            values.put(KEY_QUESTION_USER_IMAGE, question.getImageURL());

            db.insertOrThrow(TABLE_QUESTIONS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String answer1 = question.getAnswers().get(0);

        values.put(KEY_QUESTION_NAME, question.getNameQuestion());
        values.put(KEY_QUESTION_ANSWER_1, answer1);
        values.put(KEY_QUESTION_ANSWER_2, question.getAnswers().get(1));
        values.put(KEY_QUESTION_ANSWER_3, question.getAnswers().get(2));
        values.put(KEY_QUESTION_ANSWER_4, question.getAnswers().get(3));
        values.put(KEY_QUESTION_GOOD_ANSWER, question.getGoodAnswer());
        return db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + " = " + question.getIdQuestion(), null);

    }

    public int updateUserAnswer(Question question){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_USER_ANSWER, question.getUserAnswer());
        return db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + " = " + question.getIdQuestion(), null);
    }

    public int updateAllAnswers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_USER_ANSWER, "null");
        return db.update(TABLE_QUESTIONS, values, null, null);
    }


    public void deleteQuestion(Question question){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_QUESTION_ID + "=" + question.getIdQuestion(), null);
    }


    public void synchroniseDatabaseQuestions(List<Question> serverQuestions) {

        List<Question> databaseQuestions = getAllQuestions();


        // Here we will choose if we need to add or to update the question return by the server
        for (Question serverQuestion : serverQuestions) {
            boolean found = false;
            for (Question dataBaseQuestion : databaseQuestions) {
                if (serverQuestion.getIdQuestion() == dataBaseQuestion.getIdQuestion()) {
                    found = true;
                    break;
                }
            }

            if (found) {
                updateQuestion(serverQuestion);
            } else {
                addQuestion(serverQuestion);
            }
        }

        // Now we want to delete the question if thy are not on the server anymore
        for (Question dataBaseQuestion : databaseQuestions) {
            boolean found = false;
            for (Question serverQuestion : serverQuestions) {
                if (serverQuestion.getIdQuestion() == dataBaseQuestion.getIdQuestion()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                deleteQuestion(dataBaseQuestion);
            }
        }
    }


    /**
     * value = 1 correct_answer
     * value = 0 wrong_answer
     * value = null no answer
     * @param value
     * @return
     */
    public int countNbAnswer(String value){
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        if(value != null) {
            count = (int) DatabaseUtils.queryNumEntries(db, TABLE_QUESTIONS, KEY_QUESTION_USER_ANSWER + "=" + value);
        }else{
            count = (int) DatabaseUtils.queryNumEntries(db, TABLE_QUESTIONS, KEY_QUESTION_USER_ANSWER + " IS NULL");
        }
        return count;
    }

}
