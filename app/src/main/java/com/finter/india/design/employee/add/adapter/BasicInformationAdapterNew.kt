package com.finter.india.design.adapter.basicinformation.statelist

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.finter.india.R

class BasicInformationAdapterNew(
    var stateTag: ObservableField<String>,
    var alert: AlertDialog,
    private var items1: TextView,
    private var stateName: ArrayList<BasicInformationAdapterDataNew>,
    private var stateCode: ObservableField<String>,
    private var isState: Boolean
) : RecyclerView.Adapter<BasicInformationAdapterNew.ViewHolder>() {

    override fun getItemCount(): Int {
        return stateName.size
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvItemElementPopUp: TextView? = null
        var tvItemElementPopUpCode: TextView? = null
        var tvItemElementPopUpTag: TextView? = null

        init {
            this.tvItemElementPopUp = row.findViewById<TextView>(R.id.tvItemElementPopUp)
            this.tvItemElementPopUpCode = row.findViewById<TextView>(R.id.tvItemElementPopUpCode)
            this.tvItemElementPopUpTag = row.findViewById<TextView>(R.id.tvItemElementPopUpTag)
            this.tvItemElementPopUpCode?.visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userDto = stateName[position]
        holder.tvItemElementPopUp?.text = userDto.tvItemElementPopUp
        holder.tvItemElementPopUpCode?.text = "( " + userDto.tvItemElementPopUpCode + " )"
        holder.tvItemElementPopUpTag?.text = userDto.tvItemElementPopUpTag

        holder.tvItemElementPopUpCode?.visibility = View.VISIBLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_element_popup, parent, false)

        val tvItemElementPopUp: TextView = itemView.findViewById(R.id.tvItemElementPopUp)
        val tvItemElementPopUpCode: TextView = itemView.findViewById(R.id.tvItemElementPopUpCode)
        val tvItemElementPopUpTag: TextView = itemView.findViewById(R.id.tvItemElementPopUpTag)

        tvItemElementPopUpCode.visibility = View.VISIBLE

        tvItemElementPopUp.text = stateName[viewType].tvItemElementPopUp.toString()
        tvItemElementPopUpCode.text =
            "( " + stateName[viewType].tvItemElementPopUpCode.toString() + " )"
        tvItemElementPopUpTag.text = stateName[viewType].tvItemElementPopUpTag.toString()

        itemView.setOnClickListener {
            items1.text = tvItemElementPopUp.text
            items1.tag = tvItemElementPopUpTag.text
            stateTag.set(tvItemElementPopUpTag.text.toString())

            if (isState) {
                stateCode.set(stateName[viewType].tvItemElementPopUpTinCode.toString())
            }

            alert.dismiss()
        }

        return ViewHolder(
            itemView
        )
    }
}