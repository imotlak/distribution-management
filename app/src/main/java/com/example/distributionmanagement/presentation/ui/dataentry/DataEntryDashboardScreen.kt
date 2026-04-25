package com.example.distributionmanagement.presentation.ui.dataentry

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
import com.example.distributionmanagement.presentation.viewmodel.DataEntryViewModel

@Composable
fun DataEntryDashboardScreen(
    viewModel: DataEntryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val pendingRequests by viewModel.pendingRequests.collectAsState()
    val currentItems by viewModel.currentRequestItems.collectAsState()
    val selectedRequestId by viewModel.selectedRequestId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var newCategoryId by remember { mutableStateOf("") }
    var newQuantity by remember { mutableStateOf("") }
    var newUnit by remember { mutableStateOf("كيس") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "لوحة تحكم مدخل البيانات",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F4788),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (selectedRequestId == null) {
            // Requests List View
            Text(
                text = "الطلبات المعلقة",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color(0xFF1F4788)
            )

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
                    Text(
                        text = "لا توجد طلبات معلقة",
                        fontSize = 16.sp,
                        color = Color(0xFF999999)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pendingRequests) { request ->
                        DataEntryRequestCard(
                            request = request,
                            onSelect = { viewModel.selectRequest(it) }
                        )
                    }
                }
            }
        } else {
            // Data Entry Form View
            DataEntryFormScreen(
                selectedRequestId = selectedRequestId ?: "",
                currentItems = currentItems,
                onAddItem = { categoryId, quantity, unit ->
                    viewModel.addItem(categoryId, quantity, unit)
                    newCategoryId = ""
                    newQuantity = ""
                    newUnit = "كيس"
                },
                onApprove = { requestId ->
                    viewModel.approveRequest(requestId)
                },
                onBack = {
                    selectedRequestId?.let { /* Reset */ }
                }
            )
        }
    }
}

@Composable
fun DataEntryRequestCard(
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
                modifier = Modifier.fillMaxWidth(),
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
                Icon(
                    painter = androidx.compose.material.icons.Icons.Default.ChevronRight,
                    contentDescription = "اختر",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF1F4788)
                )
            }
        }
    }
}

@Composable
fun DataEntryFormScreen(
    selectedRequestId: String,
    currentItems: List<DistributionItem>,
    onAddItem: (String, Double, String) -> Unit,
    onApprove: (String) -> Unit,
    onBack: () -> Unit
) {
    var categoryId by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("كيس") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back Button
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("← رجوع", color = Color(0xFF1F4788))
        }

        Text(
            text = "إدخال الأصناف",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color(0xFF1F4788)
        )

        // Add Item Form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = categoryId,
                    onValueChange = { categoryId = it },
                    label = { Text("معرف الصنف") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("الكمية") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                TextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("الوحدة") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5)
                    )
                )

                Button(
                    onClick = {
                        if (quantity.isNotEmpty()) {
                            onAddItem(categoryId, quantity.toDoubleOrNull() ?: 0.0, unit)
                            categoryId = ""
                            quantity = ""
                            unit = "كيس"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ إضافة صنف", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Items List
        Text(
            text = "الأصناف المضافة (${currentItems.size})",
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
            items(currentItems) { item ->
                ItemCard(item = item)
            }
        }

        // Approve Button
        Button(
            onClick = { onApprove(selectedRequestId) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "✓ وافق على الطلب",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ItemCard(item: DistributionItem) {
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
                color = Color(0xFF4CAF50)
            ) {
                Text(
                    text = "✓",
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
