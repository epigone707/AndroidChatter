package edu.umich.yanfuguo.kotlinChatter

import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.reflect.full.declaredMemberProperties

// ChattStore is the Model of our app
object ChattStore {
    val chatts = arrayListOf<Chatt?>()
    private val nFields = Chatt::class.declaredMemberProperties.size

    private lateinit var queue: RequestQueue
    private const val serverUrl = "https://3.144.110.108/"

    private val client = OkHttpClient()

    fun postChatt(context: Context, chatt: Chatt, imageUri: Uri?, videoUri: Uri?,
                  completion: (String) -> Unit) {
        val mpFD = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", chatt.username ?: "")
            .addFormDataPart("message", chatt.message ?: "")

        imageUri?.run {
            toFile(context)?.let {
                mpFD.addFormDataPart("image", "chattImage",
                    it.asRequestBody("image/jpeg".toMediaType()))
            } ?: context.toast("Unsupported image format")
        }

        videoUri?.run {
            toFile(context)?.let {
                mpFD.addFormDataPart("video", "chattVideo",
                    it.asRequestBody("video/mp4".toMediaType()))
            } ?: context.toast("Unsupported video format")
        }

        val request = Request.Builder()
            .url(serverUrl+"postimages/")
            .post(mpFD.build())
            .build()

        context.toast("Posting . . . wait for 'Chatt posted!'")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completion(e.localizedMessage ?: "Posting failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    completion("Chatt posted!")
                }
            }
        })
    }

    fun getChatts(context: Context, completion: () -> Unit) {
        val getRequest = JsonObjectRequest(serverUrl+"getchatts/",
            { response ->
                chatts.clear()
                val chattsReceived = try { response.getJSONArray("chatts") } catch (e: JSONException) { JSONArray() }
                for (i in 0 until chattsReceived.length()) {
                    val chattEntry = chattsReceived[i] as JSONArray
                    if (chattEntry.length() == nFields) {
                        chatts.add(Chatt(username = chattEntry[0].toString(),
                            message = chattEntry[1].toString(),
                            timestamp = chattEntry[2].toString()))
                    } else {
                        Log.e("getChatts", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + nFields.toString())
                    }
                }
                completion()
            }, { completion() }
        )

        if (!this::queue.isInitialized) {
            queue = newRequestQueue(context)
        }
        queue.add(getRequest)
    }
}