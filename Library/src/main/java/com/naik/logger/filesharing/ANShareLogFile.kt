package com.naik.logger.filesharing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import com.naik.logger.ANUtil
import com.naik.logger.fileprovider.ANCachedFileProvider

internal class ANShareLogFile(
    private val receivers: List<String>,
    private val subject: String,
    private val text: String? = null,
    private val textIsHtml: Boolean = false,
    private val attachments: List<ANFileShare> = emptyList()
) {
    private fun buildIntent(
        context: Context,
        chooserTitle: String
    ): Intent {
        val single = attachments.size == 1
        val intent = Intent(if (single) Intent.ACTION_SEND else Intent.ACTION_SEND_MULTIPLE)
        intent.type = if (single) "message/rfc822" else "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, receivers.toTypedArray<String>())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (text != null) {
            intent.putExtra(Intent.EXTRA_TEXT, if (textIsHtml) Html.fromHtml(text) else text)
        }

        if (attachments.size == 1) {
            val cacheFileName = ANUtil.copyFileToCache(context, attachments[0])
            intent.putExtra(
                Intent.EXTRA_STREAM,
                ANCachedFileProvider.getCacheFileUri(context, cacheFileName)
            )
        } else if (attachments.size > 1) {
            val uris = ArrayList<Uri>()
            for (i in attachments.indices) {
                val cacheFileName = ANUtil.copyFileToCache(context, attachments[i])
                uris.add(ANCachedFileProvider.getCacheFileUri(context, cacheFileName))
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        }

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        return Intent.createChooser(intent, chooserTitle).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun startEmailChooser(context: Context, chooserTitle: String) {
        val intent = buildIntent(context, chooserTitle)
        context.startActivity(intent)
    }
}