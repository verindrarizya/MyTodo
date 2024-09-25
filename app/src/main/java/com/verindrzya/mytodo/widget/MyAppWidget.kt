package com.verindrzya.mytodo.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.RadioButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.verindrzya.mytodo.MainActivity
import com.verindrzya.mytodo.R
import com.verindrzya.mytodo.constant.PriorityLevel
import com.verindrzya.mytodo.constant.PriorityLevelHelper
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.di.TodoListWidgetEntryPoint
import dagger.hilt.EntryPoints

private val clickedWidgetTodoIdKey = ActionParameters.Key<Int>(
    MainActivity.KEY_CLICKED_WIDGET_TODO_ID
)

/**
 * The Widget itself
 */
class MyAppWidget : GlanceAppWidget(
    errorUiLayout = R.layout.todo_widget_error_layout
) {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In dis method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations

        val todoRepository = EntryPoints.get(context, TodoListWidgetEntryPoint::class.java)
            .getTodoRepository()

        provideContent {
            var selectedPriorityLevel by remember { mutableStateOf(PriorityLevel.All.name) }

            val todoData by todoRepository.getLimitedItems(20, selectedPriorityLevel)
                .collectAsState(initial = listOf())

            Scaffold(
                backgroundColor = ColorProvider(Color.Black),
                titleBar = {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = GlanceModifier
                                .padding(
                                    top = 10.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 6.dp
                                )
                                .fillMaxWidth(),
                            text = "Todo List",
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(GlanceModifier.height(4.dp))
                        Row(
                            modifier = GlanceModifier
                                .padding(
                                    start = 14.dp,
                                    end = 14.dp,
                                    bottom = 8.dp
                                )
                                .fillMaxWidth()
                        ) {
                            for (priorityLevel in PriorityLevelHelper.filterPriorityLevel) {
                                RadioButton(
                                    checked = priorityLevel == selectedPriorityLevel,
                                    onClick = {
                                        selectedPriorityLevel = priorityLevel
                                    },
                                    text = priorityLevel
                                )
                                Spacer(modifier = GlanceModifier.width(6.dp))
                            }
                        }
                    }
                }
            ) {
                if (todoData.isEmpty()) {
                    EmptyView()
                } else {
                    TodoListContent(
                        todoList = todoData
                    )
                }
            }
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        super.onCompositionError(context, glanceId, appWidgetId, throwable)
        val rv = RemoteViews(context.packageName, R.layout.todo_widget_error_layout)
        rv.setTextViewText(
            R.id.tv_error_message,
            "Error happened, please contact support / re-adding the widget\nError: $throwable"
        )
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rv)
    }
}

@Composable
fun EmptyView(
    modifier: GlanceModifier = GlanceModifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Your Todo list is empty",
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        )
    }
}

@Composable
fun TodoListContent(
    todoList: List<Todo>,
    modifier: GlanceModifier = GlanceModifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(todoList) { todo ->
            TodoItem(
                modifier = GlanceModifier.padding(
                    horizontal = 8.dp,
                    vertical = 2.dp
                ),
                todoItem = todo
            )
        }
    }
}

@Composable
fun TodoItem(
    todoItem: Todo,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(Color.Cyan)
            .cornerRadius(8.dp)
            .clickable(
                actionStartActivity<MainActivity>(
                    actionParametersOf(
                        clickedWidgetTodoIdKey to todoItem.id
                    )
                )
            )
    ) {
        Text(
            text = todoItem.title
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        Text(
            text = "Description:\n${todoItem.description}"
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        Text(
            text = todoItem.priorityLevel
        )
    }
}