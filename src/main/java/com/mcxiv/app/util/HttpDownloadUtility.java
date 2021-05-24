package com.mcxiv.app.util;

import com.mcxiv.app.ui.IDENTITY;
import games.rednblack.h2d.common.ProgressHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpDownloadUtility {

    private static final int BUFFER_SIZE = 4096;

    public static void downloadFile(String fileURL, String saveDir, ProgressHandler progressHandler) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(saveDir);
        downloadTo(fileURL, outputStream, progressHandler);
    }

    public static String downloadToString(String fileURL) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        downloadTo(fileURL, outputStream, IDENTITY.progressHandler);
        return outputStream.toString(StandardCharsets.UTF_8);
    }

    public static void downloadTo(String urlPath, OutputStream outputStream, ProgressHandler progressHandler) throws IOException {

        var url = new URL(urlPath);
        var connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(true);

        // always check HTTP response code first
        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            progressHandler.progressFailed();
            return;
        }

        float lengthFactor = 100f / connection.getContentLength();

        // opens input stream from the HTTP connection
        InputStream inputStream = connection.getInputStream();

        // opens an output stream to save into file
        int currentBytes = 0;
        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            currentBytes += bytesRead;

            progressHandler.progressChanged(currentBytes * lengthFactor);

            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();

        progressHandler.progressComplete();

        connection.disconnect();
    }

}
