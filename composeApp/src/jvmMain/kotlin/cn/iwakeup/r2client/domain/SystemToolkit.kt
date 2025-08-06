package cn.iwakeup.r2client.domain

import com.iwakeup.r2client.data.BucketFullInfo
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


object SystemToolkit {
    fun copyObjectLink(bucketFullInfo: BucketFullInfo, objectKey: String) {
        bucketFullInfo.publicURL?.let { publicURL ->
            copyObjectLink(publicURL, objectKey)
        }
    }

    fun copyObjectLink(publicURL: String, objectKey: String) {
        val fullURL = if (publicURL.endsWith("/")) {
            "$publicURL$objectKey"
        } else {
            "$publicURL/$objectKey"
        }
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(fullURL), null)
    }
}


