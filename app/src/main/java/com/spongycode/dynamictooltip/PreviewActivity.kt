package com.spongycode.dynamictooltip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.PopupWindow
import com.google.android.material.button.MaterialButton
import com.spongycode.dynamictooltip.databinding.ActivityPreviewBinding
import com.spongycode.dynamictooltip.databinding.TooltipLayoutBinding

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var position: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        position = "BOTTOM"
        val handler = Handler()
        handler.postDelayed({
            showNearby(binding.btn3)
        }, 100)

    }


    private fun showNearby(btn: MaterialButton) {
        val tooltipBinding = TooltipLayoutBinding.inflate(layoutInflater)
        val tooltipView = tooltipBinding.root
        val tooltipWindow = PopupWindow(
            tooltipView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tooltipView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val tooltipWidth = tooltipView.width
                val tooltipHeight = tooltipView.height
                Log.d("Preview", "tooltipWidth $tooltipWidth")
                Log.d("Preview", "tooltipHeight $tooltipHeight")
                tooltipView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                showTooltipAtLocation(tooltipWindow, tooltipWidth, tooltipHeight, btn)
            }
        })
        getArrow(tooltipBinding)
        tooltipWindow.showAtLocation(
            binding.root, Gravity.NO_GRAVITY, -1000, -1000
        )
    }

    private fun getArrow(tooltipView: TooltipLayoutBinding) {
        tooltipView.ivBottomArrow.visibility = GONE
        tooltipView.ivTopArrow.visibility = GONE
        tooltipView.ivLeftArrow.visibility = GONE
        tooltipView.ivRightArrow.visibility = GONE
        when (position) {
            "BOTTOM" -> tooltipView.ivTopArrow.visibility = VISIBLE
            "TOP" -> tooltipView.ivBottomArrow.visibility = VISIBLE
            "RIGHT" -> tooltipView.ivLeftArrow.visibility = VISIBLE
            "LEFT" -> tooltipView.ivRightArrow.visibility = VISIBLE
            else -> Unit
        }
    }

    private fun showTooltipAtLocation(
        tooltipWindow: PopupWindow,
        tooltipWidth: Int,
        tooltipHeight: Int,
        btn: MaterialButton
    ) {
        val buttonLocation = IntArray(2)
        btn.getLocationOnScreen(buttonLocation)
        val buttonX = buttonLocation[0]
        val buttonY = buttonLocation[1]
        when (position) {

            "BOTTOM" -> {
                tooltipWindow.update(
                    buttonX - tooltipWidth / 2 + btn.width / 2,
                    buttonY + btn.height,
                    -1,
                    -1
                )
            }

            "TOP" -> {
                tooltipWindow.update(
                    buttonX - tooltipWidth / 2 + btn.width / 2,
                    buttonY - tooltipHeight,
                    -1,
                    -1
                )
            }

            "RIGHT" -> {
                tooltipWindow.update(
                    buttonX + btn.width,
                    buttonY - tooltipHeight / 2 + btn.height / 2,
                    -1,
                    -1
                )
            }

            "LEFT" -> {
                tooltipWindow.update(
                    buttonX - tooltipWidth,
                    buttonY - tooltipHeight / 2 + btn.height / 2,
                    -1,
                    -1
                )
            }

            else -> Unit
        }


    }
}