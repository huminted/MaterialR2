// Kotlin
@file:OptIn(ExperimentalTestApi::class)

package cn.iwakeup.r2client.ui.components.drop

import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import cn.iwakeup.r2client.data.BucketBasicInfo
import org.junit.Rule
import org.junit.Test
import java.io.File

class DropBoxKtTestUITest {

    val buckets = listOf(
        BucketBasicInfo(
            bucketName = "bucket-1",
            createTime = "2020-0808",
            publicURL = null
        )
    )

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun dropComponent_rendersInstructionTexts() = runComposeUiTest {
        val fileState = mutableStateOf<File?>(null)

        val selected = buckets.first()

        setContent {
            DropComponent(
                file = fileState,
                onDropFile = { /* no-op */ },
                buckets = buckets,
                selectedBucket = selected,
                onSelectedChange = { /* no-op */ },
                fileList = {
                    // 用一个可识别的占位 UI 来代表文件列表
                    Text(
                        "FILE_LIST",
                        modifier = Modifier.testTag("fileList")
                    )
                }
            )
        }


        onNodeWithTag("fileList").assertDoesNotExist()


    }

    @Test
    fun dropComponent_showsFileList_whenFileIsProvided() = runComposeUiTest {
        val fileState = mutableStateOf<File?>(File("dummy.txt"))

        val selected = buckets.first()

        setContent {
            DropComponent(
                file = fileState,
                onDropFile = { /* no-op */ },
                buckets = buckets,
                selectedBucket = selected,
                onSelectedChange = { /* no-op */ },
                fileList = {
                    Text(
                        "FILE_LIST",
                        modifier = Modifier.testTag("fileList")
                    )
                }
            )
        }


        onNodeWithTag("fileList").assertIsDisplayed()
    }

    @Test
    fun dropComponent_toggleFile_showsAndHidesFileList() = runComposeUiTest {
        val fileState = mutableStateOf<File?>(null)

        val selected = buckets.first()

        setContent {
            DropComponent(
                file = fileState,
                onDropFile = { dropped -> fileState.value = dropped },
                buckets = buckets,
                selectedBucket = selected,
                onSelectedChange = { /* no-op */ },
                fileList = {
                    Text(
                        "FILE_LIST",
                        modifier = Modifier.testTag("fileList")
                    )
                }
            )
        }

        onNodeWithTag("fileList").assertDoesNotExist()

        runOnUiThread {
            fileState.value = File("dummy.txt")
        }
        waitForIdle()
        onNodeWithTag("fileList").assertIsDisplayed()

        runOnUiThread {
            fileState.value = null
        }
        waitForIdle()
        onNodeWithTag("fileList").assertDoesNotExist()
    }
}