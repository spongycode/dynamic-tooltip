package com.spongycode.dynamictooltip

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.PopupWindow

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.button.MaterialButton
import com.spongycode.dynamictooltip.databinding.ActivityPreviewBinding
import com.spongycode.dynamictooltip.databinding.TooltipLayoutBinding


class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding

    private var element: String? = null
    private var position: String? = null
    private var tooltipText: String? = null
    private var textSize: Float? = null
    private var padding: Int? = null
    private var cornerRadius: Float? = null
    private var tooltipWidth: Int? = null
    private var arrowHeight: Int? = null
    private var arrowWidth: Int? = null
    private var backgroundColor: String? = null
    private var textColor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        element = intent.getStringExtra("element")
        position = intent.getStringExtra("position")
        tooltipText = intent.getStringExtra("tooltip_text")
        textSize = intent.getStringExtra("text_size")?.toFloat()
        padding = intent.getStringExtra("padding")?.toInt()
        cornerRadius = intent.getStringExtra("corner_radius")?.toFloat()
        tooltipWidth = intent.getStringExtra("tooltip_width")?.toInt()
        arrowHeight = intent.getStringExtra("arrow_height")?.toInt()
        arrowWidth = intent.getStringExtra("arrow_width")?.toInt()
        backgroundColor = intent.getStringExtra("background_color")
        textColor = intent.getStringExtra("text_color")


        val handler = Handler()
        handler.postDelayed({
            showNearby(getElement())
        }, 100)

    }

    private fun getElement(): MaterialButton {
        val buttonMap = hashMapOf(
            "Button 1" to binding.btn1,
            "Button 2" to binding.btn2,
            "Button 3" to binding.btn3,
            "Button 4" to binding.btn4,
            "Button 5" to binding.btn5
        )
        return buttonMap[element]!!
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
        tooltipBinding.cvTooltip.layoutParams.width = tooltipWidth!!
        tooltipBinding.cvTooltip.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(backgroundColor))
        tooltipBinding.cvTooltip.radius = cornerRadius!!
        tooltipBinding.tvTooltipText.text = tooltipText!!
        tooltipBinding.tvTooltipText.setPadding(padding!!)
        tooltipBinding.tvTooltipText.textSize = textSize!!
        tooltipBinding.tvTooltipText.setTextColor(Color.parseColor(textColor))
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
            "BOTTOM" -> {
                tooltipView.ivTopArrow.visibility = VISIBLE
                tooltipView.ivTopArrow.layoutParams.width = arrowWidth!!
                tooltipView.ivTopArrow.layoutParams.height = arrowHeight!!
                tooltipView.ivTopArrow.setColorFilter(Color.parseColor(backgroundColor))
            }

            "TOP" -> {
                tooltipView.ivBottomArrow.visibility = VISIBLE
                tooltipView.ivBottomArrow.layoutParams.width = arrowWidth!!
                tooltipView.ivBottomArrow.layoutParams.height = arrowHeight!!
                tooltipView.ivBottomArrow.setColorFilter(Color.parseColor(backgroundColor))
            }

            "RIGHT" -> {
                tooltipView.ivLeftArrow.visibility = VISIBLE
                tooltipView.ivLeftArrow.layoutParams.width = arrowWidth!!
                tooltipView.ivLeftArrow.layoutParams.height = arrowHeight!!
                tooltipView.ivLeftArrow.setColorFilter(Color.parseColor(backgroundColor))
            }

            "LEFT" -> {
                tooltipView.ivRightArrow.visibility = VISIBLE
                tooltipView.ivRightArrow.layoutParams.width = arrowWidth!!
                tooltipView.ivRightArrow.layoutParams.height = arrowHeight!!
                tooltipView.ivRightArrow.setColorFilter(Color.parseColor(backgroundColor))
            }

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