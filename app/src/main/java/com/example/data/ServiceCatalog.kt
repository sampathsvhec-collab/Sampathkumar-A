package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.ui.graphics.vector.ImageVector

enum class ServiceCategory(val label: String) {
    POWER("Power & Electrical"),
    WATER("Water & Plumbing"),
    SECURITY("Security Systems"),
    CIVIL("Structural Drilling")
}

enum class PreferredSlot(val label: String, val shortLabel: String) {
    MORNING("Morning (9 AM - 12 PM)", "Morning"),
    AFTERNOON("Afternoon (12 PM - 4 PM)", "Afternoon"),
    EVENING("Evening (4 PM - 8 PM)", "Evening"),
    URGENT("Urgent / Today", "Urgent")
}

data class ServiceItem(
    val id: String,
    val title: String,
    val shortLabel: String,
    val description: String,
    val highlights: List<String>,
    val estimatedTurnaround: String,
    val category: ServiceCategory,
    val icon: ImageVector
)

object ServiceCatalog {
    val services: List<ServiceItem> = listOf(
        ServiceItem(
            id = "electrical",
            title = "Electrical",
            shortLabel = "Electrical",
            description = "Wiring diagnostics, MCB & DB panels, switches, fans, and short-circuit repair.",
            highlights = listOf("Fault tracing & rewiring", "MCB / RCCB panel setup", "Lights, fans & sockets"),
            estimatedTurnaround = "Same day",
            category = ServiceCategory.POWER,
            icon = Icons.Rounded.Bolt
        ),
        ServiceItem(
            id = "plumbing",
            title = "Plumbing",
            shortLabel = "Plumbing",
            description = "Pipe leak repair, bathroom fittings, motor lines, blockage clearing, and drainage.",
            highlights = listOf("Leak & joint sealing", "Taps, mixers & valves", "Drainage & line flushing"),
            estimatedTurnaround = "Same day",
            category = ServiceCategory.WATER,
            icon = Icons.Rounded.Plumbing
        ),
        ServiceItem(
            id = "cctv",
            title = "CCTV",
            shortLabel = "CCTV",
            description = "HD & IP camera installation, DVR/NVR configuration, cabling, and phone viewing.",
            highlights = listOf("Indoor & outdoor cams", "DVR / NVR storage setup", "Mobile app remote view"),
            estimatedTurnaround = "Within 24 hrs",
            category = ServiceCategory.SECURITY,
            icon = Icons.Rounded.Videocam
        ),
        ServiceItem(
            id = "water_purifier",
            title = "Water Purifier",
            shortLabel = "RO / Purifier",
            description = "RO/UV purifier installation, sediment & carbon filter replacement, and TDS check.",
            highlights = listOf("New RO/UV mounting", "Filter & membrane kit", "TDS & pressure check"),
            estimatedTurnaround = "Same day",
            category = ServiceCategory.WATER,
            icon = Icons.Rounded.WaterDrop
        ),
        ServiceItem(
            id = "borewell_works",
            title = "Borewell Works",
            shortLabel = "Borewell",
            description = "Submersible pump installation, motor troubleshooting, cable jointing, and starter panels.",
            highlights = listOf("Submersible pump pull/drop", "Starter & relay repair", "Waterproof cable joints"),
            estimatedTurnaround = "Within 24 hrs",
            category = ServiceCategory.WATER,
            icon = Icons.Rounded.Waves
        ),
        ServiceItem(
            id = "ups_battery",
            title = "UPS Battery Installation",
            shortLabel = "UPS & Battery",
            description = "Home inverter & UPS wiring, dual-battery bank setup, and safe load isolation.",
            highlights = listOf("Inverter & battery setup", "Backup circuit wiring", "Load & earthing check"),
            estimatedTurnaround = "Same day",
            category = ServiceCategory.POWER,
            icon = Icons.Rounded.BatteryChargingFull
        ),
        ServiceItem(
            id = "water_level_controller",
            title = "Water Level Controller",
            shortLabel = "Tank Controller",
            description = "Automatic overhead & sump tank controller installation with dry-run protection.",
            highlights = listOf("Auto motor cut-off", "Dry-run & overflow guard", "Corrosion-free sensors"),
            estimatedTurnaround = "Same day",
            category = ServiceCategory.WATER,
            icon = Icons.Rounded.Sensors
        ),
        ServiceItem(
            id = "core_cutting",
            title = "Core Cutting",
            shortLabel = "Core Cutting",
            description = "Precision low-vibration diamond core drilling through RCC slabs, beams, and brick walls.",
            highlights = listOf("AC & chimney openings", "Plumbing & conduit bores", "Clean dust-controlled cut"),
            estimatedTurnaround = "Scheduled slot",
            category = ServiceCategory.CIVIL,
            icon = Icons.Rounded.Construction
        )
    )

    val defaultService: ServiceItem = services.first()
}
