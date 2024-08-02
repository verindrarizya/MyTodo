package com.verindrzya.mytodo.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import com.verindrzya.mytodo.MainActivity
import com.verindrzya.mytodo.R

class TodoAddWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray
    ) {
        val bundle = Bundle().apply {
            putInt("id", 0)
        }
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.main_nav)
                .setDestination(R.id.addFragment)
                .setArguments(bundle)
                .setComponentName(MainActivity::class.java)
                .createPendingIntent()

            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_add_todo
            ).apply {
                setOnClickPendingIntent(R.id.fab_add_note, pendingIntent)
            }

            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }
}