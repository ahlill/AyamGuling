package com.gudangsimulasi.ayamgulingbandaaceh

import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gudangsimulasi.ayamgulingbandaaceh.adapter.DataNameAdapter
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.ActivityMainBinding
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.DialogAddPartnerBinding
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.DialogOptionEditBinding
import com.gudangsimulasi.ayamgulingbandaaceh.datamodel.DataModelList
import com.gudangsimulasi.ayamgulingbandaaceh.util.Session
import com.gudangsimulasi.ayamgulingbandaaceh.viewmodel.ViewModel

class MainActivity : AppCompatActivity() {

    private val myViewModel: ViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val bindingDialogAddPartner by lazy { DialogAddPartnerBinding.inflate(layoutInflater) }
    private val bindingDialogLogout by lazy { DialogOptionEditBinding.inflate(layoutInflater) }

    private val PERMISSION_REQUEST_CODE = 200;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivLogout.setOnClickListener{
            iniDialogLogout()
        }

        if (!checkPermission()) {
            requestPermission()
        }

        myViewModel.getDataName()
        myViewModel.dataName.observe(this) {
            showRvName(it)
        }

        binding.btnTambahData.setOnClickListener {
            initDialogAdd()
        }

    }

    private fun showRvName(dataName: ArrayList<String>) = with(binding) {
        rvPelanggan.layoutManager = LinearLayoutManager(this@MainActivity)
        rvPelanggan.setHasFixedSize(true)
        val adapter = DataNameAdapter(dataName)
        rvPelanggan.adapter = adapter

        adapter.setOnItemClickCallback(object : DataNameAdapter.OnItemClickCallback {
            override fun onClicked(namePartner: String) {
                val i = Intent(this@MainActivity, ListDataActivity::class.java)
                i.putExtra("name", namePartner)
                startActivity(i)
            }

            override fun onLongClicked(namePartner: String) {
                initDialogDelete(namePartner)
            }

        })
    }


    private fun initDialogAdd() {
        val dialogEdit = Dialog(this)

        if (bindingDialogAddPartner.root.parent != null){
            (bindingDialogAddPartner.root.parent as ViewGroup).removeView(bindingDialogAddPartner.root)
        }
        dialogEdit.setContentView(bindingDialogAddPartner.root)

        dialogEdit.setCanceledOnTouchOutside(true)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        dialogEdit.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingDialogAddPartner.btnSimpan.setText("Simpan")
        bindingDialogAddPartner.btnSimpan.setOnClickListener {
            val namaPartner = bindingDialogAddPartner.etNamaPartner.text.trim().toString()
            if (namaPartner.isBlank()){
                bindingDialogAddPartner.etNamaPartner.error = "Isi nama"
            } else {
                try {
                    myViewModel.addDataName(namaPartner)
                    bindingDialogAddPartner.etNamaPartner.setText("")
                    dialogEdit.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        dialogEdit.show()
    }

    private fun initDialogDelete(namePartner: String) {
        val dialogEdit = Dialog(this)
        dialogEdit.setTitle("Simpan Data")

        if (bindingDialogAddPartner.root.parent != null){
            (bindingDialogAddPartner.root.parent as ViewGroup).removeView(bindingDialogAddPartner.root)
        }
        dialogEdit.setContentView(bindingDialogAddPartner.root)

        dialogEdit.setCanceledOnTouchOutside(true)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        dialogEdit.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingDialogAddPartner.btnSimpan.setText("Hapus")

        bindingDialogAddPartner.btnSimpan.setOnClickListener {
            val namaPartner = bindingDialogAddPartner.etNamaPartner.text.toString()
            if (namaPartner == namePartner){
                myViewModel.deleteDataName(namaPartner, this)
            } else {
                Toast.makeText(this, "nama salah", Toast.LENGTH_SHORT).show()
            }
            bindingDialogAddPartner.etNamaPartner.setText("")
            dialogEdit.dismiss()
        }
        dialogEdit.show()
    }

    private fun iniDialogLogout() {
        val dialog = Dialog(this)

        if (bindingDialogLogout.root.parent != null){
            (bindingDialogLogout.root.parent as ViewGroup).removeView(bindingDialogLogout.root)
        }
        dialog.setContentView(bindingDialogLogout.root)

        dialog.setCanceledOnTouchOutside(true)

        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        dialog.window?.setLayout(6 * width / 7, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingDialogLogout.btnDelete.text = "Keluar"
        bindingDialogLogout.btnEdit.text = "Batal"
        bindingDialogLogout.btnDelete.setOnClickListener {
            val session = Session(this)
            session.setLogin(false)
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
            dialog.dismiss()
        }

        bindingDialogLogout.btnEdit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun checkPermission(): Boolean
    {
        // checking of permissions.
        var permission1 = ContextCompat . checkSelfPermission (getApplicationContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        var permission2 = ContextCompat . checkSelfPermission (getApplicationContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private fun requestPermission()
    {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        );
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults [0] == PackageManager.PERMISSION_GRANTED;
                val readStorage = grantResults [1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}