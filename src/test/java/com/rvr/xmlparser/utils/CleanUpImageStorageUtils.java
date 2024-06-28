package com.rvr.xmlparser.utils;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for cleaning up image storage after tests.
 */
public class CleanUpImageStorageUtils {

    public static void withCleanUpImageFolder(Resource imagesResourceDir, Runnable operation) {
        try {
            operation.run();
        } finally {
            deleteFolder(imagesResourceDir);
        }
    }

    private static void deleteFolder(Resource imagesResourceDir) {
        try {
            Path dirPath = Paths.get(imagesResourceDir.getFile().getAbsolutePath());

            Files.walk(dirPath)
                    .filter(p -> !p.equals(dirPath))
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
