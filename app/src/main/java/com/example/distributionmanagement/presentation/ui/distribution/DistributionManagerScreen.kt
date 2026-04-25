package com.example.distributionmanagement.presentation.ui.distribution

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
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.presentation.viewmodel.DistributionManagerViewModel

@Composable
fun DistributionManagerScreen(
    viewModel: DistributionManagerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pendingRequests by viewModel.pendingDistributionRequests.collectAsState()
    val currentItems by viewModel.currentRequestItems.collectAsState()
    val selectedRequestId by viewModel.selectedRequestId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showStartConfirm by remember { mutableStateOf(false) }
    var showCompleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "لوحة تحكم مسئول التوزيع",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F4788),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (selectedRequestId == null) {
            // Requests List View
            Text(
                text = "طلبات التوزيع المعلقة",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color(0xFF1F4788)
            )

            // Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
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
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1F4788))
                }
            } else if (pendingRequests.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pendingRequests) { request ->
                        DistributionRequestCard(
                            request = request,
                            onSelect = { viewModel.selectRequest(it) }
                        )
                    }
                }
            }
        } else {
            // Distribution Details View
            DistributionDetailsScreen(
                requestId = selectedRequestId ?: "",
                items = currentItems,
                onStart = { showStartConfirm = true },
                onComplete = { showCompleteConfirm = true },
                onBack = { /* Reset */ }
            )
        }
    }

    // Start Distribution Dialog
    if (showStartConfirm && selectedRequestId != null) {
        AlertDialog(
            onDismissRequest = { showStartConfirm = false },
            title = {
                Text(
                    text = "بدء التوزيع",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("هل تريد بدء عملية التوزيع لهذا الطلب؟")
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRequestId?.let { viewModel.startDistribution(it) }
                        showStartConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                ) {
                    Text("نعم، ابدأ")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showStartConfirm = false }
                ) {
                    Text("إلغاء")
                }
            }
        )
    }

    // Complete Distribution Dialog
    if (showCompleteConfirm && selectedRequestId != null) {
        AlertDialog(
            onDismissRequest = { showCompleteConfirm = false },
            title = {
                Text(
                    text = "إكمال التوزيع",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("هل تريد إكمال عملية التوزيع لهذا الطلب؟")
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRequestId?.let { viewModel.completeDistribution(it) }
                        showCompleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("نعم، أكمل")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showCompleteConfirm = false }
                ) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@Composable
fun DistributionRequestCard(
    request: DistributionRequest,
    onSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(request.id) },
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
                    color = Color(0xFF4CAF50)
                ) {
                    Text(
                        text = "جاهز",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = "المستودع: ${request.warehouseId}",
                fontSize = 11.sp,
                color = Color(0xFF999999)
            )
        }
    }
}

@Composable
fun DistributionDetailsScreen(
    requestId: String,
    items: List<DistributionItem>,
    onStart: () -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("← رجوع", color = Color(0xFF1F4788))
        }

        Text(
            text = "تفاصيل التوزيع",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color(0xFF1F4788)
        )

        // Request Info
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "معرف الطلب: $requestId",
                    fontSize = 12.sp,
                    color = Color(0xFF1F4788),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "عدد الأصناف: ${items.size}",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Items List
        Text(
            text = "الأصناف المراد توزيعها",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color(0xFF1F4788)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            items(items) { item ->
                DistributionItemCard(item = item)
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onStart,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "▶ ابدأ التوزيع",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onComplete,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "✓ أكمل التوزيع",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun DistributionItemCard(item: DistributionItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "الصنف: ${item.categoryId}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F4788)
                )
                Text(
                    text = "الكمية: ${item.quantity} ${item.unit}",
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFF2196F3)
            ) {
                Text(
                    text = "📦",
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
