package cn.iwakeup.r2client.test

import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.isNotSame
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JvmMainUtilsTest {


    @Test
    fun apiConfiguration_sameContent_returnTrue() {

        val accountId = "123"
        val accessKey = "456"
        val secretKey = "789"

        val secretKey_Different = "78910"

        val api = APIConfiguration(accountId, accessKey, secretKey)

        assertFalse { api.isNotSame(accountId, accessKey, secretKey) }

        assertTrue { api.isNotSame(accountId, accessKey, secretKey_Different) }

    }

}