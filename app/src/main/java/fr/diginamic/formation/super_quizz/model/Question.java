package fr.diginamic.formation.super_quizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
	
	private String nameQuestion;
	private List<String> answers;
	private String goodAnswer;
	private TypeQuestion type;
	

	public Question(String nameQuestion, List<String> answers, String goodAnswer) {
		super();
		this.nameQuestion = nameQuestion;
		this.answers = answers;
		this.goodAnswer = goodAnswer;
	}

	private Question(Parcel in) {
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
	
	public TypeQuestion getType() {
		return type;
	}

	public void setType(TypeQuestion type) {
		this.type = type;
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
	dest.writeString(this.nameQuestion);
	dest.writeStringList(this.answers);
	dest.writeString(this.goodAnswer);
	}
}
