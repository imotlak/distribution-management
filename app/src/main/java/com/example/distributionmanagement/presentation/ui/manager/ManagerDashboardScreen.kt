package com.example.distributionmanagement.presentation.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.presentation.viewmodel.ManagerViewModel

@Composable
fun ManagerDashboardScreen(
    viewModel: ManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pendingRequests by viewModel.pendingRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedRequestId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            // Show error message
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            // Show success message
            viewModel.clearMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "لوحة تحكم المدير",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F4788),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "عدد الطلبات المعلقة: ${pendingRequests.size}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F4788)
                )
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFF1F4788)
                ) {
                    Text(
                        text = pendingRequests.size.toString(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Loading State
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1F4788))
            }
        } else if (pendingRequests.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "✓",
                        fontSize = 64.sp,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "لا توجد طلبات معلقة",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 16.dp),
                        color = Color(0xFF666666)
                    )
                }
            }
        } else {
            // Requests List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pendingRequests) { request ->
                    ManagerRequestCard(
                        request = request,
                        onApprove = { requestId ->
                            selectedRequestId = requestId
                            showConfirmDialog = true
                        }
                    )
                }
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmDialog && selectedRequestId != null) {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog = false
                selectedRequestId = null
            },
            title = {
                Text(
                    text = "تأكيد الموافقة",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("هل تريد الموافقة على هذا الطلب؟")
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRequestId?.let { viewModel.approveRequest(it) }
                        showConfirmDialog = false
                        selectedRequestId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("نعم، وافق")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showConfirmDialog = false
                        selectedRequestId = null
                    }
                ) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun ManagerRequestCard(
    request: DistributionRequest,
    onApprove: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "معرف الطلب: ${request.id.take(8)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F4788)
                    )
                    Text(
                        text = "المخيم: ${request.campId}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFFFB74D)
                ) {
                    Text(
                        text = "قيد الانتظار",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "تم الإنشاء: ${request.createdAt}",
                fontSize = 11.sp,
                color = Color(0xFF999999),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = { onApprove(request.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "✓ وافق على الطلب",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
