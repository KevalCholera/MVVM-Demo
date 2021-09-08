package com.finter.india.design.employee.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baoyz.widget.PullRefreshLayout
import com.finter.india.R
import com.finter.india.databinding.FragmentEmployeeBinding
import com.finter.india.design.drawer.DrawerActivity
import com.finter.india.design.employee.add.EmployeeAdd
import com.finter.india.design.employee.add.EmployeeAddViewModel
import com.finter.india.design.employee.details.EmployeeDetails
import com.finter.india.design.employee.interf.EmployeeAdapterInterface
import com.finter.india.design.employee.list.adapter.EmployeeAdapter
import com.finter.india.design.employee.list.data.EmployeeData
import com.finter.india.model.Status
import com.finter.india.utils.*
import com.finter.india.utils.interf.NoInternetInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_drawer_toolbaar_frame.*
import javax.inject.Inject


@AndroidEntryPoint
class Employee : Fragment(), EmployeeAdapterInterface, NoInternetInterface {

    companion object {
        fun newInstance() =
            Employee()
    }

    @VisibleForTesting
    val customerListViewModel by viewModels<EmployeeViewModel>()
    lateinit var customerAdapter: EmployeeAdapter
    var totalSize = 0
    var currentSize = 0
    var newData = false
    var pageNo = 1
    var lastPage = 1
    lateinit var context: DrawerActivity
    var nextPageCall = false
    var search = ""
    private var apiCall = -1

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val fragmentSalesPersonBinding =
            FragmentEmployeeBinding.inflate(inflater, container, false)
        fragmentSalesPersonBinding.apply {
            viewModel = customerListViewModel
        }
        context = activity as DrawerActivity

        customerAdapter = EmployeeAdapter(sharedPreferenceUtil, this)
        fragmentSalesPersonBinding.rvFragmentSalesPersonDataList.layoutManager =
            LinearLayoutManager(context)
        fragmentSalesPersonBinding.rvFragmentSalesPersonDataList.adapter = customerAdapter

        context.tvDrawerToolbarTitle.title = "Employee"
        context.tvDrawerToolbarTitle.subtitle = ""

        fragmentSalesPersonBinding.ivSearch.setOnClickListener(View.OnClickListener {

            search = fragmentSalesPersonBinding.etSearch.text.toString()
            newData = false
            totalSize = 0
            currentSize = 0
            pageNo = 1
            context.tvDrawerToolbarTitle.title = "Employee"
            context.tvDrawerToolbarTitle.subtitle = ""
            customerListViewModel.fetchSalesPersonData(pageNo.toString(), search)
        })

        fragmentSalesPersonBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s == "" || s.isEmpty())
                    fragmentSalesPersonBinding.ivSearchClear.visibility = View.GONE
                else
                    fragmentSalesPersonBinding.ivSearchClear.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        fragmentSalesPersonBinding.ivSearchClear.setOnClickListener(View.OnClickListener {
            fragmentSalesPersonBinding.etSearch.setText("")
            fragmentSalesPersonBinding.ivSearchClear.visibility = View.GONE
            fragmentSalesPersonBinding.ivSearch.performClick()
        })

        fragmentSalesPersonBinding.prlFragmentSalesPersonList.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN)
        fragmentSalesPersonBinding.prlFragmentSalesPersonList.setColor(
            ContextCompat.getColor(
                context,
                R.color.white
            )
        )

        customerListViewModel.salesPersonResponse.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    it.response?.let { employee ->

                        if (totalSize == 0 || totalSize != currentSize) {
                            if (!newData)
                                customerAdapter.addEmployeeList(employee.data?.data!!)
                            else
                                customerAdapter.addEmployeeList(employee.data?.data!!)

                            nextPageCall = true

                            totalSize = employee.data.total!!.toInt()
                            currentSize = employee.data.to!!.toInt()

                            context.tvDrawerToolbarTitle.title =
                                "Employee"
                            context.tvDrawerToolbarTitle.subtitle =
                                "Total No. : " + fragmentSalesPersonBinding.rvFragmentSalesPersonDataList.adapter?.itemCount
                            lastPage = employee.data.last_page!!.toInt()
                        }
                        if (employee.data?.data!!.isNotEmpty())
                            context.ivDrawerToolbarIconsSearch.visibility = View.GONE
                        context.progressBarTouchable(true)
                        fragmentSalesPersonBinding.prlFragmentSalesPersonList.setRefreshing(false)
                    }
                }
                Status.ERROR -> {

                    context.progressBarTouchable(true)
                    context.snackBar(coordinatorLayout, it.error)
                    context.ivDrawerToolbarIconsSearch.visibility = View.GONE
                    fragmentSalesPersonBinding.prlFragmentSalesPersonList.setRefreshing(false)
                    if (pageNo > 1) {
                        newData = true
                        pageNo--
                    } else {
                        context.tvDrawerToolbarTitle.subtitle = ""
                    }
                    apiCall = 0
                    context.noInternetPopup(this, false)
                }
                Status.EMPTY -> {
                    context.progressBarTouchable(true)
                    context.ivDrawerToolbarIconsSearch.visibility = View.GONE
                    context.tvDrawerToolbarTitle.subtitle = "You don't have any Employee"
                    fragmentSalesPersonBinding.prlFragmentSalesPersonList.setRefreshing(false)
                }
                Status.LOADING -> {
                    context.progressBarTouchable(false)
                    context.ivDrawerToolbarIconsSearch.visibility = View.GONE
                    fragmentSalesPersonBinding.prlFragmentSalesPersonList.setRefreshing(false)
                }
                Status.FAIL -> {
                    fragmentSalesPersonBinding.tvListNoData.text = it.error.toString()
                    context.progressBarTouchable(true)
                    context.ivDrawerToolbarIconsSearch.visibility = View.GONE
                    context.tvDrawerToolbarTitle.subtitle = ""
                    fragmentSalesPersonBinding.prlFragmentSalesPersonList.setRefreshing(false)
                }
                Status.UNAUTHORISED -> {
                    context.progressBarTouchable(true)
                }
                else -> TODO()
            }
        })

        customerListViewModel.fetchSalesPersonData(pageNo.toString(), search)

        fragmentSalesPersonBinding.prlFragmentSalesPersonList.setOnRefreshListener {
            newData = false
            totalSize = 0
            currentSize = 0
            pageNo = 1
            context.tvDrawerToolbarTitle.title = "Employee"
            context.tvDrawerToolbarTitle.subtitle = ""
            customerListViewModel.fetchSalesPersonData(pageNo.toString(), search)
        }

        fragmentSalesPersonBinding.rvFragmentSalesPersonDataList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager =
                    LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager!!.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (nextPageCall && lastVisible == totalItemCount - 2 && totalSize != currentSize && pageNo != lastPage) {
                    newData = true
                    pageNo++
                    nextPageCall = false
                    customerListViewModel.updateSalesPersonList(pageNo.toString(), search)
                }
            }
        })

        fragmentSalesPersonBinding.fbFragmentSalesPersonAdd.setOnClickListener(View.OnClickListener {
            sharedPreferenceUtil.putValue(Constants.ACTIVITY_CUSTOMER, Constants.Add_CUSTOMER)

            val intent =
                Intent(activity, EmployeeAdd::class.java).putExtra(Constants.All_ID, "")
            startActivityForResult(intent, 101)

        })

        return fragmentSalesPersonBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                pageNo = 1
                newData = false
                totalSize = 0
                currentSize = 0
                customerListViewModel.fetchSalesPersonData(pageNo.toString(), search)
            }
        } catch (ex: Exception) {
            Toast.makeText(
                activity, ex.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferenceUtil.getBoolean(Constants.UPDATE_CUSTOMER_BOOLEAN, false)
            || sharedPreferenceUtil.getString(
                Constants.ACTIVITY_CUSTOMER,
                ""
            ) == Constants.LIST_CUSTOMER
        ) {
            pageNo = 1
            newData = false
            totalSize = 0
            currentSize = 0
            context.tvDrawerToolbarTitle.title = "Employee"
            context.tvDrawerToolbarTitle.subtitle = ""
            sharedPreferenceUtil.putValue(Constants.UPDATE_CUSTOMER_BOOLEAN, false)
            sharedPreferenceUtil.putValue(Constants.ACTIVITY_CUSTOMER, "")

            customerListViewModel.fetchSalesPersonData(pageNo.toString(), search)
        }
    }

    override fun onMethodCallback(
        sharedPreferenceUtil: SharedPreferenceUtil,
        context: Context,
        data: EmployeeData
    ) {
        sharedPreferenceUtil.putValue(Constants.ACTIVITY_CUSTOMER, "")

        context.startActivity(
            Intent(
                context,
                EmployeeDetails::class.java
            ).putExtra(Constants.All_ID, data.id.toString())
        )
    }

    override fun onDeleteAttachment(
        activity: Activity,
        attachmentName: String,
        employeeAddViewModel: EmployeeAddViewModel,
        attachmentId: String,
        customerId: String
    ) {

    }

    override fun noInternetRefreshClick() {
        newData = pageNo != 1

        if (newData) {
            customerListViewModel.fetchSalesPersonData(pageNo.toString(), search)
        } else {
            customerListViewModel.updateSalesPersonList(pageNo.toString(), search)
        }
    }

    override fun noInternetBackClick() {

    }
}