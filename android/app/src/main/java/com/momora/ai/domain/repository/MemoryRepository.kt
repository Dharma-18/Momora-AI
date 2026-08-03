package com.momora.ai.domain.repository

import com.momora.ai.data.scanner.FileScanner
import java.io.File

data class ScanProgress(
    val totalFound: Int = 0,
    val uploaded: Int = 0,
    val skippedSensitive: Int = 0,
    val currentFile: String = "",
    val isComplete: Boolean = false,
    val error: String? = null
)

interface MemoryRepository {
    suspend fun scanAndUploadFiles(
        rootDir: File,
        onProgress: (ScanProgress) -> Unit
    ): ScanProgress
}
