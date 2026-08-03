package com.momora.ai.data.repository

import android.util.Log
import com.momora.ai.data.network.MomoraApi
import com.momora.ai.data.network.dto.TextIngestRequestDto
import com.momora.ai.data.scanner.FileScanner
import com.momora.ai.domain.repository.MemoryRepository
import com.momora.ai.domain.repository.ScanProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryRepositoryImpl @Inject constructor(
    private val api: MomoraApi
) : MemoryRepository {

    private val scanner = FileScanner()
    private val TAG = "MemoryRepo"

    override suspend fun scanAndUploadFiles(
        rootDir: File,
        onProgress: (ScanProgress) -> Unit
    ): ScanProgress = withContext(Dispatchers.IO) {

        // Step 1: Scan the device
        Log.d(TAG, "Starting scan at: ${rootDir.absolutePath}")
        val scanResult = scanner.scan(rootDir)

        Log.d(TAG, "Scan complete: ${scanResult.safeFiles.size} safe files, " +
                "${scanResult.skippedSensitive.size} sensitive files skipped")
        
        scanResult.skippedSensitive.forEach { name ->
            Log.d(TAG, "  SKIPPED (sensitive): $name")
        }

        var uploaded = 0

        onProgress(ScanProgress(
            totalFound = scanResult.safeFiles.size,
            uploaded = 0,
            skippedSensitive = scanResult.skippedSensitive.size,
            currentFile = "Scanning complete. Starting upload...",
        ))

        // Step 2: Upload each safe file
        for (file in scanResult.safeFiles) {
            try {
                onProgress(ScanProgress(
                    totalFound = scanResult.safeFiles.size,
                    uploaded = uploaded,
                    skippedSensitive = scanResult.skippedSensitive.size,
                    currentFile = file.name,
                ))

                val ext = file.extension.lowercase()

                if (ext == "pdf" || ext == "docx" || ext == "doc") {
                    // Upload binary files via multipart
                    val mediaType = when (ext) {
                        "pdf" -> "application/pdf"
                        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        "doc" -> "application/msword"
                        else -> "application/octet-stream"
                    }
                    val requestBody = file.asRequestBody(mediaType.toMediaTypeOrNull())
                    val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    val sourceType = "DeviceScan".toRequestBody("text/plain".toMediaTypeOrNull())
                    val userId = "default_user".toRequestBody("text/plain".toMediaTypeOrNull())
                    val category = "General".toRequestBody("text/plain".toMediaTypeOrNull())

                    val response = api.uploadDocument(filePart, sourceType, userId, category)
                    Log.d(TAG, "Uploaded ${file.name}: ${response.numChunks} chunks")

                } else if (ext == "txt" || ext == "md") {
                    // Read text files and send as JSON
                    val text = file.readText(Charsets.UTF_8)
                    if (text.isNotBlank()) {
                        val response = api.ingestText(
                            TextIngestRequestDto(
                                text = text,
                                sourceName = file.name,
                                sourceType = "DeviceScan"
                            )
                        )
                        Log.d(TAG, "Ingested ${file.name}: ${response.numChunks} chunks")
                    }
                }

                uploaded++

            } catch (e: Exception) {
                Log.e(TAG, "Failed to upload ${file.name}: ${e.message}")
                // Continue to next file, don't stop the scan
            }
        }

        val finalProgress = ScanProgress(
            totalFound = scanResult.safeFiles.size,
            uploaded = uploaded,
            skippedSensitive = scanResult.skippedSensitive.size,
            currentFile = "",
            isComplete = true
        )
        onProgress(finalProgress)
        finalProgress
    }
}
