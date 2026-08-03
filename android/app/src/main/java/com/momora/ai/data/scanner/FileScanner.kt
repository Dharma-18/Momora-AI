package com.momora.ai.data.scanner

import java.io.File

/**
 * Smart File Scanner that recursively scans device storage for safe,
 * non-sensitive documents. Filters by extension and a blacklist of
 * sensitive keywords in file names.
 */
class FileScanner {

    companion object {
        // File types Momora cares about
        val ALLOWED_EXTENSIONS = setOf("pdf", "txt", "md", "docx", "doc")

        // Sensitive keywords — files containing these words in their name are IGNORED
        val SENSITIVE_KEYWORDS = setOf(
            "password", "passwd", "bank", "statement", "tax",
            "confidential", "private", "payslip", "invoice",
            "salary", "credit", "debit", "aadhaar", "aadhar",
            "pan", "passport", "license", "otp", "pin",
            "secret", "finance", "loan", "emi", "insurance",
            "medical", "prescription", "diagnosis", "health"
        )

        // Directories we should never scan (system, caches, app internals)
        val IGNORED_DIRECTORIES = setOf(
            "Android", ".thumbnails", ".cache", "cache",
            "com.", "org.", "net.", ".git", "node_modules",
            "DCIM", "Pictures", "Movies", "Music", "Ringtones",
            "Alarms", "Notifications", "Podcasts"
        )
    }

    data class ScanResult(
        val safeFiles: List<File>,
        val skippedSensitive: List<String>,
        val skippedExtension: Int,
        val totalScanned: Int
    )

    /**
     * Recursively scans the given root directory for safe document files.
     *
     * @param rootDir The root directory to scan (e.g., /storage/emulated/0)
     * @return A ScanResult containing safe files and stats about skipped files
     */
    fun scan(rootDir: File): ScanResult {
        val safeFiles = mutableListOf<File>()
        val skippedSensitive = mutableListOf<String>()
        var skippedExtension = 0
        var totalScanned = 0

        fun walk(dir: File) {
            val children = dir.listFiles() ?: return

            for (file in children) {
                if (file.isDirectory) {
                    // Skip system and media directories
                    val dirName = file.name
                    val shouldIgnore = IGNORED_DIRECTORIES.any { ignored ->
                        dirName.equals(ignored, ignoreCase = true) ||
                                dirName.startsWith(ignored, ignoreCase = true)
                    }
                    if (!shouldIgnore) {
                        walk(file)
                    }
                } else {
                    totalScanned++
                    val extension = file.extension.lowercase()

                    // Step 1: Only process allowed file types
                    if (extension !in ALLOWED_EXTENSIONS) {
                        skippedExtension++
                        continue
                    }

                    // Step 2: Check file name against sensitive keywords
                    val lowerName = file.nameWithoutExtension.lowercase()
                    val isSensitive = SENSITIVE_KEYWORDS.any { keyword ->
                        lowerName.contains(keyword)
                    }

                    if (isSensitive) {
                        skippedSensitive.add(file.name)
                    } else {
                        safeFiles.add(file)
                    }
                }
            }
        }

        walk(rootDir)

        return ScanResult(
            safeFiles = safeFiles,
            skippedSensitive = skippedSensitive,
            skippedExtension = skippedExtension,
            totalScanned = totalScanned
        )
    }
}
