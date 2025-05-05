package jp.co.zaico.codingtest

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import kotlin.jvm.java

/**
 * UIテスト用のRunner
 * https://developer.android.com/training/dependency-injection/hilt-testing?hl=ja#instrumented-tests
 */
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}