package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.PreferredSlot
import com.example.data.ServiceCatalog
import com.example.data.ServiceItem
import com.example.ui.components.BottomDispatchBar
import com.example.ui.components.CustomerDetailsSection
import com.example.ui.components.DispatchSettingsDialog
import com.example.ui.components.HeroHeaderBanner
import com.example.ui.components.InlineDispatchPanel
import com.example.ui.components.MessagePreviewSheet
import com.example.ui.components.ServiceSelectorSection
import com.example.ui.components.WhatsAppUnavailableDialog
import kotlinx.coroutines.launch

@Composable
fun BookingScreen(
    uiState: BookingUiState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onIssueNotesChange: (String) -> Unit,
    onServiceSelected: (ServiceItem) -> Unit,
    onSlotSelected: (PreferredSlot) -> Unit,
    onRememberChanged: (Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    onDismissSettings: () -> Unit,
    onSaveBusinessNumber: (String) -> Unit,
    onClearSavedCustomer: () -> Unit,
    onOpenPreview: () -> Unit,
    onDismissPreview: () -> Unit,
    onBookWhatsApp: () -> Unit,
    onCopyMessage: (String) -> Unit,
    onShareMessage: (String) -> Unit,
    onDismissFallbackDialog: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val copiedMsg = stringResource(R.string.msg_copied)
    val savedMsg = stringResource(R.string.msg_settings_saved)
    val clearedMsg = stringResource(R.string.msg_saved_cleared)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWideLayout = maxWidth >= 680.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (!isWideLayout) {
                    BottomDispatchBar(
                        selectedServiceTitle = uiState.selectedService.title,
                        preferredSlotLabel = uiState.preferredSlot.label,
                        onPreviewClick = {
                            focusManager.clearFocus()
                            onOpenPreview()
                        },
                        onBookWhatsAppClick = {
                            focusManager.clearFocus()
                            onBookWhatsApp()
                        },
                        modifier = Modifier.imePadding()
                    )
                }
            }
        ) { innerPadding ->
            if (isWideLayout) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        HeroHeaderBanner(
                            businessWhatsAppNumber = uiState.businessWhatsAppNumber,
                            onOpenSettings = onOpenSettings
                        )
                        ServiceSelectorSection(
                            services = ServiceCatalog.services,
                            selectedService = uiState.selectedService,
                            onServiceSelected = onServiceSelected
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.95f)
                            .verticalScroll(rememberScrollState())
                            .imePadding(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomerDetailsSection(
                            uiState = uiState,
                            onNameChange = onNameChange,
                            onPhoneChange = onPhoneChange,
                            onAddressChange = onAddressChange,
                            onIssueNotesChange = onIssueNotesChange,
                            onSlotSelected = onSlotSelected,
                            onRememberChanged = onRememberChanged,
                            onSubmitKeyboardDone = {
                                focusManager.clearFocus()
                                onBookWhatsApp()
                            }
                        )
                        InlineDispatchPanel(
                            selectedServiceTitle = uiState.selectedService.title,
                            preferredSlotLabel = uiState.preferredSlot.label,
                            onPreviewClick = {
                                focusManager.clearFocus()
                                onOpenPreview()
                            },
                            onBookWhatsAppClick = {
                                focusManager.clearFocus()
                                onBookWhatsApp()
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        HeroHeaderBanner(
                            businessWhatsAppNumber = uiState.businessWhatsAppNumber,
                            onOpenSettings = onOpenSettings
                        )

                        ServiceSelectorSection(
                            services = ServiceCatalog.services,
                            selectedService = uiState.selectedService,
                            onServiceSelected = onServiceSelected
                        )

                        CustomerDetailsSection(
                            uiState = uiState,
                            onNameChange = onNameChange,
                            onPhoneChange = onPhoneChange,
                            onAddressChange = onAddressChange,
                            onIssueNotesChange = onIssueNotesChange,
                            onSlotSelected = onSlotSelected,
                            onRememberChanged = onRememberChanged,
                            onSubmitKeyboardDone = {
                                focusManager.clearFocus()
                                onBookWhatsApp()
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    if (uiState.isPreviewSheetOpen) {
        val message = remember(uiState) { uiState.buildFormattedMessage() }
        MessagePreviewSheet(
            formattedMessage = message,
            onDismiss = onDismissPreview,
            onCopyMessage = {
                onCopyMessage(message)
                scope.launch { snackbarHostState.showSnackbar(copiedMsg) }
            },
            onShareMessage = {
                onDismissPreview()
                onShareMessage(message)
            },
            onSendWhatsApp = onBookWhatsApp
        )
    }

    if (uiState.isSettingsDialogOpen) {
        DispatchSettingsDialog(
            currentNumber = uiState.businessWhatsAppNumber,
            onDismiss = onDismissSettings,
            onSave = { number ->
                onSaveBusinessNumber(number)
                scope.launch { snackbarHostState.showSnackbar(savedMsg) }
            },
            onClearSavedCustomer = {
                onClearSavedCustomer()
                scope.launch { snackbarHostState.showSnackbar(clearedMsg) }
            }
        )
    }

    if (uiState.isFallbackDialogOpen) {
        val message = remember(uiState) { uiState.buildFormattedMessage() }
        WhatsAppUnavailableDialog(
            formattedMessage = message,
            onDismiss = onDismissFallbackDialog,
            onCopyMessage = {
                onCopyMessage(message)
                scope.launch { snackbarHostState.showSnackbar(copiedMsg) }
            },
            onShareMessage = {
                onShareMessage(message)
            }
        )
    }
}
