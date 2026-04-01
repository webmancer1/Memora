package com.example.memora.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.memora.ui.planning.BudgetState
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BudgetPdfExporter(private val context: Context) {

    fun exportBudgetToPdf(state: BudgetState): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() 
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        
        val paint = Paint()
        paint.isAntiAlias = true
        
        var yPosition = 50f
        val leftMargin = 50f
        
        
        paint.textSize = 24f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = Color.BLACK
        canvas.drawText("Memora Budget Report", leftMargin, yPosition, paint)
        
        yPosition += 30f
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val dateStr = SimpleDateFormat("MMMM dd, yyyy - HH:mm", Locale.US).format(Date())
        canvas.drawText("Generated on: $dateStr", leftMargin, yPosition, paint)
        
        yPosition += 50f
        
        
        paint.textSize = 16f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Budget Overview", leftMargin, yPosition, paint)
        
        yPosition += 30f
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        canvas.drawText("Total Budget: ${format.format(state.totalBudget)}", leftMargin, yPosition, paint)
        yPosition += 25f
        canvas.drawText("Total Spent: ${format.format(state.totalSpent)}", leftMargin, yPosition, paint)
        yPosition += 25f
        canvas.drawText("Total Pending: ${format.format(state.totalPending)}", leftMargin, yPosition, paint)
        yPosition += 25f
        canvas.drawText("Remaining: ${format.format(state.remaining)}", leftMargin, yPosition, paint)
        
        yPosition += 50f
        
        
        if (state.categoryBreakdown.isNotEmpty()) {
            paint.textSize = 16f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Expenses by Category", leftMargin, yPosition, paint)
            yPosition += 30f
            
            paint.textSize = 14f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            for (category in state.categoryBreakdown) {
                canvas.drawText("${category.name}: ${format.format(category.amount)} (${category.percentage.toInt()}%)", leftMargin, yPosition, paint)
                yPosition += 25f
            }
            yPosition += 25f
        }
        
        
        paint.textSize = 16f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("All Expenses", leftMargin, yPosition, paint)
        yPosition += 30f
        
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        for (expense in state.expenses) {
            if (yPosition > 800f) {
                pdfDocument.finishPage(page)
                
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                yPosition = 50f
                paint.textSize = 12f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val text = "${expense.date} | ${expense.title} (${expense.vendor}) | ${expense.status.uppercase(Locale.US)} | ${format.format(expense.amount)}"
            canvas.drawText(text, leftMargin, yPosition, paint)
            yPosition += 20f
        }
        
        pdfDocument.finishPage(page)
        
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "Memora_Budget_Report_${System.currentTimeMillis()}.pdf")
        
        return try {
            pdfDocument.writeTo(FileOutputStream(file))
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            pdfDocument.close()
        }
    }
}
