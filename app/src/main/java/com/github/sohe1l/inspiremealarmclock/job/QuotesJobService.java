package com.github.sohe1l.inspiremealarmclock.job;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Quote;
import com.github.sohe1l.inspiremealarmclock.network.GetQuotesService;
import com.github.sohe1l.inspiremealarmclock.network.RetrofitClientInstance;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuotesJobService extends JobService {

    private static final String TAG = QuotesJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters job) {
        // Do some work here
        GetQuotesService service = RetrofitClientInstance.getRetrofitInstance().create(GetQuotesService.class);
        Call<List<Quote>> call = service.getQuotes();

        call.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {

                if (response.isSuccessful()) {
                    AppDatabase mDB = AppDatabase.getInstance(getApplicationContext());

                    List<Quote> quotesList = response.body();
                    for (Quote q: quotesList) {
                        Quote quote = new Quote(q.getQuote(), q.getAuthor(), q.getCategory());
                        mDB.quoteDao().insert(quote);
                    }
                    jobFinished(job, false);
                }else{
                    jobFinished(job, true);
                }
            }
            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Log.d(TAG, "Error on loading quotes from the API: " + t.getMessage());
                jobFinished(job, true);
            }
        });


        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        // since we did not called jobFinished therefore the job should be rescheduled
        return true;
    }


    public static void scheduleQuoteJob(Context context){

        // set job for updating quotes
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job quotesJob = dispatcher.newJobBuilder()
                .setService(QuotesJobService.class)
                .setTag(Quote.JOB_TAG)
                //.setReplaceCurrent(true)
                //.setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                //.setTrigger(Trigger.executionWindow(0, 5))
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(quotesJob);
    }
}