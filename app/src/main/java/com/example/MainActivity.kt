package com.example

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.PreferencesRepository
import com.example.ui.BookingScreen
import com.example.ui.BookingViewModel
import com.example.ui.theme.SSServicesTheme

class MainActivity : ComponentActivity() {

    private val preferencesRepository by lazy { PreferencesRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SSServicesTheme {
                val viewModel: BookingViewModel = viewModel(
                    factory = BookingViewModel.Factory(preferencesRepository)
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                BookingScreen(
                    uiState = uiState,
                    onNameChange = viewModel::onCustomerNameChanged,
                    onPhoneChange = viewModel::onPhoneNumberChanged,
                    onAddressChange = viewModel::onAddressChanged,
                    onIssueNotesChange = viewModel::onIssueNotesChanged,
                    onServiceSelected = viewModel::onServiceSelected,
                    onSlotSelected = viewModel::onPreferredSlotSelected,
                    onRememberChanged = viewModel::onRememberDetailsChanged,
                    onOpenSettings = viewModel::openSettingsDialog,
                    onDismissSettings = viewModel::dismissSettingsDialog,
                    onSaveBusinessNumber = viewModel::saveBusinessWhatsAppNumber,
                    onClearSavedCustomer = viewModel::clearSavedCustomerDetails,
                    onOpenPreview = viewModel::openPreviewSheet,
                    onDismissPreview = viewModel::dismissPreviewSheet,
                    onBookWhatsApp = {
                        val url = viewModel.validateAndPrepareBooking()
                        if (url != null) {
                            launchWhatsAppUrl(
                                url = url,
                                onUnavailable = viewModel::showWhatsAppFallbackDialog
                            )
                        }
                    },
                    onCopyMessage = { message ->
                        copyBookingToClipboard(message)
                    },
                    onShareMessage = { message ->
                        shareBookingMessage(message)
                    },
                    onDismissFallbackDialog = viewModel::dismissWhatsAppFallbackDialog,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }

    private fun launchWhatsAppUrl(
        url: String,
        onUnavailable: () -> Unit
    ) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            onUnavailable()
        }
    }

    private fun copyBookingToClipboard(message: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
        clipboard.setPrimaryClip(ClipData.newPlainText("SS Services Booking", message))
    }

    private fun shareBookingMessage(message: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.brand_title))
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(sendIntent, getString(R.string.cta_share_other))
        try {
            startActivity(chooser)
        } catch (_: ActivityNotFoundException) {
            copyBookingToClipboard(message)
        }
    }
}
