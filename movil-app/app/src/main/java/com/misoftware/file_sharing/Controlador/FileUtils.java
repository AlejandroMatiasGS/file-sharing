package com.misoftware.file_sharing.Controlador;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.NonNull;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static String getFileNameFromUri(@NonNull Context context, @NonNull Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {OpenableColumns.DISPLAY_NAME};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            cursor.close();
            return displayName;
        } else {
            Log.e(TAG, "Cursor is null or could not move to first.");
            return null;
        }
    }

    public static long getFileSizeFromUri(@NonNull Context context, @NonNull Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {OpenableColumns.SIZE};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int sizeIndex = cursor.getColumnIndexOrThrow(OpenableColumns.SIZE);
            long size = cursor.getLong(sizeIndex);
            cursor.close();
            return size;
        } else {
            Log.e(TAG, "Cursor is null or could not move to first.");
            return 0;
        }
    }
}
