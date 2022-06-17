package com.example.githubtests

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubtests.view.search.MainActivity
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

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if (BuildConfig.TYPE == MainActivity.FAKE) {
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 42")))
        } else {
            onView(isRoot()).perform(delay())
            //Внимание!
            //Перед тем как запускать тест, убедитесь в правильном количестве репозиториев. Скорее всего
            //оно будет меняться, несмотря на редкость языка (Algol).
            onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 3080")))
        }
    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()

            override fun getDescription(): String = "wait for $2 seconds"

            override fun perform(uiController: UiController, view: View?) {
                //perform() занимается непосредственно Action’ом: нас интересует UiController, у
                //которого есть нужный нам метод. Мы “замораживаем” UI на 2 секунды. Этого
                //достаточно для того, чтобы асинхронный запрос через Retrofit отправился и вернулся с
                //ответом. Если у вас нестабильное или слабое соединение, можно увеличить время
                //ожидания, но имейте в виду, что время выполнения теста увеличится на
                //соответствующее время.
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}