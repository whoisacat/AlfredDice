package com.whoisacat.alfreddice

import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whoisacat.alfreddice.ui.theme.AlfredDiceTheme
import com.whoisacat.alfreddice.ShakeListener.OnShakeListener
import java.lang.RuntimeException

class MainActivity : ComponentActivity() {

    private var mNumber = 0
    private var mShakeListener: ShakeListener? = null
    private var mVibrator: Vibrator? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val savedInstanceState = Bundle()
        savedInstanceState.putInt(NUMBER, mNumber)
    }

    private fun setRandom() {
        val n = Math.random().toFloat() * 5
        mNumber = Math.round(n) + 1
    }

    private fun callVibro() {
        if (mVibrator!!.hasVibrator()) {
            mVibrator!!.vibrate(MILLS)
        }
    }

    override fun onPause() {
        super.onPause()
        mVibrator = null
    }

    override fun onResume() {
        super.onResume()
        if (mVibrator == null) {
            mVibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mShakeListener = ShakeListener(applicationContext)
        val listener = object : OnShakeListener {
            override fun onShake() {
                if (mVibrator != null) {
                    callVibro()
                }
                setRandom()
                setContent {
                    AlfredDiceTheme {
                        ShowDice(mNumber)
                    }
                }

            }
        }
        mShakeListener!!.setOnShakeListener(listener)
        setContent {
            InitScrean()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun InitScreanTest() {
        InitScrean()
    }

    @Composable
    private fun InitScrean() {
        AlfredDiceTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Play with me!!!",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colors.primary,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Test() {
        ShowDice(dice = 5)
    }

    @Composable
    fun ShowDice(dice: Int) {
        Surface(color = MaterialTheme.colors.background) {
            val painter = when(dice) {
                1 -> painterResource(R.drawable.dice1)
                2 -> painterResource(R.drawable.dice2)
                3 -> painterResource(R.drawable.dice3)
                4 -> painterResource(R.drawable.dice4)
                5 -> painterResource(R.drawable.dice5)
                6 -> painterResource(R.drawable.dice6)
                else -> throw RuntimeException("unappropriatedValue")
            }
            Image(
                painter,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    companion object {
        private const val MILLS = 250L
        private const val NUMBER = "number"
    }
}
