package com.gudangsimulasi.ayamgulingbandaaceh.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gudangsimulasi.ayamgulingbandaaceh.databinding.CardPartnerBinding

class DataNameAdapter (private val dataName: ArrayList<String>) :
    RecyclerView.Adapter<DataNameAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        CardPartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataName[position]
        holder.bindData(data)

        holder.itemView.setOnClickListener {onItemClickCallback.onClicked(data)}
        holder.itemView.setOnLongClickListener {onItemClickCallback.onLongClicked(data)
        true}
    }

    override fun getItemCount(): Int = dataName.size

    class ViewHolder(private var binding: CardPartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun bindData(data: String) = with(binding) {
            tvNamePartner.text = data
        }
    }

    interface OnItemClickCallback {
        fun onClicked(namePartner: String)
        fun onLongClicked(namePartner: String)
    }

}