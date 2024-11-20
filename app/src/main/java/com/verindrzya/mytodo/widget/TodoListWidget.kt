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
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.RadioButton
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
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
import androidx.glance.semantics.semantics
import androidx.glance.semantics.testTag
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

object TodoListActionParam {
    val clickedWidgetTodoIdKey = ActionParameters.Key<Int>(
        MainActivity.KEY_CLICKED_WIDGET_TODO_ID
    )
}

/**
 * The Widget itself
 */
class TodoListWidget : GlanceAppWidget(
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

            GlanceTheme {
                TodoListScreen(
                    todoList = todoData,
                    selectedPriorityLevel = selectedPriorityLevel,
                    onChangePriorityLevel = { selectedPriorityLevel = it }
                )
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
fun TodoListScreen(
    modifier: GlanceModifier = GlanceModifier,
    todoList: List<Todo>,
    selectedPriorityLevel: String,
    onChangePriorityLevel: (String) -> Unit
) {
    Scaffold(
        modifier = modifier
            .appWidgetBackground(),
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
                        color = ColorProvider(
                            day = Color.Black,
                            night = Color.White,
                        ),
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
                                onChangePriorityLevel(priorityLevel)
                            },
                            text = priorityLevel
                        )
                        Spacer(modifier = GlanceModifier.width(6.dp))
                    }
                }
            }
        }
    ) {
        if (todoList.isEmpty()) {
            EmptyView()
        } else {
            TodoListContent(
                todoList = todoList
            )
        }
    }
}

object TodoListTestTag {
    val composableTag = "todo-list-composable-tag"
}

@Composable
fun TodoListContent(
    todoList: List<Todo>,
    modifier: GlanceModifier = GlanceModifier,
) {
    LazyColumn(
        modifier = modifier
            .semantics { testTag = TodoListTestTag.composableTag }
            .fillMaxSize()
    ) {
        items(
            items = todoList,
            itemId = { todo -> todo.id.toLong() }
        ) { todo ->
            Column {
                TodoItem(
                    modifier = GlanceModifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    todoItem = todo
                )
                Spacer(GlanceModifier.height(5.dp))
            }
        }
    }
}

object TodoItemTestTag {
    val composableTag = "todo-item-composable-tag"
    val titleTag = "todo-item-title-tag"
    val descriptionTag = "todo-item-description-tag"
    val priorityLevelTag = "todo-item-priority-level-tag"
}

@Composable
fun TodoItem(
    todoItem: Todo,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier
            .semantics { testTag = TodoItemTestTag.composableTag }
            .fillMaxWidth()
            .cornerRadius(8.dp)
            .clickable(
                actionStartActivity<MainActivity>(
                    actionParametersOf(
                        TodoListActionParam.clickedWidgetTodoIdKey to todoItem.id
                    )
                )
            )
            .background(Color.Cyan)
    ) {
        Text(
            modifier = GlanceModifier
                .semantics { testTag = TodoItemTestTag.titleTag },
            text = todoItem.title
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        Text(
            modifier = GlanceModifier
                .semantics { testTag = TodoItemTestTag.descriptionTag },
            text = "Description:\n${todoItem.description}"
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        Text(
            modifier = GlanceModifier
                .semantics { testTag = TodoItemTestTag.priorityLevelTag },
            text = todoItem.priorityLevel
        )
    }
}

object EmptyViewTestTag {
    val composableTag = "empty-todo-list-composable-tag"
}

@Composable
fun EmptyView(
    modifier: GlanceModifier = GlanceModifier
) {
    Box(
        modifier = modifier
            .semantics {
                testTag = EmptyViewTestTag.composableTag
            }
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