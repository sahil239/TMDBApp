package dev.sahildesai.tmdbapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain


/**
 * Sets the main coroutines dispatcher to a [TestDispatcher] for unit testing.
 */
@ExperimentalCoroutinesApi
class TMDBCoroutineRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        // This method will be called before each test starts
        Dispatchers.setMain(testDispatcher)
        println("Starting test: ${description?.methodName}")
    }

    override fun finished(description: Description?) {
        super.finished(description)
        // This method will be called after each test finishes
        Dispatchers.resetMain()
        println("Finished test: ${description?.testClass} ${description?.methodName}")
    }

    override fun failed(e: Throwable?, description: Description?) {
        super.failed(e, description)
        // This method will be called if a test fails
        println("Test failed: ${description?.methodName}, Reason: ${e?.message}")
    }

    override fun succeeded(description: Description?) {
        super.succeeded(description)
        // This method will be called if a test succeeds
        println("Test passed: ${description?.methodName}")
    }

}