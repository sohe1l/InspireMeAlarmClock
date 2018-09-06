package com.github.sohe1l.inspiremealarmclock.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new WidgetViewFactory(this.getApplicationContext(), intent));
    }
}

