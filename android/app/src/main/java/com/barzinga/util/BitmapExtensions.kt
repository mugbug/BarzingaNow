package com.barzinga.util

import android.graphics.*


fun Bitmap.forPrediction(): String {
    val scaledBitmap = Bitmap.createScaledBitmap(
        this,
        50,
        50,
        false
    )
//        val grayscaleBitmap = scaledBitmap.toGrayscale()

    val rgbConvertionConstant = 65535/255
    val rgbComponents = mutableListOf<Int>()


    // percorrer imagem
    (0 until scaledBitmap.height).forEach { verticalPosition ->
        (0 until scaledBitmap.width).forEach { horizontalPosition ->

            // pegar cada pixel
            // pegar compoentes rgb
            // converter rgb pra base 65535
            val color = scaledBitmap.getPixel(horizontalPosition, verticalPosition)
            val red = Color.red(color) * rgbConvertionConstant
            val green = Color.green(color) * rgbConvertionConstant
            val blue = Color.blue(color) * rgbConvertionConstant
            rgbComponents.addAll(listOf(red, green, blue))
        }
    }


    // concatenar todos separados por virgula
    return rgbComponents.joinToString(separator = ",")
}

fun Bitmap.toGrayscale(): Bitmap {
    val width: Int
    val height: Int
    height = this.height
    width = this.width

    val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(bmpGrayscale)
    val paint = Paint()
    val cm = ColorMatrix()
    cm.setSaturation(0f)
    val f = ColorMatrixColorFilter(cm)
    paint.colorFilter = f
    c.drawBitmap(this, 0f, 0f, paint)
    return bmpGrayscale
}