package cn.iwakeup.r2client.ui.screens.upload

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.ui.components.UploadFileItem
import cn.iwakeup.r2client.ui.components.drop.DropComponent


@Composable
fun UploadScreen(bucketBasicInfoList: List<BucketBasicInfo>, viewModel: UploadViewModel) {
    val fileState = remember { viewModel.fileState }
    val fileList = remember { viewModel.fileList }

    val selectedBucket by remember { viewModel.selectedBucket }

    if (selectedBucket == null) {
        bucketBasicInfoList.getOrNull(0)?.let {
            viewModel.selectBucket(it)
        }
    } else {
        Box(modifier = Modifier.padding(20.dp).fillMaxSize()) {
            DropComponent(fileState.value, {
                viewModel.dropFile(it)
            }, bucketBasicInfoList, selectedBucket!!, {
                viewModel.selectBucket(it)
            }) {

                LazyColumn {
                    items(fileList.size, {
                        fileList[it].hashCode()
                    }) {
                        UploadFileItem(
                            fileList[it],
                            onCopyLink = { file ->
                                viewModel.copyLink(file)
                            },
                            onRemoveItem = { file ->
                                viewModel.removeFile(file)
                            })
                    }
                }

            }

            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = {
                    viewModel.uploadToR2()
                },
            ) {
                Icon(Icons.Filled.Rocket, "Floating action button.")
            }
        }
    }

}