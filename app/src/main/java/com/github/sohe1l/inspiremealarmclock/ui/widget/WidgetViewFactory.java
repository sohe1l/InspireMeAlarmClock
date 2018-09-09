package com.github.sohe1l.inspiremealarmclock.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.model.Quote;

class WidgetViewFactory  implements RemoteViewsService.RemoteViewsFactory {


    private final Context context;
    private String quoteText;
    private int count = 0;

    public WidgetViewFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }


    @Override
    public void onDataSetChanged() {
        Quote quote = Quote.getRandomQuote(context);
        quoteText = quote.getQuote();
        count = 1;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        view.setTextViewText(R.id.widget_list_text, quoteText);
        return view;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
