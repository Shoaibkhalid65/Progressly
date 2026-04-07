package com.example.progresstracker.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.progresstracker.ui.dashboard.charts.BarChart
import com.example.progresstracker.ui.dashboard.charts.BarChartData
import com.example.progresstracker.ui.dashboard.charts.LineChart
import com.example.progresstracker.ui.dashboard.charts.LineChartData

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                windowInsets = WindowInsets()
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Today summary cards ───────────────────────────────────
            Text(
                text = "Today",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    label = "Hours worked",
                    value = "%.1f h".format(uiState.todaySummary.totalHours),
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    label = "Sessions",
                    value = "${uiState.todaySummary.tasksDone}",
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    label = "Avg satisfaction",
                    value = "%.0f%%".format(uiState.todaySummary.avgSatisfaction),
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Weekly bar chart ──────────────────────────────────────
            Text(
                text = "This week — work hours",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (uiState.weeklyDurations.isEmpty()) {
                EmptyChartPlaceholder("No duration data yet")
            } else {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    BarChart(
                        data = uiState.weeklyDurations.map { entry ->
                            BarChartData(
                                label = viewModel.dayLabel(entry.dateEpoch),
                                value = entry.totalMillis / 3_600_000f,
                                unit = "h"
                            )
                        },
                        modifier = Modifier.padding(16.dp),
                        barColor = MaterialTheme.colorScheme.primary,
                        selectedBarColor = MaterialTheme.colorScheme.tertiary,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        gridColor = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            // ── Satisfaction trend ────────────────────────────────────
            Text(
                text = "30-day satisfaction trend",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (uiState.satisfactionTrend.isEmpty()) {
                EmptyChartPlaceholder("No satisfaction data yet")
            } else {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    LineChart(
                        data = uiState.satisfactionTrend.map { entry ->
                            LineChartData(
                                label = viewModel.dayLabel(entry.dateEpoch),
                                value = entry.avgPercent
                            )
                        },
                        modifier = Modifier.padding(16.dp),
                        lineColor = MaterialTheme.colorScheme.primary,
                        fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        gridColor = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

// ── Summary card ──────────────────────────────────────────────────────────────

@Composable
fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
fun EmptyChartPlaceholder(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
    }
}
