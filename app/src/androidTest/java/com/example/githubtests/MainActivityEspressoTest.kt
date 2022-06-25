package com.example.githubtests

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubtests.view.search.MainActivity
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test //проверка на существование активити
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test //Проверка на прохождение в активити метода onResume()
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test // Проверка наличия элемента с id "searchEditText"
    fun activity_SearchEditText_NotNull() {
        scenario.onActivity {
            val editText = it.findViewById<EditText>(R.id.searchEditText)
            TestCase.assertNotNull(editText)
        }
    }

    @Test // Проверка наличия элемента с id "toDetailsActivityButton"
    fun activity_ToDetailsButton_NotNull() {
        scenario.onActivity {
            val toDetailsButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            TestCase.assertNotNull(toDetailsButton)
        }
    }

    @Test // Проверка наличия элемента с id "totalCountTextView"
    fun activity_TotalCountTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test // Проверка наличия элемента с id "recyclerView"
    fun activity_RecyclerView_NotNull() {
        scenario.onActivity {
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            TestCase.assertNotNull(recyclerView)
        }
    }

    @Test // Проверка на невидимость элемента с id "totalCountTextView", перед запросом к API
    fun activity_TotalCountTextView_IsInvisible() {
        onView(withId(R.id.totalCountTextView)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test // Проверка на видимость элемента с id "totalCountTextView", после выполнения запроса к API
    fun activity_TotalCountTextView_IsVisible() {
        onView(withId(R.id.searchEditText)).perform(click())
            .perform(replaceText(TEST_SOME_SEARCH_QUERY_ALGOL), closeSoftKeyboard())
            .perform(pressImeActionButton())
        onView(isRoot()).perform(delay())
        onView(withId(R.id.totalCountTextView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test // Проверка частичного отображения элемента с id "searchEditText"
    fun activity_SearchEditText_isDisplayed() {
        onView(withId(R.id.searchEditText)).check((matches(isDisplayed())))
    }

    @Test // Проверка отображения корректного текста с подсказкой на элементе с id "searchEditText"
    fun activity_EditTextIsCorrectHintText() {
        onView(withId(R.id.searchEditText)).check(matches(withHint(TEST_HINT_EDITTEXT)))
    }

    @Test //Проверка отображения корректного текста на кнопке с id "toDetailsActivityButton"
    fun activity_ButtonToDetails_IsCorrectText() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText(TEST_TO_DETAILS_BUTTON_TEXT)))
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText(TEST_SOME_SEARCH_QUERY_ALGOL), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if (BuildConfig.TYPE == FAKE_BUILDCONFIG) {
            onView(withId(R.id.totalCountTextView)).check(matches(withText(FAKE_RESULTS)))
        } else {
            onView(isRoot()).perform(delay())
            //Внимание!
            //Перед тем как запускать тест, убедитесь в правильном количестве репозиториев. Скорее всего
            //оно будет меняться, несмотря на редкость языка (Algol).
            onView(withId(R.id.totalCountTextView)).check(matches(withText(TEST_SOME_SEARCH_RESULTS)))
        }
    }

    @Test
    fun activity_ButtonToDetails_AreEffectiveVisible() {
        onView(withId(R.id.toDetailsActivityButton)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
    }

    @Test
    fun activity_ButtonToDetails_IsWorking() {
        onView(withId(R.id.toDetailsActivityButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.totalCountTextView)).check(matches(withText(TEST_NUMBER_OF_RESULTS_ZERO)))
    }

    // Функция для реализации задержки
    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()

            override fun getDescription(): String = TEST_DELAY_DESCRIPTION

            /** perform() занимается непосредственно Action’ом: нас интересует UiController, у
            которого есть нужный нам метод. Мы “замораживаем” UI на 2 секунды. Этого
            достаточно для того, чтобы асинхронный запрос через Retrofit отправился и вернулся с
            ответом. Если у вас нестабильное или слабое соединение, можно увеличить время
            ожидания, но имейте в виду, что время выполнения теста увеличится на
            соответствующее время.*/
            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}