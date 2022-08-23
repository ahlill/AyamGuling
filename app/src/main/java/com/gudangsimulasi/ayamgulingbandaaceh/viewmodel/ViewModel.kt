package com.gudangsimulasi.ayamgulingbandaaceh.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gudangsimulasi.ayamgulingbandaaceh.datamodel.DataModelList
import com.gudangsimulasi.ayamgulingbandaaceh.util.Helper
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.collections.ArrayList

class ViewModel : ViewModel() {

    private val myRef =
        Firebase.database("https://buku-keuangan-ayam-guling-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val _dataLive = MutableLiveData<ArrayList<DataModelList>>()
    private val _dataNameLive = MutableLiveData<ArrayList<String>>()
    var dataList: LiveData<ArrayList<DataModelList>> = _dataLive
    var dataName: LiveData<ArrayList<String>> = _dataNameLive
    val arrData = arrayListOf<DataModelList>()


    fun addDataList(
        c: Context,
        namaPartner: String,
        namaBarang: String,
        jumlah: String,
        harga: String,
        penerimaan: String
    ) {
        val key:String? = myRef.child(namaPartner).push().key
        val waktuMillis = System.currentTimeMillis().toString()
        val total = jumlah.toDouble() * harga.toDouble()
        val sisa = penerimaan.toDouble() - total
        val mData = DataModelList(
                namaPartner,
                namaBarang,
                waktuMillis,
                jumlah,
                harga,
                total.toString(),
                penerimaan,
                sisa.toString(),
                key
            )
        if (key != null) {
            myRef.child(namaPartner).child(key).setValue(mData)
                .addOnSuccessListener {
                    Toast.makeText(c, "Berhasil menyimpan", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun updateDataList(
        data: DataModelList,
        namaBarang: String,
        jumlah: String,
        harga: String,
        penerimaan: String
    ) {
        val total = jumlah.toDouble() * harga.toDouble()
        val sisa = penerimaan.toDouble() - total
        val mData = DataModelList(
            data.namaPartner,
            namaBarang,
            data.tanggal,
            jumlah,
            harga,
            total.toString(),
            penerimaan,
            sisa.toString(),
            data.key

        )

        data.key?.let {
            myRef.child(data.namaPartner).child(it).setValue(mData).addOnSuccessListener {
                Log.d(TAG, "updateDataList: berhasil")
            }
        }
    }

    fun deleteDataList(data: DataModelList?) {
        if (data != null) {
            data.key?.let {
                myRef.child(data.namaPartner).child(it).removeValue().addOnSuccessListener {
                    Log.d(TAG, "deleteDataList: delete ok")
                }
            }
        }
    }

    fun getDataList(namaPartner: String) {
        myRef.child(namaPartner).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrData.clear()
                for (data: DataSnapshot in snapshot.children) {
                    arrData.add(
                        DataModelList(
                            data.child("namaPartner").value.toString(),
                            data.child("namaBarang").value.toString(),
                            data.child("tanggal").value.toString(),
                            data.child("jumlah").value.toString(),
                            data.child("harga").value.toString(),
                            data.child("total").value.toString(),
                            data.child("penerimaan").value.toString(),
                            data.child("sisa").value.toString(),
                            data.child("key").value.toString()
                        )
                    )
                }
                arrData.reverse()
                _dataLive.postValue(arrData)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun getDataName() {
        Log.d(TAG, "getDataName: berjalan")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val arrDataName = arrayListOf<String>()
                for (data: DataSnapshot in snapshot.children) {
                    arrDataName.add(data.key.toString())
                }
                _dataNameLive.postValue(arrDataName)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun addDataName(namePartner: String) {
        myRef.child(namePartner).setValue("")
    }

    fun deleteDataName(namePartner: String, c: Context){
        myRef.child(namePartner).removeValue().addOnSuccessListener {
            Toast.makeText(c, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
        }
    }

    fun printData(namePartner: String): Boolean{
        val h = Helper()
        val tableSize = floatArrayOf(24f, 80f, 60f, 100f, 100f, 100f, 100f)
        val fName = "${namePartner}_${System.currentTimeMillis()}"
        val path = "/sdcard/$fName.pdf"
        val file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        return try {
            val doc = Document(
                PageSize.A4,
                h.cmToPoint(2f),
                h.cmToPoint(2f),
                h.cmToPoint(2f),
                h.cmToPoint(2f)
            )
            PdfWriter.getInstance(doc, FileOutputStream(file.absoluteFile))

            val t = PdfPTable(tableSize)
            t.widthPercentage = 100f
            doc.open()
            doc.add(h.textTitle("Pembukuan $namePartner ${h.getDate(arrData[arrData.lastIndex].tanggal)} - ${h.getDate(arrData[0].tanggal)}"))
            h.tableAddStatement(arrData, t)
            doc.add(t)
            doc.close()
            return true
        } catch (e: IOException){
            e.printStackTrace()
            false
        }
    }

}