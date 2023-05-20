package com.naik.logger.fileprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.File
import java.io.FileNotFoundException


class ANCachedFileProvider : ContentProvider() {
    var authority: String? = null
    private var uriMatcher: UriMatcher? = null
    override fun onCreate(): Boolean {
        authority = getAuthority(context)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher!!.addURI(authority, "*", 1)
        return true
    }

    @Throws(FileNotFoundException::class)
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        return when (uriMatcher!!.match(uri)) {
            1 -> {
                val fileLocation =
                    context!!.cacheDir.toString() + File.separator + uri.lastPathSegment
                ParcelFileDescriptor.open(
                    File(fileLocation),
                    ParcelFileDescriptor.MODE_READ_ONLY
                )
            }

            else -> throw FileNotFoundException("Unsupported uri: $uri")
        }
    }

    override fun update(
        uri: Uri,
        contentvalues: ContentValues?,
        s: String?,
        `as`: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, s: String?, `as`: Array<String>?): Int {
        return 0
    }

    override fun insert(uri: Uri, contentvalues: ContentValues?): Uri? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        s: String?,
        as1: Array<String>?,
        s1: String?
    ): Cursor? {
        return null
    }

    companion object {
        fun getAuthority(context: Context?): String {
            return context!!.packageName + ".CachedFileProvider"
        }

        fun getCacheFileUri(context: Context?, file: String): Uri {
            return Uri.parse("content://" + getAuthority(context) + "/" + file)
        }
    }
}