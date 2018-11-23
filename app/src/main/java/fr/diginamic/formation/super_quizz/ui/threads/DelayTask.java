package fr.diginamic.formation.super_quizz.ui.threads;

import android.os.AsyncTask;
import android.os.SystemClock;

public class DelayTask extends AsyncTask<Void, Integer, String> {

    public OnDelayTaskListener onDelayTaskListener;
    int count = 0;
    int durationSec = 60;
    int stepMs = 1000;
    //float totalProgress = 100;


    public DelayTask(OnDelayTaskListener listener){
        super();
        this.onDelayTaskListener = listener;
    }

    @Override
    protected void onPreExecute() {
        onDelayTaskListener.willStart();
    }

    @Override
    protected String doInBackground(Void... params) {
        while (count < durationSec) {
            SystemClock.sleep(stepMs);
            count++;
            publishProgress(count );
        }
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        onDelayTaskListener.onProgress(count);
    }

    public interface OnDelayTaskListener{
        void willStart();
        void onProgress(int progress);
    }
}
