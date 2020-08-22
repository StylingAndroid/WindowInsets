package com.stylingandroid.windowinsets

import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import com.stylingandroid.windowinsets.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val currentInsetTypes = mutableSetOf<Int>()
    private lateinit var binding: ActivityMainBinding

    private var currentWindowInsets: WindowInsetsCompat = WindowInsetsCompat.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            currentWindowInsets = windowInsets
            applyInsets()
        }

        val itemTypes = listOf(
            binding.toggleCaptionBar,
            binding.toggleIme,
            binding.toggleMandatorySystemGestures,
            binding.toggleStatusBars,
            binding.toggleSystemBars,
            binding.toggleSystemGestures,
            binding.toggleTappableElement
        )

        binding.toggleDecorFitSystemWindows.setOnCheckedChangeListener { _, checked ->
            WindowCompat.setDecorFitsSystemWindows(window, checked)
            binding.insetTypes.isEnabled = checked
            if (checked) {
                itemTypes.forEach { checkBox ->
                    checkBox.isChecked = false
                    checkBox.isEnabled = false
                }
                currentInsetTypes.clear()
            } else {
                itemTypes.forEach { checkBox ->
                    checkBox.isEnabled = true
                }
            }
        }
        binding.toggleCaptionBar.addChangeListener(Type.captionBar())
        binding.toggleIme.addChangeListener(Type.ime())
        binding.toggleMandatorySystemGestures.addChangeListener(Type.mandatorySystemGestures())
        binding.toggleStatusBars.addChangeListener(Type.statusBars())
        binding.toggleSystemBars.addChangeListener(Type.systemBars())
        binding.toggleSystemGestures.addChangeListener(Type.systemGestures())
        binding.toggleTappableElement.addChangeListener(Type.tappableElement())
    }

    private fun CheckBox.addChangeListener(type: Int) =
        setOnCheckedChangeListener { _, checked -> toggleType(type, checked) }

    private fun toggleType(type: Int, required: Boolean) {
        if (required) {
            currentInsetTypes.add(type)
        } else {
            currentInsetTypes.remove(type)
        }
        applyInsets()
    }

    private fun applyInsets(): WindowInsetsCompat {
        val currentInsetTypeMask = currentInsetTypes.fold(0) { accumulator, type ->
            accumulator or type
        }
        val insets = currentWindowInsets.getInsets(currentInsetTypeMask)
        binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(insets.left, insets.top, insets.right, insets.bottom)
        }
        return WindowInsetsCompat.Builder()
            .setInsets(currentInsetTypeMask, insets)
            .build()
    }
}
