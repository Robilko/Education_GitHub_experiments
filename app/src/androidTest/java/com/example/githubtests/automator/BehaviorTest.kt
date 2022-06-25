package com.example.githubtests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import com.example.githubtests.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    /** Класс UiDevice предоставляет доступ к вашему устройству. Именно через UiDevice вы можете управлять устройством, открывать приложения и находить нужные элементы на экране */
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    /**
     * Контекст нам понадобится для запуска нужных экранов и получения packageName
     */
    private val context = ApplicationProvider.getApplicationContext<Context>()

    /**
     * Путь к классам нашего приложения, которые мы будем тестировать
     */
    private val packageName = context.packageName

    @Before
    fun setup() {
        // Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        // Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)  //Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)

        // Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    /**
     * Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент и проверить его на null
     */
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText и проверяем на null
        val editText = uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_EDIT_TEXT))
        Assert.assertNotNull(editText)
    }

    /** Убеждаемся, что поиск работает как ожидается */
    @Test
    fun test_SearchIsPositive() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_EDIT_TEXT))
        //Устанавливаем значение
        editText.text = TEST_TEXT_UIAUTOMATOR
        //Через uiDevice находим кнопку searchButton и имитируем клик по ней
        val searchButton = uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_BUTTON))
        searchButton.click()
        //Ожидаем конкретного события: появления текстового поля totalCountTextView. Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText = uiDevice.wait(Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXTVIEW)), TIMEOUT)
        //Убеждаемся, что сервер вернул корректный результат. Обратите внимание, что количество
        // результатов может варьироваться во времени, потому что количество репозиториев постоянно меняется.
        Assert.assertNotNull(changedText)
    }

    @Test
    fun test_SearchIsNegative() {
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_BUTTON)).click()
        val changedText = uiDevice.wait(Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXTVIEW)), TIMEOUT)
        Assert.assertNull(changedText)
    }

    /** Убеждаемся, что DetailsScreen открывается */
    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails = uiDevice.findObject(By.res(packageName, RESOURCE_ID_TO_DETAILS_ACTIVITY_BUTTON))
        //Кликаем по ней
        toDetails.click()
        //Ожидаем конкретного события: появления текстового поля totalCountTextView. Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText = uiDevice.wait(Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXTVIEW)), TIMEOUT)
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        // Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_ZERO)
    }

    @Test //Тест на то что после успешного выполнения запроса и получения нужного количества репозиториев, DetailsScreen отображает именно это количество
    fun test_OpenDetailsScreenWithQuery() {
        val editText = uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_EDIT_TEXT))
        editText.text = TEST_SOME_SEARCH_QUERY
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_SEARCH_BUTTON)).click()
        uiDevice.wait(Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXTVIEW)), TIMEOUT)
        val changedText = changedTextOnDetailsScreen(uiDevice, packageName)
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_PLUS_1)
    }

    @Test //Тестируем кнопку "+" на экране DetailsScreen
    fun test_DetailsScreenPlusButton() {
        val changedText = changedTextOnDetailsScreen(uiDevice, packageName)
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_ZERO)
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_INCREMENT_BUTTON)).click()
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_PLUS_1)
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_INCREMENT_BUTTON)).click()
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_PLUS_2)
    }

    @Test //Тестируем кнопку "-" на экране DetailsScreen
    fun test_DetailsScreenMinusButton() {
        val changedText = changedTextOnDetailsScreen(uiDevice, packageName)
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_ZERO)
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_DECREMENT_BUTTON)).click()
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_MINUS_1)
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_DECREMENT_BUTTON)).click()
        Assert.assertEquals(changedText.text, TEST_NUMBER_OF_RESULTS_MINUS_2)
    }

    //Метод получения объекта totalCountTextView на экране DetailsScreen
    private fun changedTextOnDetailsScreen(uiDevice: UiDevice, packageName: String) : UiObject2 {
        uiDevice.findObject(By.res(packageName, RESOURCE_ID_TO_DETAILS_ACTIVITY_BUTTON)).click()
        uiDevice.wait(Until.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXTVIEW)), TIMEOUT)
        return uiDevice.findObject(By.res(packageName, RESOURCE_ID_TOTAL_COUNT_TEXTVIEW))
    }

}