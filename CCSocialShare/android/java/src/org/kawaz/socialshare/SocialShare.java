package org.kawaz.socialshare;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.cocos2dx.lib.Cocos2dxActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SocialShare {
    static public void postToTwitter(String message, String imagePath) {
        String url = "http://twitter.com/share?text=" + message;
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (!imagePath.equals("")) {
            try {
                Uri originalImageUri = Uri.parse(imagePath);
                String ext = getExtension(originalImageUri);
                Uri readableFileUri = saveImageToExternalDirectory(originalImageUri);
                if (readableFileUri != null) {
                    intent.setType("image/" + ext);
                    intent.putExtra(Intent.EXTRA_STREAM, readableFileUri);
                }
            } catch (Exception e) {
            }
        }
        Context context = Cocos2dxActivity.getContext();
        context.startActivity(intent);
    }

    static private String getExtension(Uri imagePath) {
        String pathString = imagePath.getPath();
        return pathString.substring(pathString.lastIndexOf(".") + 1);
    }

    static private String getFilename(Uri imagePath) {
        return imagePath.getLastPathSegment();
    }

    static private Uri saveImageToExternalDirectory(Uri imageUri) {
        File dst = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getFilename(imageUri));
        File src = new File(imageUri.getPath());
        try {
            copyFile(src, dst);
        } catch (Exception e) {
            return null;
        }
        return Uri.fromFile(dst);
    }

    static public void copyFile(File src, File dst) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
