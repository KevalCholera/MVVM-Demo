package com.finter.india.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finter.india.design.employee.list.data.EmployeeData
import com.finter.india.design.employee.list.adapter.EmployeeAdapter
import com.skydoves.whatif.whatIfNotNullOrEmpty

@BindingAdapter("adapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}
@BindingAdapter("adapterEmployeeList")
fun bindAdapterEmployeeList(view: RecyclerView, employeeList: List<EmployeeData>?) {
    employeeList.whatIfNotNullOrEmpty {
        (view.adapter as? EmployeeAdapter)?.addEmployeeList(it)
    }
}