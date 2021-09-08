package com.finter.india.design.employee.add.adapter

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finter.india.R
import com.finter.india.databinding.ItemResultBinding

import com.finter.india.design.employee.add.EmployeeAddViewModel
import com.finter.india.design.employee.interf.EmployeeAdapterInterface
import com.finter.india.design.party.add.adapter.PartyAddDocumentData
import com.finter.india.utils.Constants

class EmployeeAddDocumentAdapter(
    var customerAddViewModel: EmployeeAddViewModel,
    var mList: ArrayList<Uri>,
    var mListUploaded: ArrayList<String>,
    var mListLocal: ArrayList<Uri>,
    var activity: Activity,
    var customerId: String,
    private val employeeAdapterInterface: EmployeeAdapterInterface
) :
    RecyclerView.Adapter<EmployeeAddDocumentAdapter.CustomerAddDocViewHolder>() {

    private var items: MutableList<PartyAddDocumentData> = mutableListOf()
    private var onClickedTime = System.currentTimeMillis()
    private var newDoc: Boolean = true
    private var mAdapterCallback = employeeAdapterInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerAddDocViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemResultBinding>(
                inflater,
                R.layout.item_result,
                parent,
                false
            )

        return CustomerAddDocViewHolder(binding)
    }

    fun emptyDocList() {
        items = mutableListOf()

        val mListLocalNew = mListLocal
        val mListUploadedNew = mListUploaded
        val mListNew = mList

        mListLocal.removeAll(mListLocalNew)
        mListUploaded.removeAll(mListUploadedNew)
        mList.removeAll(mListNew)
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

    override fun onBindViewHolder(holder: CustomerAddDocViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            viewModel = item
            executePendingBindings()

            holder.binding.ivItemResultCross.setOnClickListener(View.OnClickListener {
                val currentTime = System.currentTimeMillis()

                if (currentTime - onClickedTime > Constants.CLICK_TIME) {
                    onClickedTime = currentTime

                    items.removeAt(position)

                    mListLocal.removeAt(position - mListUploaded.size)
                    mList.removeAt(position)
                    notifyDataSetChanged()
                }
            })

            holder.binding.ivItemResultRemove.setOnClickListener(View.OnClickListener {
                val currentTime = System.currentTimeMillis()

                if (currentTime - onClickedTime > Constants.CLICK_TIME) {
                    onClickedTime = currentTime

                    mAdapterCallback.onDeleteAttachment(
                        activity,
                        item.attachment_name.toString(),
                        customerAddViewModel,
                        item.id.toString(),
                        customerId
                    )

//                    EmployeeAdd().errorPopUp(
//                        activity,
//                        item.attachment_name.toString(),
//                        customerAddViewModel,
//                        item.id.toString(),
//                        customerId
//                    )
                }
            })
        }
    }

    override fun getItemCount() = items.size

    class CustomerAddDocViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root)
}