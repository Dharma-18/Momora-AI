package com.momora.ai.presentation.memory

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momora.ai.domain.repository.MemoryRepository
import com.momora.ai.domain.repository.ScanProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ScanUiState(
    val isScanning: Boolean = false,
    val progress: ScanProgress = ScanProgress(),
    val hasPermission: Boolean = false
)

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: MemoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun updatePermissionStatus(granted: Boolean) {
        _uiState.update { it.copy(hasPermission = granted) }
    }

    fun startScan() {
        if (_uiState.value.isScanning) return

        _uiState.update { it.copy(isScanning = true, progress = ScanProgress()) }

        viewModelScope.launch {
            try {
                val rootDir = Environment.getExternalStorageDirectory()
                repository.scanAndUploadFiles(rootDir) { progress ->
                    _uiState.update { it.copy(progress = progress) }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        progress = it.progress.copy(
                            error = e.localizedMessage ?: "Unknown scanning error"
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isScanning = false) }
            }
        }
    }
}
