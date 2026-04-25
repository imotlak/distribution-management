package com.example.distributionmanagement.data.remote.sheets

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.Query

/**
 * Google Sheets API Service
 * Communicates with Google Sheets using Sheets API v4
 */
interface GoogleSheetsApiService {

    /**
     * Get values from a specific range in the spreadsheet
     * @param spreadsheetId The ID of the spreadsheet
     * @param range The range to read (e.g., "Sheet1!A1:D10")
     */
    @GET("v4/spreadsheets/{spreadsheetId}/values/{range}")
    suspend fun getValues(
        @Path("spreadsheetId") spreadsheetId: String,
        @Path("range") range: String,
        @Query("key") apiKey: String
    ): SheetsResponse<ValueRange>

    /**
     * Update values in the spreadsheet
     * @param spreadsheetId The ID of the spreadsheet
     * @param range The range to update
     */
    @PUT("v4/spreadsheets/{spreadsheetId}/values/{range}")
    suspend fun updateValues(
        @Path("spreadsheetId") spreadsheetId: String,
        @Path("range") range: String,
        @Body valueRange: ValueRange,
        @Query("valueInputOption") valueInputOption: String = "USER_ENTERED",
        @Query("key") apiKey: String
    ): SheetsResponse<UpdateValuesResponse>

    /**
     * Append values to the spreadsheet
     * @param spreadsheetId The ID of the spreadsheet
     * @param range The range where to append
     */
    @POST("v4/spreadsheets/{spreadsheetId}/values/{range}:append")
    suspend fun appendValues(
        @Path("spreadsheetId") spreadsheetId: String,
        @Path("range") range: String,
        @Body valueRange: ValueRange,
        @Query("valueInputOption") valueInputOption: String = "USER_ENTERED",
        @Query("key") apiKey: String
    ): SheetsResponse<AppendValuesResponse>

    /**
     * Clear values from a range
     * @param spreadsheetId The ID of the spreadsheet
     * @param range The range to clear
     */
    @POST("v4/spreadsheets/{spreadsheetId}/values/{range}:clear")
    suspend fun clearValues(
        @Path("spreadsheetId") spreadsheetId: String,
        @Path("range") range: String,
        @Query("key") apiKey: String
    ): SheetsResponse<ClearValuesResponse>
}

/**
 * Data class representing a range of values in Google Sheets
 */
data class ValueRange(
    val range: String? = null,
    val majorDimension: String = "ROWS",
    val values: List<List<Any>>? = null
)

/**
 * Response wrapper from Google Sheets API
 */
data class SheetsResponse<T>(
    val data: T? = null
)

/**
 * Response after updating values
 */
data class UpdateValuesResponse(
    val spreadsheetId: String,
    val updatedRange: String,
    val updatedRows: Int,
    val updatedColumns: Int,
    val updatedCells: Int
)

/**
 * Response after appending values
 */
data class AppendValuesResponse(
    val spreadsheetId: String,
    val updatedRange: String,
    val updatedRows: Int,
    val updatedColumns: Int,
    val updatedCells: Int
)

/**
 * Response after clearing values
 */
data class ClearValuesResponse(
    val spreadsheetId: String,
    val clearedRange: String
)
