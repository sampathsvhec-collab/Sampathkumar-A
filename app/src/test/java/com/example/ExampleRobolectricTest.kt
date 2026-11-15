package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PreferredSlot
import com.example.data.PreferencesRepository
import com.example.data.ServiceCatalog
import com.example.ui.BookingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SS Services", appName)
    }

    @Test
    fun `blank form fails validation and sets inline errors`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val viewModel = BookingViewModel(PreferencesRepository(context))
        advanceUntilIdle()

        val result = viewModel.validateAndPrepareBooking()
        assertNull(result)
        assertTrue(viewModel.uiState.value.nameError)
        assertTrue(viewModel.uiState.value.phoneError)
        assertTrue(viewModel.uiState.value.addressError)
    }

    @Test
    fun `valid booking builds encoded whatsapp dispatch url`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = PreferencesRepository(context)
        val viewModel = BookingViewModel(repo)
        advanceUntilIdle()

        viewModel.saveBusinessWhatsAppNumber("+91 98765 43210")
        viewModel.onCustomerNameChanged("Aarav Sharma")
        viewModel.onPhoneNumberChanged("9876501234")
        viewModel.onAddressChanged("42 MG Road, 2nd Floor")
        viewModel.onServiceSelected(ServiceCatalog.services.first { it.id == "cctv" })
        viewModel.onPreferredSlotSelected(PreferredSlot.EVENING)
        viewModel.onIssueNotesChanged("Need 4 outdoor cameras")

        val url = viewModel.validateAndPrepareBooking()
        advanceUntilIdle()

        assertNotNull(url)
        assertTrue(url!!.startsWith("https://wa.me/919876543210?text="))
        val state = viewModel.uiState.value
        assertFalse(state.nameError)
        assertFalse(state.phoneError)
        assertFalse(state.addressError)
        val message = state.buildFormattedMessage()
        assertTrue(message.contains("SS SERVICES BOOKING"))
        assertTrue(message.contains("Service: CCTV"))
        assertTrue(message.contains("Name: Aarav Sharma"))
        assertTrue(message.contains("Preferred Slot: Evening (4 PM - 8 PM)"))
        assertTrue(message.contains("Notes: Need 4 outdoor cameras"))
    }
}
