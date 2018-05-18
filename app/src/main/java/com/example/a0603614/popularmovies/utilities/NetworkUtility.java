package com.example.a0603614.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by A0603614 on 5/18/18.
 */

public class NetworkUtility {
    private static final String TAG = NetworkUtility.class.getSimpleName();


    public static String getResponseFromHTTPUrl(Uri sortUri) throws IOException {
        Log.i(TAG, "getResponseFromHTTPUrl: Starting HTTP request");
        // Create a URL from the sortUri
        URL url = new URL(sortUri.toString());

        // Create a HTTP connection using the supplied URL
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            // Open the input stream from the connection
            InputStream stream = urlConnection.getInputStream();

            // Set up the scanner to iterate over the stream
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");

            // Check that there is content to iterate in the stream
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
