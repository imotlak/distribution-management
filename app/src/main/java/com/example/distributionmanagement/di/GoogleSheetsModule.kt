package com.example.distributionmanagement.di

import android.content.Context
import com.example.distributionmanagement.data.remote.sheets.GoogleSheetsApiService
import com.example.distributionmanagement.data.remote.sheets.GoogleSheetsService
import com.example.distributionmanagement.data.remote.sheets.GoogleSheetsSyncManager
import com.example.distributionmanagement.data.repository.DistributionRepository
import com.example.distributionmanagement.data.repository.CampRepository
import com.example.distributionmanagement.data.repository.AidCategoryRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Dependency Injection module for Google Sheets integration
 */
object GoogleSheetsModule {

    private const val GOOGLE_SHEETS_API_URL = "https://sheets.googleapis.com/"

    // Replace these with your actual values
    private const val SPREADSHEET_ID = "YOUR_SPREADSHEET_ID"
    private const val API_KEY = "YOUR_GOOGLE_SHEETS_API_KEY"

    fun provideGoogleSheetsApiService(): GoogleSheetsApiService {
        return Retrofit.Builder()
            .baseUrl(GOOGLE_SHEETS_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleSheetsApiService::class.java)
    }

    fun provideGoogleSheetsService(
        context: Context,
        apiService: GoogleSheetsApiService = provideGoogleSheetsApiService()
    ): GoogleSheetsService {
        return GoogleSheetsService(
            context = context,
            apiService = apiService,
            spreadsheetId = SPREADSHEET_ID,
            apiKey = API_KEY
        )
    }

    fun provideGoogleSheetsSyncManager(
        context: Context,
        googleSheetsService: GoogleSheetsService,
        distributionRepository: DistributionRepository,
        campRepository: CampRepository,
        aidCategoryRepository: AidCategoryRepository
    ): GoogleSheetsSyncManager {
        return GoogleSheetsSyncManager(
            context = context,
            googleSheetsService = googleSheetsService,
            distributionRepository = distributionRepository,
            campRepository = campRepository,
            aidCategoryRepository = aidCategoryRepository
        )
    }
}
