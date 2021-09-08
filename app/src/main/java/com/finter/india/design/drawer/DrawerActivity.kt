package com.finter.india.design.drawer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.finter.india.R
import com.finter.india.design.employee.list.Employee
import com.finter.india.utils.Constants
import com.finter.india.utils.SharedPreferenceUtil
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DrawerActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var context: Context

    lateinit var tvDrawerToolbarTitle: Toolbar
    private lateinit var flDrawerFrameLayout: FrameLayout
    private var fragmentBoolean = false

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val toolbar = findViewById<Toolbar>(R.id.tvDrawerToolbarTitle)
        setSupportActionBar(toolbar)

        context = this@DrawerActivity

        flDrawerFrameLayout = findViewById(R.id.flDrawerFrameLayout)
        tvDrawerToolbarTitle = findViewById(R.id.tvDrawerToolbarTitle)

        tvDrawerToolbarTitle.title = "Dashboard"
        tvDrawerToolbarTitle.subtitle = ""


        sharedPreferenceUtil.putValue(
            Constants.ACCOUNT_TOKEN,
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMTY4YjU5NTU2NzBlYmM3NTc0NmUzNDY5YzQwZGIzMDUzMDc4ZjJjMmM4Yjc2ZGIwODQ4M2ZjNmRmZWJjYTk4Mzc2ZDcwNmYyOTk3MmZhZWUiLCJpYXQiOjE2MzA5NDAxMzIsIm5iZiI6MTYzMDk0MDEzMiwiZXhwIjoxNjYyNDc2MTMyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.KWy0IsuqU8n6fyIzlqKt0qRil5gpYycGaqCRnB6n_ox0hJgtZLUOeUdDnUghD2p2zbfktFGkx9ZepNwVq_A1J_wwUQhMNDluaS76asjX-y7OGjdZtYFTTG0314VPty5231ou12vhwMrFNhWbK4BXticO2Y4QTzJAq8vAseTiR3uIPqOcwkUz7MI_S3McwusLoejxokgQ_spEYndJ07ab7yYJtOfXHUfiCXlWMldvFGmuRnmXZ_0HBhXPTrVvgT3fqU8fq0sS__YQNqX5zUOaC9LLsQ3FPl3BkEqZIXG2PZofMvjIkJpD9oVCJB5QD1rXzGRexOTyVdYADizezRkVhUm6SoT7rWg0RKmaPlGrQSAl6ksvtvOQkq9i_5QsbB7SU1yLhEQPZTUc-onNP3rDRRc4TFzN3WvQlp4OM-ZnSSX9uMB7OhIvdarhDWg7BUgF4QMwa-1McxukEZ3osrotKA_uug0A_vyvSZxV5n4XPbz8YhodXMBRMLwe--_xgjVGowiNI02UE4EABUOtUyOQ2NCpztStf54hl5UGAtCCI5feyGR5lTnl4fAhUfHgOCbSMEDxB1vAEQ4bbMaOq7Ky1o6ecOBNxvFs-XXMCUAIj4d5jigyHNWUHZ-YHpdf3whCIYULk7-Q0_X02-tOmjJAY6kJMBadhB9B0gGNZ6dvuXE"
        )
        sharedPreferenceUtil.putValue(
            Constants.FIRM_TOKEN,
            "eyJpdiI6Ik13cUd4cmk2MSt0N1cvUU5DVU9NSXc9PSIsInZhbHVlIjoiRUhOd0x4NVZuSnIvWnpmY05kSTRadz09IiwibWFjIjoiNjg1ZmM2OTE4MWQ4MWFjODZlN2Y2NjQyYmJlZDIzM2VmZWE3MTMyY2UwMTM3OTExMGVkODU5NjUxZmFjM2NhYyJ9"
        )
        sharedPreferenceUtil.putValue(Constants.CURRENT_YEAR_SERVER, "2021-04-01 / 2022-03-31")
        sharedPreferenceUtil.putValue(Constants.DEVICE_ID, "4a6067e29ac427ff")

        setFragment(Employee(), "Employee")

    }

    private fun setFragment(fragment1: Fragment, tag: String) {

        var data = ""
        if (intent.hasExtra("restart"))
            data = intent.extras?.getString("restart").toString()

        val bundle = Bundle()
        bundle.putString("restart", data)

        fragment1.arguments = bundle

        if (fragmentBoolean) {

            val fm: FragmentManager = supportFragmentManager
            val fragments: List<Fragment> = fm.fragments
            for (element in fragments) {

                if (tag != element.tag) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flDrawerFrameLayout, fragment1, tag)
                        .commit()
                    break
                }
            }
        } else {
            fragmentBoolean = true
            supportFragmentManager.beginTransaction()
                .replace(R.id.flDrawerFrameLayout, fragment1, tag)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun supportPopUp(view: View) {
        optionSupportNumber()
    }

    private fun makePhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel: $phoneNumber")
        startActivity(intent)
    }

    private fun makeEmail(email: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("mailto: $email")
        startActivity(intent)
    }

    private fun openWebsite(website: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val fulWebsite = "http://$website"
        intent.data = Uri.parse(fulWebsite)
        startActivity(intent)
    }

    private fun optionSupportNumber() {
        val options: Array<String> = arrayOf(
            "+91 985 985 99 22",
            "+91 985 985 99 33",
            "support@finter.in",
            "www.finter.in"
        )
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.support_center))
        builder.setItems(options) { dialog, item ->
            if (item == 0 || item == 1)
                makePhone(options[item])
            else if (item == 2)
                makeEmail(options[item])
            else if (item == 3)
                openWebsite(options[item])
            dialog.dismiss()
        }
        builder.show()
        builder.setCancelable(true)
    }
}