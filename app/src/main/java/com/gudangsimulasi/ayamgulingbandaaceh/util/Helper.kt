package com.gudangsimulasi.ayamgulingbandaaceh.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.gudangsimulasi.ayamgulingbandaaceh.datamodel.DataModelList
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class Helper {
    fun formatRupiah(number: Double): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateTime(timeMillis: String?): String {
        return try {
            val sdf1 = SimpleDateFormat("d/M/yyyy h:m:s a")
            sdf1.format(timeMillis?.let { Date(it.toLong()) })
        } catch (e: Exception) {
            e.toString()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(timeMillis: String?): String {
        return try {
            val sdf1 = SimpleDateFormat("d/M/yyyy")
            sdf1.format(timeMillis?.let { Date(it.toLong()) })
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun cmToPoint(cm: Float): Float {
        return cm * 28.3464567F
    }

    fun cellStatement(
        text: String,
        multipleLeading: Float = 1.5f,
        indentLeft: Float = 0f
    ): PdfPCell {
        val fontText = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL, BaseColor.BLACK)
        val c = PdfPCell(Phrase(text, fontText))
        c.setLeading(0f, multipleLeading)
        c.paddingLeft = cmToPoint(indentLeft)
        return c
    }

    fun tableAddStatement(d: ArrayList<DataModelList>, t: PdfPTable) {
        t.horizontalAlignment = Element.ALIGN_CENTER
        t.addCell(cellStatement("No", indentLeft = 0.1f))
        t.addCell(cellStatement("Tanggal", indentLeft = 0.1f))
        t.addCell(cellStatement("Jumlah", indentLeft = 0.1f))
        t.addCell(cellStatement("Harga", indentLeft = 0.1f))
        t.addCell(cellStatement("Total", indentLeft = 0.1f))
        t.addCell(cellStatement("Penerimaan", indentLeft = 0.1f))
        t.addCell(cellStatement("Sisa", indentLeft = 0.1f))
        for (i in d.indices) {
            t.addCell(cellStatement((i + 1).toString(), indentLeft = 0.1f))
            t.addCell(cellStatement(getDateTime(d[i].tanggal), indentLeft = 0.1f))
            t.addCell(cellStatement(d[i].jumlah, indentLeft = 0.1f))
            t.addCell(
                Helper().formatRupiah(d[i].harga.toDouble())
                    ?.let { cellStatement(it, indentLeft = 0.1f) })
            t.addCell(
                Helper().formatRupiah(d[i].total.toDouble())
                    ?.let { cellStatement(it, indentLeft = 0.1f) })
            t.addCell(
                Helper().formatRupiah(d[i].penerimaan.toDouble())
                    ?.let { cellStatement(it, indentLeft = 0.1f) })
            t.addCell(
                Helper().formatRupiah(d[i].sisa.toDouble())
                    ?.let { cellStatement(it, indentLeft = 0.1f) })
        }
    }

    fun textTitle(text: String): Paragraph {
        val fontTitle = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD, BaseColor.BLACK)
        val t = Paragraph(text, fontTitle)
        t.alignment = Element.ALIGN_CENTER
        t.spacingAfter = 20f
        t.font.style = Font.UNDERLINE
        return t
    }


}


