package com.verindrzya.mytodo

import android.content.ComponentName
import android.content.Context
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.testing.unit.runGlanceAppWidgetUnitTest
import androidx.glance.testing.unit.hasStartActivityClickAction
import androidx.glance.testing.unit.hasTestTag
import androidx.glance.testing.unit.hasText
import com.verindrzya.mytodo.constant.PriorityLevel
import com.verindrzya.mytodo.data.database.Todo
import com.verindrzya.mytodo.widget.TodoItem
import com.verindrzya.mytodo.widget.TodoItemTestTag
import com.verindrzya.mytodo.widget.TodoListActionParam
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TodoListGlanceUnitTest {

    lateinit var dummyContext: Context

    private val dummyTodoItem = Todo(
        id = 1,
        title = "Dummy item title",
        description = "Dummy item description",
        priorityLevel = PriorityLevel.High.name
    )

    @Before
    fun setup() {
        dummyContext = RuntimeEnvironment.getApplication()
    }

    @Test
    fun properTodoItemText() = runGlanceAppWidgetUnitTest {
        provideComposable {
            TodoItem(
                todoItem = dummyTodoItem
            )
        }

        onNode(hasTestTag(TodoItemTestTag.titleTag))
            .assert(hasText(dummyTodoItem.title))
        onNode(hasTestTag(TodoItemTestTag.descriptionTag))
            .assert(hasText("Description:\n${dummyTodoItem.description}"))
        onNode(hasTestTag(TodoItemTestTag.priorityLevelTag))
            .assert(hasText(dummyTodoItem.priorityLevel))
    }

    @Test
    fun todoItemComposableHasProperActionStartActivity() = runGlanceAppWidgetUnitTest {
        setContext(dummyContext)
        provideComposable {
            TodoItem(todoItem = dummyTodoItem)
        }

        onNode(hasTestTag(TodoItemTestTag.composableTag))
            .assert(hasStartActivityClickAction(
                componentName = ComponentName(
                    "com.verindrzya.mytodo",
                    MainActivity::class.java.name
                ),
                parameters = actionParametersOf(
                    TodoListActionParam.clickedWidgetTodoIdKey to dummyTodoItem.id
                )
            ))
    }

}