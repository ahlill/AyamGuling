package com.gudangsimulasi.ayamgulingbandaaceh.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gudangsimulasi.ayamgulingbandaaceh.R
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.CardDataBinding
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.DialogEditDataBinding
import com.gudangsimulasi.ayamgulingbandaaceh.datamodel.DataModelList
import com.gudangsimulasi.ayamgulingbandaaceh.util.Helper
import com.gudangsimulasi.ayamgulingbandaaceh.viewmodel.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class DataListAdapter(private val viewModel: ViewModel, private val c: Context, private val dataItems: ArrayList<DataModelList>) :
    RecyclerView.Adapter<DataListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(c,
        CardDataBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        DialogEditDataBinding.inflate(LayoutInflater.from(parent.context),parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataItems[position]
        holder.bindData(viewModel,c, data)


        holder.itemView.setOnLongClickListener{onItemClickCallback.onLongClick(data)
        true}

    }

    override fun getItemCount(): Int = dataItems.size

    class ViewHolder(c: Context, private var binding: CardDataBinding, private var bindingDialogEdit: DialogEditDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        internal fun bindData(viewModel: ViewModel, c: Context, data: DataModelList?) = with(binding) {
            tvName.text = data?.namaBarang
            tvTanggal.text = Helper().getDateTime(data?.tanggal)
            tvTitleJumlahBarang.text = "Jumlah : ${data?.jumlah}"
            tvTitleHargaBarang.text = "Harga : ${data?.harga?.let { Helper().formatRupiah(it.toDouble()) }}"
            tvTotalPembayaran.text = "Total : ${data?.total?.let { Helper().formatRupiah(it.toDouble())}}"
            tvTitlePenerimaanPembayaran.text = "Penerimaan : ${data?.penerimaan?.let { Helper().formatRupiah(it.toDouble()) }}"
            tvTitleSisaPenerimaanPembayaran.text = "Sisa : ${data?.sisa?.let { Helper().formatRupiah(it.toDouble()) }}"

            if (data?.sisa?.toDouble()!! < 0.0) {
                tvTitleSisaPenerimaanPembayaran.setBackgroundResource(R.drawable.bg_sisa_minus)
            } else if (data.sisa.toDouble()!! > 0) {
                tvTitleSisaPenerimaanPembayaran.setBackgroundResource(R.drawable.bg_sisa_plus)
            }

        }
    }

    interface OnItemClickCallback{
        fun onClick(data: DataModelList?)
        fun onLongClick(data: DataModelList?)
    }

}