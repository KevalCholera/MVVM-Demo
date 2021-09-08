package com.finter.india.design.employee.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finter.india.R
import com.finter.india.databinding.ItemResultDetailsBinding
import com.finter.india.design.party.add.adapter.PartyAddDocumentData
import com.finter.india.design.employee.interf.PartyDetailsAdapterInterface
import com.finter.india.utils.Constants

class EmployeeDetailsDocumentAdapter(
    customerDetailsAdapterInterface: PartyDetailsAdapterInterface
) :
    RecyclerView.Adapter<EmployeeDetailsDocumentAdapter.SupplierDetailsDocViewHolder>() {

    private var items: MutableList<PartyAddDocumentData> = mutableListOf()
    private var mAdapterCallback = customerDetailsAdapterInterface
    private var onClickedTime = System.currentTimeMillis()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupplierDetailsDocViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemResultDetailsBinding>(
                inflater,
                R.layout.item_result_details,
                parent,
                false
            )

        return SupplierDetailsDocViewHolder(binding)
    }

    fun emptyDocList() {
        items = mutableListOf()

        notifyDataSetChanged()
    }

    fun addDocList(pokemonList: List<PartyAddDocumentData>) {
        items = mutableListOf()
        items.addAll(pokemonList)
        notifyDataSetChanged()
    }

    fun updateDocList(pokemonList: List<PartyAddDocumentData>) {
        items.addAll(pokemonList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SupplierDetailsDocViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            viewModel = item
            executePendingBindings()


            holder.binding.ivItemResultDetailsDownload.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - onClickedTime > Constants.CLICK_TIME) {
                    onClickedTime = currentTime
                    mAdapterCallback.onClickAttachment(item)
                }
            }

            holder.binding.ivItemResultView.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - onClickedTime > Constants.CLICK_TIME) {

                    onClickedTime = currentTime
                    mAdapterCallback.onClickView(item)
                }
            }


        }
    }

    override fun getItemCount() = items.size

    class SupplierDetailsDocViewHolder(val binding: ItemResultDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)
}