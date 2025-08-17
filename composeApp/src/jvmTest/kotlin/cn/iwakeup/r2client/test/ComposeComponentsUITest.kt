package cn.iwakeup.r2client.test

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.runComposeUiTest
import cn.iwakeup.r2client.data.BucketBasicInfo
import org.junit.Assert.assertTrue
import org.junit.Rule
import java.io.File
import kotlin.test.Test

class ComposeComponentsUITest {
    @get:Rule
    val rule = createComposeRule()


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest() = runComposeUiTest {

        rule.setContent {

            // Arrange
            val droppedFile = File("dummy.txt")
            val onDropFileCalled = mutableStateOf(false)
            val onDropFile = { file: File ->
                if (file == droppedFile) {
                    onDropFileCalled.value = true
                }
            }
            val buckets = listOf(BucketBasicInfo("TestBucket", "2025-08-15", "http://testbucket.url"))
            val selectedBucket = buckets[0]

            // Act


//            DropBox(onDropFile, buckets, selectedBucket, {})


            onDropFile(droppedFile)

            // Assert
            assertTrue(onDropFileCalled.value)

        }
    }
}