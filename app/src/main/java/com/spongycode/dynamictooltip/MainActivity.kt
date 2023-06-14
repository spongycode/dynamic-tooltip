package com.spongycode.dynamictooltip

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.spongycode.dynamictooltip.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val itemsElement = listOf("Button 1", "Button 2", "Button 3", "Button 4", "Button 5")
        val adapterElement = ArrayAdapter(this, R.layout.list_item, itemsElement)
        (binding.tilElement.editText as? AutoCompleteTextView)?.setAdapter(adapterElement)

        val itemsPosition = listOf("BOTTOM", "TOP", "LEFT", "RIGHT")
        val adapterPosition = ArrayAdapter(this, R.layout.list_item, itemsPosition)
        (binding.tilPosition.editText as? AutoCompleteTextView)?.setAdapter(adapterPosition)



        binding.btnBackgroundColor.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, _ ->
                        binding.btnBackgroundColor.text = "#" + envelope.hexCode.toString()
                    })
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show()
        }

        binding.btnTextColor.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.confirm),
                    ColorEnvelopeListener { envelope, _ ->
                        binding.btnTextColor.text = "#" + envelope.hexCode.toString()
                    })
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show()
        }

        binding.btnPreviewTooltip.setOnClickListener {
            val intent = Intent(this, PreviewActivity::class.java)

            intent.putExtra("element", binding.tilElement.editText?.text.toString())
            intent.putExtra("position", binding.tilPosition.editText?.text.toString())
            intent.putExtra("tooltip_text", binding.tilTooltipText.editText?.text.toString())
            intent.putExtra("text_size", binding.tilTextSize.editText?.text.toString())
            intent.putExtra("padding", binding.tilPadding.editText?.text.toString())
            intent.putExtra("corner_radius", binding.tilCornerRadius.editText?.text.toString())
            intent.putExtra("tooltip_width", binding.tilTooltipWidth.editText?.text.toString())
            intent.putExtra("arrow_height", binding.tilArrowHeight.editText?.text.toString())
            intent.putExtra("arrow_width", binding.tilArrowWidth.editText?.text.toString())
            intent.putExtra("background_color", binding.btnBackgroundColor.text)
            intent.putExtra("text_color", binding.btnTextColor.text)

            startActivity(intent)
        }

        binding.btnFillDefault.setOnClickListener {
            fillDefault()
        }
    }

    private fun fillDefault() {
        (binding.tilElement.editText as? MaterialAutoCompleteTextView)?.setText("Button 1", false)
        (binding.tilPosition.editText as? MaterialAutoCompleteTextView)?.setText("BOTTOM", false)
        binding.tilTooltipText.editText?.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
        binding.tilTextSize.editText?.setText("15")
        binding.tilPadding.editText?.setText("25")
        binding.tilCornerRadius.editText?.setText("15")
        binding.tilTooltipWidth.editText?.setText("300")
        binding.tilArrowHeight.editText?.setText("25")
        binding.tilArrowWidth.editText?.setText("50")
        binding.btnTextColor.text = "#FFFFFF"
        binding.btnBackgroundColor.text = "#000000"
    }
}