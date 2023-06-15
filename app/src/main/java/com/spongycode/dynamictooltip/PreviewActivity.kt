package com.spongycode.dynamictooltip

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import com.google.android.material.button.MaterialButton
import com.spongycode.dynamictooltip.Utils.dpToPx
import com.spongycode.dynamictooltip.Utils.getNotificationPanelHeight
import com.spongycode.dynamictooltip.Utils.getScreenDimensions
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

        getExtras()

        Handler().postDelayed({
            showNearby(getElement())
        }, 100)

    }

    private fun getExtras() {
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
                tooltipView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                showTooltipAtLocation(
                    tooltipBinding,
                    tooltipWindow,
                    btn
                )
            }
        })

        makeTooltip(tooltipBinding)

        tooltipWindow.showAtLocation(binding.root, Gravity.NO_GRAVITY, -1000, -1000)
    }

    private fun makeTooltip(tooltipBinding: TooltipLayoutBinding) {
        tooltipBinding.apply {
            cvTooltip.apply {
                layoutParams.width = tooltipWidth!!
                backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(backgroundColor))
                radius = cornerRadius!!
            }
            tvTooltipText.text = tooltipText!!
            tvTooltipText.setPadding(padding!!)
            tvTooltipText.textSize = textSize!!
            tvTooltipText.setTextColor(Color.parseColor(textColor))

            ivBottomArrow.visibility = GONE
            ivTopArrow.visibility = GONE
            ivLeftArrow.visibility = GONE
            ivRightArrow.visibility = GONE
        }

        when (position) {
            "BOTTOM" -> {
                tooltipBinding.ivTopArrow.apply {
                    visibility = VISIBLE
                    layoutParams.width = arrowWidth!!
                    layoutParams.height = arrowHeight!!
                    setColorFilter(Color.parseColor(backgroundColor))
                }
            }

            "TOP" -> {
                tooltipBinding.ivBottomArrow.apply {
                    visibility = VISIBLE
                    layoutParams.width = arrowWidth!!
                    layoutParams.height = arrowHeight!!
                    setColorFilter(Color.parseColor(backgroundColor))
                }
            }

            "RIGHT" -> {
                tooltipBinding.ivLeftArrow.apply {
                    visibility = VISIBLE
                    layoutParams.width = arrowWidth!!
                    layoutParams.height = arrowHeight!!
                    setColorFilter(Color.parseColor(backgroundColor))
                }
            }

            "LEFT" -> {
                tooltipBinding.ivRightArrow.apply {
                    visibility = VISIBLE
                    layoutParams.width = arrowWidth!!
                    layoutParams.height = arrowHeight!!
                    setColorFilter(Color.parseColor(backgroundColor))
                }
            }

            else -> Unit
        }
    }


    private fun showTooltipAtLocation(
        tooltipLayoutBinding: TooltipLayoutBinding,
        tooltipWindow: PopupWindow,
        btn: MaterialButton
    ) {
        val tooltipView = tooltipLayoutBinding.root
        val tooltipWidth = tooltipView.width
        val tooltipHeight = tooltipView.height

        val buttonLocation = IntArray(2)
        btn.getLocationOnScreen(buttonLocation)
        val buttonX = buttonLocation[0]
        val buttonY = buttonLocation[1]
        val screenDimensions = getScreenDimensions(this)

        // Arrow adjustment
        if (position == "TOP" || position == "BOTTOM") {
            val requiredSpaceRight = tooltipWidth / 2
            val availableSpaceRight = (buttonX + btn.width / 2)
            val pushedRight = requiredSpaceRight - availableSpaceRight
            val layoutParams: ConstraintLayout.LayoutParams = when (position) {
                "BOTTOM" -> tooltipLayoutBinding.ivTopArrow.layoutParams as ConstraintLayout.LayoutParams
                "TOP" -> tooltipLayoutBinding.ivBottomArrow.layoutParams as ConstraintLayout.LayoutParams
                else -> ConstraintLayout.LayoutParams(0, 0)
            }
            if (pushedRight > 0) {
                val offset =
                    (0.5 - ((pushedRight + dpToPx(this, 10f)) * 1.0) / tooltipWidth).toFloat()
                layoutParams.horizontalBias = offset
                tooltipLayoutBinding.ivTopArrow.layoutParams = layoutParams
            }

            val screenWidth = screenDimensions.first
            val requiredSpaceLeft = tooltipWidth / 2 - btn.width / 2
            val availableSpaceLeft = screenWidth - (buttonX + btn.width)
            val pushedLeft = requiredSpaceLeft - availableSpaceLeft
            if (pushedLeft > 0) {
                val offset =
                    (0.5 + ((pushedLeft + dpToPx(this, 10f)) * 1.0) / tooltipWidth).toFloat()
                layoutParams.horizontalBias = offset
                tooltipLayoutBinding.ivTopArrow.layoutParams = layoutParams
            }

        }

        if (position == "LEFT" || position == "RIGHT") {
            val requiredSpaceUp = tooltipHeight / 2
            val availableSpaceUp = buttonY + btn.height / 2 - getNotificationPanelHeight(this)
            val pushedUp = requiredSpaceUp - availableSpaceUp
            val layoutParams: ConstraintLayout.LayoutParams = when (position) {
                "LEFT" -> tooltipLayoutBinding.ivRightArrow.layoutParams as ConstraintLayout.LayoutParams
                "RIGHT" -> tooltipLayoutBinding.ivLeftArrow.layoutParams as ConstraintLayout.LayoutParams
                else -> ConstraintLayout.LayoutParams(0, 0)
            }
            if (pushedUp > 0) {
                val offset =
                    (0.5 - ((pushedUp + dpToPx(this, 10f)) * 1.0) / tooltipHeight).toFloat()
                layoutParams.verticalBias = offset
                tooltipLayoutBinding.ivTopArrow.layoutParams = layoutParams
            }

            val screenHeight = screenDimensions.second
            val requiredSpaceDown = tooltipHeight / 2
            val availableSpaceDown = screenHeight - (buttonY + btn.height / 2)
            val pushedDown = requiredSpaceDown - availableSpaceDown
            if (pushedDown > 0) {
                val offset =
                    (0.5 + ((pushedDown + dpToPx(this, 10f)) * 1.0) / tooltipHeight).toFloat()
                layoutParams.verticalBias = offset
                tooltipLayoutBinding.ivTopArrow.layoutParams = layoutParams
            }

        }

        // Placing the tooltip
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