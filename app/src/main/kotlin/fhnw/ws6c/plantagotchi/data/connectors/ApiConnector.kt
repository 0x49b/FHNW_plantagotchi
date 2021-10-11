package fhnw.ws6c.plantagotchi.data.connectors

import android.graphics.BitmapFactory.decodeByteArray
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

/**
 * API Connector to get, post, put Data from a REST API & downloading images from a http resource
 */
class ApiConnector {

    private val TAG = "ApiConnector"

    /**
     * Get some JSON from an API
     */
    fun getJSONString(url: URL, token: String = ""): String {
        try {
            val conn = url.openConnection() as HttpsURLConnection

            if (!token.isEmpty()) {
                conn.setRequestProperty("Authorization", token)
            }

            conn.connect()

            val inputStream = BufferedInputStream(conn.inputStream)
            val br = BufferedReader(InputStreamReader(inputStream))

            val jsonData = StringBuffer()
            var line: String?

            do {
                line = br.readLine()
                if (line == null) {
                    break
                }
                jsonData.append(line + "n")
            } while (true)
            br.close()
            inputStream.close()
            return jsonData.toString()

        } catch (e: Exception) {
            return "{\"error\":\"${e}\", \"stacktrace\":\"${e.stackTrace}\"}"
        }
    }

    /**
     * Post or Put data to an http endpoint
     */
    fun postPutJsonData(url: URL, token: String = "", json: String, update: Boolean): String {
        val conn = url.openConnection() as HttpsURLConnection

        if (!token.isEmpty()) {
            conn.setRequestProperty("Authorization", token)
        }

        if (update) {
            conn.requestMethod = "PUT"
        } else {
            conn.requestMethod = "POST"
        }

        conn.connectTimeout = 300000
        conn.connectTimeout = 300000
        conn.doOutput = true


        val postData = json.toByteArray(StandardCharsets.UTF_8)

        conn.setRequestProperty("charset", "utf-8")
        conn.setRequestProperty("Content-length", postData.size.toString())
        conn.setRequestProperty("Content-Type", "application/json")

        try {
            val outputStream = DataOutputStream(conn.outputStream)
            outputStream.write(postData)
            outputStream.flush()
        } catch (exception: Exception) {
        }

        Log.d(TAG, "response message ${conn.responseMessage}")
        Log.d(TAG, "response code ${conn.responseCode}")


        if (conn.responseCode != HttpURLConnection.HTTP_OK && conn.responseCode != HttpURLConnection.HTTP_CREATED) {
            val inputStream = DataInputStream(conn.inputStream)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val output: String = reader.readLine()
            Log.d("upload", "There was error while connecting the chat $output")
        } else {
            val inputStream = DataInputStream(conn.inputStream)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val output: String = reader.readLine()
            Log.d("upload", "uploaded $output")
            return output
        }

        return ""
    }


    /**
     * Load Image from Internet
     */
    fun getImage(imageURL: URL): ImageBitmap {
        try {
            val conn = imageURL.openConnection() as HttpsURLConnection
            conn.connect()

            val inputStream = conn.inputStream
            val allBytes = inputStream.readBytes()
            inputStream.close()

            val bitmap = decodeByteArray(allBytes, 0, allBytes.size)

            return bitmap.asImageBitmap()
        } catch (e: Exception) {
            return ImageBitmap(10, 10)
        }
    }
}