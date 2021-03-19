package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Enemy(context: Context) {
    var enemyX: Int = 0
    var enemyY: Int = 0

    var image: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
    var enemyBitmap: Bitmap = Bitmap.createScaledBitmap(image, 100, 100, false)
}