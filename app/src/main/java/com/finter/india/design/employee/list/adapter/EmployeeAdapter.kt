package com.finter.india.design.employee.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finter.india.R
import com.finter.india.databinding.ItemEmployeeBinding
import com.finter.india.design.employee.interf.EmployeeAdapterInterface
import com.finter.india.design.employee.list.data.EmployeeData
import com.finter.india.utils.Constants
import com.finter.india.utils.SharedPreferenceUtil

class EmployeeAdapter(
    private val sharedPreferenceUtil: SharedPreferenceUtil,
    employeeAdapterInterface: EmployeeAdapterInterface
) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    private var items: MutableList<EmployeeData> = mutableListOf()
    private var onClickedTime = System.currentTimeMillis()

    private var mAdapterCallback = employeeAdapterInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemEmployeeBinding>(
                inflater,
                R.layout.item_employee,
                parent,
                false
            )
        return EmployeeViewHolder(binding)
    }

    fun addEmployeeList(pokemonList: List<EmployeeData>) {
        items = mutableListOf()
        items.addAll(pokemonList)
        notifyDataSetChanged()
    }

    fun updateEmployeeList(pokemonList: List<EmployeeData>) {
        items.addAll(pokemonList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            salesPerson = item
            executePendingBindings()
            root.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - onClickedTime > Constants.CLICK_TIME) {
                    onClickedTime = currentTime
                    mAdapterCallback.onMethodCallback(sharedPreferenceUtil, root.context, item)
                }
            }
        }
    }

    override fun getItemCount() = items.size

    class EmployeeViewHolder(val binding: ItemEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root)
}