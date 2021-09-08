package com.finter.india.utils.cameragallerypopup

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.finter.india.R

class CameraAndGalleryPopUp {

    fun showPopupWindow(
        view: View,
        cameraAndGalleryInterface: CameraGalleryInterface
    ) {
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.element_image_selection_capture, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        val popupWindow = PopupWindow(popupView, width, height, true)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val llElementImageSelectionCaptureCamera =
            popupView.findViewById<LinearLayout>(R.id.llElementImageSelectionCaptureCamera)
        val llElementImageSelectionCaptureGallery =
            popupView.findViewById<LinearLayout>(R.id.llElementImageSelectionCaptureGallery)
        val btElementImageSelectionCaptureCancel =
            popupView.findViewById<Button>(R.id.btElementImageSelectionCaptureCancel)

        btElementImageSelectionCaptureCancel.setOnClickListener { popupWindow.dismiss() }

        llElementImageSelectionCaptureCamera.setOnClickListener {
            cameraAndGalleryInterface.onCameraClickListener()
            popupWindow.dismiss()
        }

        llElementImageSelectionCaptureGallery.setOnClickListener {
            cameraAndGalleryInterface.onGalleryClickListener()
            popupWindow.dismiss()
        }

//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                //Close the window when clicked
//                popupWindow.dismiss();
//                return true;
//            }
//        });
    }
}