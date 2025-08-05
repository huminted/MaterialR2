package cn.iwakeup.r2client.ui.components.drop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import java.awt.datatransfer.DataFlavor
import java.io.File

@ExperimentalComposeUiApi
class FileDropTarget(
    val onDropFile: (singleFile: File) -> Unit,
    val onDropDirectory: (singleFile: File) -> Unit
) : DragAndDropTarget {

    override fun onDrop(event: DragAndDropEvent): Boolean {
        val fileList = event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor) as List<*>

        for (file in fileList) {
            file as File
            if (file.isDirectory) {
                println(file.absoluteFile)
                onDropFile(file)
            } else {
                onDropDirectory(file)
            }
        }

        return true
    }
}