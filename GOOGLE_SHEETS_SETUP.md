# Google Sheets Integration Guide

## الإعدادات المطلوبة

### 1. إنشاء Google Sheets Spreadsheet

1. اذهب إلى [Google Sheets](https://sheets.google.com)
2. أنشئ spreadsheet جديد باسم "Distribution Management"
3. أنشئ الأوراق التالية (Sheets):
   - Distribution Requests
   - Distribution Items
   - Camps
   - Aid Categories
   - Users

### 2. إعداد Google API Console

1. اذهب إلى [Google Cloud Console](https://console.cloud.google.com)
2. أنشئ مشروع جديد
3. فعّل Google Sheets API:
   - اذهب إلى "APIs & Services" > "Library"
   - ابحث عن "Google Sheets API"
   - اضغط "Enable"

4. أنشئ API Key:
   - اذهب إلى "APIs & Services" > "Credentials"
   - اضغط "Create Credentials" > "API Key"
   - انسخ الـ API Key

### 3. تحديث الكود

في ملف `GoogleSheetsModule.kt`، استبدل:

```kotlin
private const val SPREADSHEET_ID = "YOUR_SPREADSHEET_ID"
private const val API_KEY = "YOUR_GOOGLE_SHEETS_API_KEY"
```

بـ:
- `SPREADSHEET_ID`: معرف الـ Spreadsheet (موجود في الـ URL)
- `API_KEY`: الـ API Key الذي حصلت عليه

### 4. هيكل البيانات في Google Sheets

#### Distribution Requests Sheet
```
A          B       C           D       E                    F           G                  H                      I                          J                              K
ID         Camp    Warehouse   Status  Created By User ID   Created At  Manager Approval   Data Entry Approval    Distribution Started At    Distribution Completed At      Notes
123abc     C1      W1          PENDING user1               2024-01-01   2024-01-02        2024-01-03            2024-01-04                                             ملاحظات
```

#### Distribution Items Sheet
```
A      B         C          D        E      F
ID     Request   Category   Qty      Unit   Created At
456    123abc    CAT1       100      كيس    2024-01-01
```

#### Camps Sheet
```
A      B           C        D        E          F
ID     Name        Location Capacity Status     Created At
C1     المخيم الأول الشرقية  1000    active     2024-01-01
```

#### Aid Categories Sheet
```
A      B          C          D      E
ID     Name       Description Unit   Created At
CAT1   الحبوب      وصف         كيس    2024-01-01
```

## الاستخدام

### 1. تحميل البيانات من Sheets
```kotlin
val syncManager = GoogleSheetsSyncManager(...)
val result = syncManager.syncFromSheets()
if (result.isSuccess) {
    // البيانات تم تحميلها بنجاح
}
```

### 2. رفع البيانات إلى Sheets
```kotlin
val result = syncManager.syncToSheets()
if (result.isSuccess) {
    // البيانات تم رفعها بنجاح
}
```

### 3. المزامنة الثنائية
```kotlin
val result = syncManager.biDirectionalSync()
```

## ملاحظات مهمة

⚠️ **أمان:**
- لا تضع الـ API Key مباشرة في الكود في الإنتاج
- استخدم `secrets.properties` أو Firebase Remote Config
- حدد نطاق الـ IP في Google Cloud Console

📱 **الأداء:**
- المزامنة تأخذ وقتاً، استخدم `isSyncInProgress()` للتحقق
- لا تشغل مزامنتين في نفس الوقت
- استخدم caching محلي لتقليل الطلبات

🔄 **التزامن:**
- يدعم conflict resolution (آخر تحديث يفوز)
- تتبع آخر وقت مزامنة `getLastSyncTime()`

## المكتبات المطلوبة

أضف في `build.gradle`:

```gradle
dependencies {
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // Gson
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1'
}
```

## استكشاف الأخطاء

### 1. خطأ 403 (Permission Denied)
- تأكد أن الـ API Key صحيح
- تأكد أن Google Sheets API مفعل

### 2. خطأ 404 (Not Found)
- تأكد أن SPREADSHEET_ID صحيح
- تأكد أن أسماء الأوراق صحيحة

### 3. بطء في المزامنة
- قلل عدد الصفوف
- استخدم pagination
- حسّن الإتصال

## الخطوات التالية

- [ ] اختبار المزامنة مع بيانات حقيقية
- [ ] إضافة error handling أفضل
- [ ] إضافة notificaations للمزامنة
- [ ] إضافة backup تلقائي
