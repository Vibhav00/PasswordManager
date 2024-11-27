package com.potentialServices.passwordmanager.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.potentialServices.passwordmanager.R
import com.potentialServices.passwordmanager.models.PasswordItem
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PrintFeature(val context: Context) {
    companion object{
        const val CHANNEL_ID = "download_channel"

    }


    private fun createSinglePage(pdfDocument: PdfDocument, pageNo:Int, list: List<PasswordItem>){
        // first page finished ..............
        // Define the page size (A4 size in points)
        var pageWidth = 595 // A4 size in points (8.27 inches)
        var pageHeight = 842 // A4 size in points (11.69 inches)
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNo).create()

        // Start a new page
        val page = pdfDocument.startPage(pageInfo)

        // Get the canvas to draw on the page
        val canvas = page.canvas
        canvas.drawColor(ContextCompat.getColor(this.context,R.color.mainColor))
//

//        var i=0;
//        var temp=pageNo-1;
//        var item=3;
//        var kk = temp*3
//        if(kk>list.size)
//            item=3-(kk-list.size)
//        var sp=80f
//        while (i<item){
//            createSingleDoc(pageWidth,canvas,sp)
//            sp += 250
//            i++
//        }


        var itemIndex= (pageNo-2)*3
        var i=0;
        var sp=80f
        while (i<3 && itemIndex<list.size){
            createSingleDoc(pageWidth,canvas,sp,list[itemIndex])
            itemIndex++;
            i++;
            sp += 250
        }

        // Finish the page
        pdfDocument.finishPage(page)


    }

    private fun createSingleDoc(pageWidth:Int, canvas: Canvas, startPoint:Float,passwordItem: PasswordItem){
        val paint = Paint()
        val customTypefaceNormal = ResourcesCompat.getFont(context, R.font.roboto_regular)
        paint.typeface = customTypefaceNormal
        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.textAlign = Paint.Align.CENTER // Align text to center

        val title = passwordItem.title
        paint.textSize=25f
        canvas.drawText(title, (pageWidth / 2).toFloat(), startPoint+30, paint)
        paint.textSize=20f
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Username :-  ", 70f, startPoint+80, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(passwordItem.userName, (pageWidth - 70).toFloat(), startPoint+80, paint)


        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Password :-  ", 70f, startPoint+110, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(passwordItem.password, (pageWidth - 70).toFloat(), startPoint+110, paint)



        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Website :-  ", 70f, startPoint+140, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(passwordItem.website, (pageWidth - 70).toFloat(), startPoint+140, paint)



        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Description :-  ", 70f, startPoint+170, paint)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(passwordItem.description, (pageWidth - 70).toFloat(), startPoint+170, paint)


        // Draw a rectangle
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRect(50f, startPoint, (pageWidth - 50).toFloat(), (startPoint+190), paint)
    }


    private fun createInitialPage(pdfDocument: PdfDocument, pageNo: Int){
        // Define the page size (A4 size in points)
        var pageWidth = 595 // A4 size in points (8.27 inches)
        var pageHeight = 842 // A4 size in points (11.69 inches)
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNo).create()

        // Start a new page
        var page = pdfDocument.startPage(pageInfo)

        // Get the canvas to draw on the page
        var canvas = page.canvas

        canvas.drawColor(ContextCompat.getColor(this.context,R.color.mainColor))

        // Draw content on the canvas
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.textAlign = Paint.Align.CENTER // Align text to center

        val constPaint=Paint()
        // Draw circles with gradients
//        val gradientPaint = Paint().apply {
//            isAntiAlias = true
//            shader = RadialGradient(
//                0f, 0f, 150f,
//                intArrayOf(ContextCompat.getColor(this@PrintFeature.context,R.color.secondaryColor), ContextCompat.getColor(this@PrintFeature.context,R.color.mainColor)), // Start and end colors
//                null,
//                Shader.TileMode.CLAMP
//            )
//        }
//        val gradientPaint2 = Paint().apply {
//            isAntiAlias = true
//            shader = RadialGradient(
//                595f, 700f, 100f,
//                intArrayOf(ContextCompat.getColor(this@PrintFeature.context,R.color.secondaryColor), ContextCompat.getColor(this@PrintFeature.context,R.color.mainColor)), // Start and end colors
//                 // Start and end colors
//                null,
//                Shader.TileMode.CLAMP
//            )
//        }
        constPaint.color = ContextCompat.getColor(this@PrintFeature.context,R.color.secondaryColor)
        val paintSecond = Paint()
        paintSecond.color = ContextCompat.getColor(this@PrintFeature.context,R.color.thirdColor)

        canvas.drawCircle(0f, 100f, 100f, constPaint)
        canvas.drawCircle(595f, 0f, 150f, paintSecond)
        canvas.drawCircle(90f, 842f, 80f, paintSecond)
        canvas.drawCircle(595f, 700f, 50f, constPaint)

        // Draw image
        val drawable = ContextCompat.getDrawable(context, R.drawable.new_logo)
        drawable?.setBounds(197, 321, 397, 521)
        drawable?.draw(canvas)

        // Draw text
        var title = "PASSWORD MANAGER"
        val fontId = R.font.roboto_bold // Replace with your font id
        val customTypefaceBold = ResourcesCompat.getFont(context, R.font.roboto_bold)
        val customTypefaceNormal = ResourcesCompat.getFont(context, R.font.roboto_regular)
        paint.typeface = customTypefaceBold
//        val typeface = Typeface.cre
        canvas.drawText(title, (pageWidth / 2).toFloat(), 590f, paint)

        // Draw additional text
        paint.typeface = customTypefaceNormal
        paint.textSize = 20f
        var infoText = "Please remember your master password to unlock PDF"
        if(pageNo!=1)
            infoText = "Thanks for using My app ❣️"
        canvas.drawText(infoText, (pageWidth / 2).toFloat(), 700f, paint)

        // Draw a line
        paint.strokeWidth = 4f
        canvas.drawLine(50f, 650f, (pageWidth - 50).toFloat(), 650f, paint)

        // Draw a rectangle
//        paint.style = Paint.Style.STROKE
//        canvas.drawRect(50f, 100f, (pageWidth - 50).toFloat(), 200f, paint)

        // Finish the page
        pdfDocument.finishPage(page)
    }


    fun createPdf(list:List<PasswordItem>) {
        // Create a new PdfDocument
        val pdfDocument = PdfDocument()


        createInitialPage(pdfDocument,1)

        val noOfPages = if (list.size%3==0) list.size/3 else list.size/3 +1
        var i=1;
        while (i<=noOfPages){
            createSinglePage(pdfDocument,i+1,list)
            i++;
        }

        createInitialPage(pdfDocument,noOfPages+1)


//        createSinlePage(pdfDocument,3)
//        createSinlePage(pdfDocument,4)





        // Save the PDF to a file
//        val directoryPath =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//        val file = File(directoryPath, "sample_${System.currentTimeMillis()}.pdf")



        try {


            val contentResolver = context.contentResolver

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 and above, use MediaStore to save the file in the Downloads folder
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "sample_${System.currentTimeMillis()}")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    contentResolver.openOutputStream(it)?.use { outputStream ->

                        pdfDocument.writeTo(outputStream)
                        pdfDocument.close()
                        outputStream.close()
//                        outputStream.write(fileContent)
//                        outputStream.flush()
                    }
                }
            } else {
                // For Android 9 and below, save the file directly to the Downloads directory
                val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                val file = File(directoryPath, "sample_${System.currentTimeMillis()}")
                FileOutputStream(file).use { outputStream ->

                    pdfDocument.writeTo(outputStream)
                    pdfDocument.close()
                    outputStream.close()
//                    outputStream.write(fileContent)
//                    outputStream.flush()
                }
            }







//            val outputStream = FileOutputStream(file)
//            pdfDocument.writeTo(outputStream)
//            pdfDocument.close()
//            outputStream.close()
//
//            val intent = Intent(Intent.ACTION_VIEW)
//            val pdfUri: Uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
//            intent.setDataAndType(pdfUri, "application/pdf")


            // Wrap the intent in a PendingIntent
//            val pendingIntent = PendingIntent.getActivity(
//                context,
//                0,
//                intent,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
//            )


            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, "Pdf Created ", NotificationManager.IMPORTANCE_LOW)
                notificationManager.createNotificationChannel(channel)
            }
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Pdf Created ")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setContentIntent(pendingIntent)
                .setOngoing(false)
            notificationManager.notify(1, notificationBuilder.build())




            Toast.makeText(context, "PDF saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

//    private fun createNotificationChannel(notificationManager: NotificationManager) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(CHANNEL_ID, "Pdf Created ", NotificationManager.IMPORTANCE_LOW)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

}