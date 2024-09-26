package com.verindrzya.mytodo.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * The receiver, extends from BroadcastReceiver
 */
class TodoListWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TodoListWidget()

    /**
     * called automatically based on, updatePeriodMillis in AppWidgetProviderInfo
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    /**
     * do something that should occured only once in this method
     * example: open a new database or perform setup
     */
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }
}