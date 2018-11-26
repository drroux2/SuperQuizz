package fr.diginamic.formation.super_quizz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import java.io.IOException;
import java.util.List;

import fr.diginamic.formation.super_quizz.R;
import fr.diginamic.formation.super_quizz.api.APIClient;
import fr.diginamic.formation.super_quizz.database.QuestionDatabaseHelper;
import fr.diginamic.formation.super_quizz.model.Question;
import fr.diginamic.formation.super_quizz.ui.fragments.EditQuestionFragment;
import fr.diginamic.formation.super_quizz.ui.fragments.PlayFragment;
import fr.diginamic.formation.super_quizz.ui.fragments.QuestionListFragment;
import fr.diginamic.formation.super_quizz.ui.fragments.ScoreFragment;
import fr.diginamic.formation.super_quizz.ui.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionListFragment.OnListFragmentInteractionListener, EditQuestionFragment.OnEditQuestionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            initListQuestion();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        APIClient.getInstance().getQuestions(new APIClient.APIResult<List<Question>>() {
            @Override
            public void onFailure(IOException e) {
                // TODO : Nothing
            }

            @Override
            public void OnSuccess(List<Question> questions) throws IOException {
                QuestionDatabaseHelper helper = QuestionDatabaseHelper.getInstance(MainActivity.this);
                helper.synchroniseDatabaseQuestions(questions);
                initListQuestion();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_score) {
            initScoreFragment();
        } else if (id == R.id.nav_informations) {
            Intent intent = new Intent(this, InformationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_play) {
            initPlayFragment();
        } else if (id == R.id.nav_settings) {
            initSettingsFragment();
        } else if (id == R.id.nav_edit_question) {
            initEditQuestionFragment(null);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startQuizzWithQuestion(Question item) {
        Intent i = new Intent(this, QuestionActivity.class);
        i.putExtra("question", item);
        startActivity(i);
    }

    @Override
    public void editQuestion(Question item) {
        initEditQuestionFragment(item);
    }

    private void initListQuestion(){
        QuestionListFragment questionListFragment = new QuestionListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, questionListFragment);
        transaction.commit();
    }

    private void initSettingsFragment(){
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, settingsFragment);
        transaction.commit();
    }

    private void initPlayFragment(){
        PlayFragment playFragment = new PlayFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, playFragment);
        transaction.commit();
    }

    private void initScoreFragment(){
        ScoreFragment scoreFragment = new ScoreFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, scoreFragment);
        transaction.commit();
    }

    private void initEditQuestionFragment(Question q){
        EditQuestionFragment editQuestionFragment = null;
        if(q == null) {
            editQuestionFragment = EditQuestionFragment.newCreateQuestionInstance();
        }else{
            editQuestionFragment = EditQuestionFragment.newEditQuestionInstance(q);
        }
        editQuestionFragment.mListener = this;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_main, editQuestionFragment);
        transaction.commit();

    }

    @Override
    public void saveQuestion(Question q) {
        QuestionDatabaseHelper databaseHelper = QuestionDatabaseHelper.getInstance(this);
        databaseHelper.addQuestion(q);
        initListQuestion();
    }

    @Override
    public void returnToList() {
        initListQuestion();
    }

    @Override
    public void updateQuestion(Question q) {
        QuestionDatabaseHelper databaseHelper = QuestionDatabaseHelper.getInstance(this);
        databaseHelper.updateQuestion(q);
        initListQuestion();
    }

}
