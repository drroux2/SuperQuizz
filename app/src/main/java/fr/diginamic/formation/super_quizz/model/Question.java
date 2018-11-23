package fr.diginamic.formation.super_quizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Question implements Parcelable {

	private int idQuestion;
	private int idServerQuestion;
	private String nameQuestion;
	private List<String> answers;
	private String goodAnswer;
	private String userAnswer;


	public Question(){

	}

	public Question(int idQuestion, String nameQuestion, List<String> answers, String goodAnswer) {
		super();
		this.idQuestion = idQuestion;
		this.nameQuestion = nameQuestion;
		this.answers = answers;
		this.goodAnswer = goodAnswer;
	}

	public Question(String nameQuestion, List<String> answers, String goodAnswer) {
		super();
		this.nameQuestion = nameQuestion;
		this.answers = answers;
		this.goodAnswer = goodAnswer;
	}

	private Question(Parcel in) {
		idQuestion = in.readInt();
		nameQuestion = in.readString();
        answers = in.createStringArrayList();
        goodAnswer = in.readString();
	}

	public static final Creator<Question> CREATOR = new Creator<Question>() {
		@Override
		public Question createFromParcel(Parcel in) {
			return new Question(in);
		}

		@Override
		public Question[] newArray(int size) {
			return new Question[size];
		}
	};

	public int getIdQuestion(){ return idQuestion; }

	public void setIdQuestion(int idQuestion){ this.idQuestion = idQuestion; }

	public int getIdServerQuestion() {
		return idServerQuestion;
	}

	public void setIdServerQuestion(int idServerQuestion) {
		this.idServerQuestion = idServerQuestion;
	}

	public String getNameQuestion() {
		return nameQuestion;
	}

	public void setNameQuestion(String nameQuestion) {
		this.nameQuestion = nameQuestion;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> propositions) {
		this.answers = propositions;
	}

	public String getGoodAnswer() {
		return goodAnswer;
	}

	public void setGoodAnswer(String goodAnswer) {
		this.goodAnswer = goodAnswer;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public void addAnswer(String answer) {
		this.answers.add(answer);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.idQuestion);
		dest.writeString(this.nameQuestion);
		dest.writeStringList(this.answers);
		dest.writeString(this.goodAnswer);
	}
}
