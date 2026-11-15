package com.example.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PreferredSlot
import com.example.data.PreferencesRepository
import com.example.data.ServiceCatalog
import com.example.data.ServiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingUiState(
    val customerName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val issueNotes: String = "",
    val selectedService: ServiceItem = ServiceCatalog.defaultService,
    val preferredSlot: PreferredSlot = PreferredSlot.MORNING,
    val rememberDetails: Boolean = true,
    val businessWhatsAppNumber: String = "",
    val nameError: Boolean = false,
    val phoneError: Boolean = false,
    val addressError: Boolean = false,
    val isSettingsDialogOpen: Boolean = false,
    val isPreviewSheetOpen: Boolean = false,
    val isFallbackDialogOpen: Boolean = false
) {
    val isFormValid: Boolean
        get() = customerName.isNotBlank() &&
            phoneNumber.count { it.isDigit() } >= 7 &&
            address.isNotBlank()

    fun buildFormattedMessage(): String = buildString {
        appendLine("SS SERVICES BOOKING")
        appendLine("Service: ${selectedService.title}")
        appendLine("Name: ${customerName.trim()}")
        appendLine("Phone: ${phoneNumber.trim()}")
        appendLine("Address: ${address.trim()}")
        append("Preferred Slot: ${preferredSlot.label}")
        if (issueNotes.isNotBlank()) {
            appendLine()
            append("Notes: ${issueNotes.trim()}")
        }
    }.trim()

    fun buildWhatsAppUrl(): String {
        val encoded = Uri.encode(buildFormattedMessage())
        val digitsOnly = businessWhatsAppNumber.filter { it.isDigit() }
        return if (digitsOnly.isNotEmpty()) {
            "https://wa.me/$digitsOnly?text=$encoded"
        } else {
            "https://wa.me/?text=$encoded"
        }
    }
}

class BookingViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initial = preferencesRepository.preferencesFlow.first()
            _uiState.update { state ->
                state.copy(
                    customerName = if (state.customerName.isEmpty()) initial.savedCustomerName else state.customerName,
                    phoneNumber = if (state.phoneNumber.isEmpty()) initial.savedPhoneNumber else state.phoneNumber,
                    address = if (state.address.isEmpty()) initial.savedAddress else state.address,
                    rememberDetails = initial.rememberDetails,
                    businessWhatsAppNumber = initial.businessWhatsAppNumber
                )
            }
            preferencesRepository.preferencesFlow.collect { prefs ->
                _uiState.update { state ->
                    state.copy(
                        businessWhatsAppNumber = prefs.businessWhatsAppNumber,
                        rememberDetails = prefs.rememberDetails
                    )
                }
            }
        }
    }

    fun onCustomerNameChanged(value: String) {
        _uiState.update {
            it.copy(
                customerName = value,
                nameError = if (it.nameError && value.isNotBlank()) false else it.nameError
            )
        }
    }

    fun onPhoneNumberChanged(value: String) {
        _uiState.update {
            it.copy(
                phoneNumber = value,
                phoneError = if (it.phoneError && value.count { ch -> ch.isDigit() } >= 7) false else it.phoneError
            )
        }
    }

    fun onAddressChanged(value: String) {
        _uiState.update {
            it.copy(
                address = value,
                addressError = if (it.addressError && value.isNotBlank()) false else it.addressError
            )
        }
    }

    fun onIssueNotesChanged(value: String) {
        _uiState.update { it.copy(issueNotes = value) }
    }

    fun onServiceSelected(service: ServiceItem) {
        _uiState.update { it.copy(selectedService = service) }
    }

    fun onPreferredSlotSelected(slot: PreferredSlot) {
        _uiState.update { it.copy(preferredSlot = slot) }
    }

    fun onRememberDetailsChanged(remember: Boolean) {
        _uiState.update { it.copy(rememberDetails = remember) }
        viewModelScope.launch {
            val current = _uiState.value
            preferencesRepository.saveCustomerDetails(
                name = current.customerName,
                phone = current.phoneNumber,
                address = current.address,
                remember = remember
            )
        }
    }

    fun validateAndPrepareBooking(): String? {
        val current = _uiState.value
        val nameInvalid = current.customerName.isBlank()
        val phoneInvalid = current.phoneNumber.count { it.isDigit() } < 7
        val addressInvalid = current.address.isBlank()

        if (nameInvalid || phoneInvalid || addressInvalid) {
            _uiState.update {
                it.copy(
                    nameError = nameInvalid,
                    phoneError = phoneInvalid,
                    addressError = addressInvalid
                )
            }
            return null
        }

        viewModelScope.launch {
            preferencesRepository.saveCustomerDetails(
                name = current.customerName,
                phone = current.phoneNumber,
                address = current.address,
                remember = current.rememberDetails
            )
        }
        return current.buildWhatsAppUrl()
    }

    fun openSettingsDialog() {
        _uiState.update { it.copy(isSettingsDialogOpen = true) }
    }

    fun dismissSettingsDialog() {
        _uiState.update { it.copy(isSettingsDialogOpen = false) }
    }

    fun saveBusinessWhatsAppNumber(rawNumber: String) {
        val normalized = rawNumber.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                businessWhatsAppNumber = normalized,
                isSettingsDialogOpen = false
            )
        }
        viewModelScope.launch {
            preferencesRepository.saveBusinessWhatsAppNumber(normalized)
        }
    }

    fun clearSavedCustomerDetails() {
        viewModelScope.launch {
            preferencesRepository.clearCustomerDetails()
            _uiState.update {
                it.copy(
                    customerName = "",
                    phoneNumber = "",
                    address = "",
                    nameError = false,
                    phoneError = false,
                    addressError = false
                )
            }
        }
    }

    fun openPreviewSheet() {
        val current = _uiState.value
        val nameInvalid = current.customerName.isBlank()
        val phoneInvalid = current.phoneNumber.count { it.isDigit() } < 7
        val addressInvalid = current.address.isBlank()
        if (nameInvalid || phoneInvalid || addressInvalid) {
            _uiState.update {
                it.copy(
                    nameError = nameInvalid,
                    phoneError = phoneInvalid,
                    addressError = addressInvalid
                )
            }
            return
        }
        _uiState.update { it.copy(isPreviewSheetOpen = true) }
    }

    fun dismissPreviewSheet() {
        _uiState.update { it.copy(isPreviewSheetOpen = false) }
    }

    fun showWhatsAppFallbackDialog() {
        _uiState.update { it.copy(isFallbackDialogOpen = true) }
    }

    fun dismissWhatsAppFallbackDialog() {
        _uiState.update { it.copy(isFallbackDialogOpen = false) }
    }

    class Factory(
        private val preferencesRepository: PreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BookingViewModel(preferencesRepository) as T
        }
    }
}
