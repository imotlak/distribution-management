# Distribution Management App - Project Structure

## مشروع تطبيق إدارة عملية التوزيع

### نظرة عامة
تطبيق اندرويد متقدم لإدارة عملية توزيع المساعدات من المخازن والمخيمات بنظام workflow متعدد المراحل.

### المميزات الرئيسية

#### 1. نظام الأدوار والصلاحيات (Role-Based Access)
- **المدير (Manager)**: يوافق على طلبات التوزيع الأولية
- **مدخل البيانات (Data Entry)**: يدخل بيانات التوزيع ويوافق عليها
- **مسئول التوزيع (Distribution Manager)**: ينفذ التوزيع الفعلي
- **المسئول الإداري (Admin)**: إدارة المستخدمين والمخازن والمخيمات

#### 2. Workflow العملية
```
المخيم
   ↓
إدخال بيانات المخيم
   ↓
موافقة المدير
   ↓
موافقة مدخل البيانات
   ↓
توزيع بواسطة مسئول التوزيع
   ↓
تسجيل المستلم والتوقيع
   ↓
إنهاء العملية
```

#### 3. البيانات الأساسية (Master Data)
- **المخيمات (Camps)**: معلومات المخيم، الموقع، العدد
- **المخازن (Warehouses)**: معلومات المخزن والمسئول
- **فئات المساعدات (Aid Categories)**: أنواع المساعدات والوحدات

#### 4. التكامل مع Google Sheets
- مزامنة البيانات مع Google Sheets
- تصدير التقارير النهائية
- سهولة المشاركة والتعاون

### البنية المشروعية

```
distribution-management/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/distributionmanagement/app/
│   │   │   │   ├── data/
│   │   │   │   │   ├── dao/                 # Database Access Objects
│   │   │   │   │   ├── database/            # Room Database
│   │   │   │   │   ├── model/               # Data Models
│   │   │   │   │   ├── repository/          # Repository Pattern
│   │   │   │   │   └── service/             # Google Sheets Service
│   │   │   │   ├── ui/
│   │   │   │   │   ├── auth/                # Authentication Screens
│   │   │   │   │   ├── manager/             # Manager Dashboard
│   │   │   │   │   ├── dataentry/           # Data Entry Dashboard
│   │   │   │   │   ├── distribution/        # Distribution Manager Dashboard
│   │   │   │   │   ├── request/             # Request Details & Approval
│   │   │   │   │   ├── viewmodel/           # ViewModels
│   │   │   │   │   └── MainActivity.kt      # Main Activity
│   │   │   │   ├── utils/                   # Utility Functions
│   │   │   │   └── DistributionApp.kt       # Application Class
│   │   │   ├── res/
│   │   │   │   ├── layout/                  # UI Layouts
│   │   │   │   ├── values/                  # Colors, Strings
│   │   │   │   ├── drawable/                # Icons & Images
│   │   │   │   └── menu/                    # Menu Resources
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                            # Unit Tests
│   │   └── androidTest/                     # Android Tests
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

### المكتبات المستخدمة

#### Android Framework
- `androidx.appcompat:appcompat` - Backward compatibility
- `androidx.constraintlayout` - Layout management
- `com.google.android.material` - Material Design 3
- `androidx.lifecycle` - ViewModel, LiveData
- `androidx.navigation` - Navigation Component

#### Database
- `androidx.room` - Local database
- SQLite - Offline support

#### Google Sheets Integration
- `com.google.api-client` - Google API Client
- `com.google.apis:google-api-services-sheets` - Sheets API
- `com.google.auth-library-java-oauth2` - OAuth 2.0

#### Networking
- `com.squareup.retrofit2` - REST Client
- `com.squareup.okhttp3` - HTTP Client
- `com.google.code.gson` - JSON Parsing

#### Firebase
- `firebase-auth` - Authentication
- `firebase-firestore` - Cloud Database (Optional)

#### Dependency Injection
- `com.google.dagger:hilt-android` - Dependency Injection

### نماذج البيانات (Data Models)

#### User
```kotlin
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val role: UserRole,
    val isActive: Boolean
)
```

#### DistributionRequest
```kotlin
data class DistributionRequest(
    val id: String,
    val requestNumber: String,
    val campId: String,
    val warehouseId: String,
    val status: RequestStatus,
    val managerApprovedBy: String?,
    val dataEntryApprovedBy: String?,
    val distributionStartedBy: String?,
    val distributionCompletedBy: String?
)
```

#### DistributionItem
```kotlin
data class DistributionItem(
    val id: String,
    val requestId: String,
    val categoryId: String,
    val requestedQuantity: Double,
    val approvedQuantity: Double?,
    val distributedQuantity: Double?
)
```

### واجهات المستخدم (UI Screens)

#### 1. Login Screen
- تسجيل الدخول بالبريد الإلكتروني
- اختيار الدور

#### 2. Manager Dashboard
- عرض الطلبات المعلقة
- الموافقة أو الرفض
- إضافة ملاحظات

#### 3. Data Entry Dashboard
- إدخال بيانات التوزيع
- تعديل الكميات المطلوبة
- الموافقة النهائية

#### 4. Distribution Manager Dashboard
- عرض الطلبات المعتمدة
- بدء التوزيع
- تسجيل التوزيع الفعلي
- التوقيع الرقمي

#### 5. Request Details Screen
- عرض جميع تفاصيل الطلب
- سجل النشاط (Activity Log)
- التاريخ الكامل للطلب

### الميزات المتقدمة

#### 1. Offline Support
- جميع البيانات تحفظ محلياً
- Sync تلقائي عند الاتصال بالإنترنت

#### 2. Activity Logging
- تسجيل جميع الإجراءات
- تتبع التعديلات والموافقات
- تقارير تفصيلية

#### 3. Reporting
- تصدير التقارير إلى Google Sheets
- احصائيات التوزيع
- تقارير الأداء

### الخطوات التالية

- [ ] إنشاء واجهات UI باستخدام Jetpack Compose أو XML
- [ ] تنفيذ نظام Authentication
- [ ] إنشاء Google Sheets Integration
- [ ] كتابة Unit Tests
- [ ] إضافة Hilt Dependency Injection
- [ ] تطبيق تصميم Material Design 3
- [ ] إضافة Notifications
- [ ] تحسين الأداء والأمان

### المتطلبات والإعدادات

#### الأذونات المطلوبة
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

#### الإعدادات الأساسية
- Min SDK: 24
- Target SDK: 34
- Compile SDK: 34

### الدعم والمساهمة

للمساهمة في المشروع أو الإبلاغ عن المشاكل، يرجى إنشاء Issue أو Pull Request.

---

**آخر تحديث:** 2026-04-25
