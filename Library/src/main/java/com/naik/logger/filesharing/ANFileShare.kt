package com.naik.logger.filesharing

import android.net.Uri
import java.io.File


internal interface IANFileShare {
    val uri: Uri
    val cacheFileName: String
    val checkIfFileIsInCache: Boolean
}

internal sealed class ANFileShare : IANFileShare {

    class DefaultName(override val uri: Uri) : ANFileShare() {
        constructor(file: File) : this(Uri.fromFile(file))

        override val cacheFileName: String = File(uri.path).name

        override val checkIfFileIsInCache = true
    }

}