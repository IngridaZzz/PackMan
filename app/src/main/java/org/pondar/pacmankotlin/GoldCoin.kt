package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

//Here you need to fill out what should be in a GoldCoin and what should the constructor be
class GoldCoin(context: Context) {

    var coinX: Int = 0
    var coinY: Int = 0

    var image: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.coin)
    var goldCoinBitmap: Bitmap = Bitmap.createScaledBitmap(image, 80, 80, false)

    var taken: Boolean = false

}