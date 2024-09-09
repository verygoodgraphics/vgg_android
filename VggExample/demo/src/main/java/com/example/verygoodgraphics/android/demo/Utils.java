package com.example.verygoodgraphics.android.demo;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.nio.file.Files;

public class Utils {

    private Utils() {
    }

    /**
     * Throws a throwable without declaring it in the method signature.
     *
     * @param t the throwable to throw, may be any type of throwable, must not be null
     * @return never returns, always throws the given throwable
     * @throws T the type of the throwable
     */
    public static <T extends Throwable> AssertionError unsafeThrow(@NonNull Throwable t) throws T {
        throw (T) t;
    }

    public static File copyAssetsToCache(@NonNull Context ctx, @NonNull String assetsPath) {
        File cacheDir = ctx.getCacheDir();
        File file = new File(cacheDir, assetsPath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (java.io.InputStream in = ctx.getAssets().open(assetsPath);
                     java.io.OutputStream out = Files.newOutputStream(file.toPath())) {
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                }
            } catch (java.io.IOException e) {
                throw unsafeThrow(e);
            }
        }
        return file;
    }

}
