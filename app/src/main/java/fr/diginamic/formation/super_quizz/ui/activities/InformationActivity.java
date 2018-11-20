package fr.diginamic.formation.super_quizz.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fr.diginamic.formation.super_quizz.R;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
    }

    public void mailContact(View view) {
        String uriText =
                "mailto:seigle.mathis@gmail.com" +
                        "?subject=" + Uri.encode("envoi depuis app android") +
                        "&body=" + Uri.encode("contact");

        Uri uri = Uri.parse(uriText);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        startActivity(Intent.createChooser(sendIntent, "Send email"));
    }

    public void githubAcces(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/mathis86180"));
        startActivity(browserIntent);
    }
}
