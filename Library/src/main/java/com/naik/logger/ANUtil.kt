package com.naik.logger

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import com.naik.logger.exception.ANLoggerException
import com.naik.logger.filesharing.ANFileShare
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


internal object ANUtil {
    fun getRealSubject(context: Context, subject: String): String {
        return context.getAppVersionName()?.let { "$subject (v$it)" } ?: subject
    }

    private fun Context.getAppVersionName(): String? {
        return try {
            val info = packageManager.getPackageInfo(packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun copyFileToCache(context: Context?, fileShareObj: ANFileShare): String {

        val cacheFileName = fileShareObj.cacheFileName

        //This is an very important to share the file
        if (context != null) {
            copyFile(
                context,
                fileShareObj.uri,
                cacheFileName,
                fileShareObj.checkIfFileIsInCache
            )
        }else{
            throw ANLoggerException("Wow, context is null")
        }

        return cacheFileName
    }

    fun copyFile(
        context: Context,
        fileToCopy: Uri?,
        cacheFileName: String?,
        checkIfFileIsInCache: Boolean
    ): Uri? {

        // 1) check if file is already in cache
        if (checkIfFileIsInCache && isFileInCache(context, fileToCopy)) {
            return fileToCopy
        }

        // 2) else copy the file to the cache directory
        val cacheFile = File(context.cacheDir, cacheFileName)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
        fileToCopy?.let { copy(context, it, cacheFile) }
            ?: try {
                cacheFile.createNewFile()
            } catch (e: IOException) {
            }
        return Uri.fromFile(cacheFile)
    }

    private fun isFileInCache(context: Context, fileUri: Uri?): Boolean {
        return try {
            val file = File(fileUri!!.path)
            file.parentFile.absolutePath == context.cacheDir.absolutePath
        } catch (e: Exception) {
            false
        }
    }

    private fun copy(context: Context, src: Uri, dst: File): Boolean {
        return try {
            val content = context.contentResolver
            val `in` = content.openInputStream(src)
            val out: OutputStream = FileOutputStream(dst)
            copy(`in`, out)
        } catch (e: IOException) {
            false
        }
    }

    private fun copy(`in`: InputStream?, out: OutputStream?): Boolean {
        var success = false
        try {
            // Transfer bytes from in to out
            val buf = ByteArray(1024)
            var len: Int
            while (`in`!!.read(buf).also { len = it } > 0) {
                out!!.write(buf, 0, len)
            }
            success = true
        } catch (e: IOException) {
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                }
            }
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                }
            }
        }
        return success
    }
}