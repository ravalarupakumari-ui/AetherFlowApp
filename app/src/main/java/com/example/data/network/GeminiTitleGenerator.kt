package com.example.data.network

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiGeneratedMetadata(
    val title: String,
    val description: String,
    val tags: String
)

object GeminiTitleGenerator {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun generateMetadata(promptTopic: String): AiGeneratedMetadata = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getFallbackMetadata(promptTopic)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val promptText = """
                You are a YouTube viral video automation AI.
                Topic: "$promptTopic".
                Generate a JSON object with 3 keys:
                "title": an engaging, viral YouTube title max 70 chars.
                "description": a SEO optimized 3-sentence description with hashtags.
                "tags": a comma-separated list of 8 relevant tags.
                Return ONLY valid JSON without markdown wrapping.
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply { put("text", promptText) })
                        })
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text", "")
                        val cleanedText = text.replace("```json", "").replace("```", "").trim()
                        val parsed = JSONObject(cleanedText)
                        return@withContext AiGeneratedMetadata(
                            title = parsed.optString("title", "Futuristic AI Explained: $promptTopic"),
                            description = parsed.optString("description", "Explore the latest breakthrough in $promptTopic. #AI #Automation #AetherFlow"),
                            tags = parsed.optString("tags", "$promptTopic, AI, Automation, Tech, Future, Innovation, Shorts, Viral")
                        )
                    }
                }
            }
            getFallbackMetadata(promptTopic)
        } catch (e: Exception) {
            getFallbackMetadata(promptTopic)
        }
    }

    private fun getFallbackMetadata(promptTopic: String): AiGeneratedMetadata {
        val topicClean = if (promptTopic.isBlank()) "Futuristic AI Workflow" else promptTopic
        return AiGeneratedMetadata(
            title = "🚀 How $topicClean Works: The 2026 AI Automation Breakthrough",
            description = "Unveiling the next generation of $topicClean using liquid glass automation pipelines. Discover how creators publish 10x faster with AI.\n\n#AetherFlow #$topicClean #Tech2026",
            tags = "AI Automation, $topicClean, YouTube Growth, Content Creation, Future Tech, Liquid Glass, Productivity, 2026 Trends"
        )
    }
}
