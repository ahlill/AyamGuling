package com.gudangsimulasi.ayamgulingbandaaceh

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gudangsimulasi.ayamgulingbandaaceh.adapter.DataListAdapter
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.ActivityListDataBinding
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.DialogAddDataBinding
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.DialogEditDataBinding
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.DialogOptionEditBinding
import com.gudangsimulasi.ayamgulingbandaaceh.datamodel.DataModelList
import com.gudangsimulasi.ayamgulingbandaaceh.util.DecimalFormatSeparator
import com.gudangsimulasi.ayamgulingbandaaceh.util.Helper
import com.gudangsimulasi.ayamgulingbandaaceh.viewmodel.ViewModel

class ListDataActivity : AppCompatActivity() {

    private val myViewModel: ViewModel by viewModels()
    private val binding by lazy { ActivityListDataBinding.inflate(layoutInflater) }
    private val bindingDialogOption by lazy { DialogOptionEditBinding.inflate(layoutInflater) }
    private val bindingDialogEdit by lazy { DialogEditDataBinding.inflate(layoutInflater) }
    private val bindingDialogAdd by lazy { DialogAddDataBinding.inflate(layoutInflater) }
    private lateinit var key: String

    private lateinit var s: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        key = intent.extras?.getString("name", "").toString()
        binding.tvNamaPartner.text = key
        myViewModel.getDataList(key)
        myViewModel.dataList.observe(this) {
            showDataList(this, it)
        }

        binding.btnTambahData.setOnClickListener {
            initDialogAdd()
        }

        binding.btnPrint.setOnClickListener {
            if (myViewModel.printData(key)) {
                Toast.makeText(this, "File Created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDataList(c: Context, dataList: ArrayList<DataModelList>) = with(binding) {
        rvDataList.layoutManager = LinearLayoutManager(this@ListDataActivity)
        rvDataList.setHasFixedSize(true)
        val adapter = DataListAdapter(myViewModel, c, dataList)
        rvDataList.adapter = adapter

        adapter.setOnItemClickCallback(object : DataListAdapter.OnItemClickCallback {
            override fun onClick(data: DataModelList?) {

            }

            override fun onLongClick(data: DataModelList?) {
                if (data != null) {
                    iniDialogOption(data)
                }
            }

        })

    }

    private fun iniDialogOption(data: DataModelList) {
        val dialog = Dialog(this@ListDataActivity)

        if (bindingDialogOption.root.parent != null) {
            (bindingDialogOption.root.parent as ViewGroup).removeView(bindingDialogOption.root)
        }
        dialog.setContentView(bindingDialogOption.root)

        dialog.setCanceledOnTouchOutside(true)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        dialog.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingDialogOption.btnDelete.setOnClickListener {
            try {
                myViewModel.deleteDataList(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }

        bindingDialogOption.btnEdit.setOnClickListener {
            try {
                initDialogEdit(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initDialogEdit(data: DataModelList) {
        val dialogEdit = Dialog(this)

        if (bindingDialogEdit.root.parent != null) {
            (bindingDialogEdit.root.parent as ViewGroup).removeView(bindingDialogEdit.root)
        }
        dialogEdit.setContentView(bindingDialogEdit.root)

        dialogEdit.setCanceledOnTouchOutside(true)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        dialogEdit.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingDialogEdit.etNamaBarangEdit.setText(data.namaBarang)
        bindingDialogEdit.etJumlahBarangEdit.setText(data.jumlah)
        bindingDialogEdit.etHargaBarangEdit.setText(data.harga)
        bindingDialogEdit.etPenerimaanEdit.setText(data.penerimaan)

        bindingDialogEdit.btnSimpan.setOnClickListener {
            val namaBarang = bindingDialogEdit.etNamaBarangEdit.text.toString()
            val jumlah = bindingDialogEdit.etJumlahBarangEdit.text.toString()
            val harga = bindingDialogEdit.etHargaBarangEdit.text.toString()
            val penerimaan = bindingDialogEdit.etPenerimaanEdit.text.toString()

            if (namaBarang.isBlank()) {
                bindingDialogEdit.etNamaBarangEdit.error = "isi data"
            } else if (jumlah.isBlank()) {
                bindingDialogEdit.etJumlahBarangEdit.error = "isi data"
            } else if (harga.isBlank()) {
                bindingDialogEdit.etHargaBarangEdit.error = "isi data"
            } else if (penerimaan.isBlank()) {
                bindingDialogEdit.etPenerimaanEdit.error = "isi data"
            } else {
                myViewModel.updateDataList(data, namaBarang, jumlah, harga, penerimaan)
                dialogEdit.dismiss()
            }

        }
        dialogEdit.show()
    }

    private fun initDialogAdd() {
        val dialogAdd = Dialog(this)

        if (bindingDialogAdd.root.parent != null) {
            (bindingDialogAdd.root.parent as ViewGroup).removeView(bindingDialogAdd.root)
        }
        dialogAdd.setContentView(bindingDialogAdd.root)

        dialogAdd.setCanceledOnTouchOutside(true)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        dialogAdd.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingDialogAdd.etNamaBarangEdit.setText("")
        bindingDialogAdd.etJumlahBarangEdit.setText("")
        bindingDialogAdd.etHargaBarangEdit.setText("")
        bindingDialogAdd.etPenerimaanEdit.setText("")

        bindingDialogAdd.btnSimpan.setOnClickListener {
            val namaBarang = bindingDialogAdd.etNamaBarangEdit.text.toString()
            val jumlah = bindingDialogAdd.etJumlahBarangEdit.text.toString()
            val harga = bindingDialogAdd.etHargaBarangEdit.text.toString()
            val penerimaan = bindingDialogAdd.etPenerimaanEdit.text.toString()

            if (namaBarang.isBlank()) {
                bindingDialogEdit.etNamaBarangEdit.error = "isi data"
            } else if (jumlah.isBlank()) {
                bindingDialogEdit.etJumlahBarangEdit.error = "isi data"
            } else if (harga.isBlank()) {
                bindingDialogEdit.etHargaBarangEdit.error = "isi data"
            } else if (penerimaan.isBlank()) {
                bindingDialogEdit.etPenerimaanEdit.error = "isi data"
            } else {
                try {
                    myViewModel.addDataList(this, key, namaBarang, jumlah, harga, penerimaan)
                    dialogAdd.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        dialogAdd.show()
    }
}