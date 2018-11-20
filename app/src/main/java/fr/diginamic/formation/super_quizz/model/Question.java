package fr.diginamic.formation.super_quizz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
	
	private String intitule;
	private List<String> propositions;
	private String bonneReponse;
	private TypeQuestion type;
	

	public Question(String intitule, List<String> propositions, String bonneReponse) {
		super();
		this.intitule = intitule;
		this.propositions = propositions;
		this.bonneReponse = bonneReponse;
	}

	private Question(Parcel in) {
		intitule = in.readString();
		propositions = in.createStringArrayList();
		bonneReponse = in.readString();
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

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public List<String> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<String> propositions) {
		this.propositions = propositions;
	}

	public String getBonneReponse() {
		return bonneReponse;
	}

	public void setBonneReponse(String bonneReponse) {
		this.bonneReponse = bonneReponse;
	}
	
	public TypeQuestion getType() {
		return type;
	}

	public void setType(TypeQuestion type) {
		this.type = type;
	}

	public boolean verifierReponse(String reponse) {
		boolean reponseOK = false;
		if(reponse.equals(this.bonneReponse)) {
			reponseOK = true;
		}
		return reponseOK;
	}
	
	public void addProposition(String proposition) {
		this.propositions.add(proposition);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(this.intitule);
	dest.writeStringList(this.propositions);
	dest.writeString(this.bonneReponse);
	}
}
