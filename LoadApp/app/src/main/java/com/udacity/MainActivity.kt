package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.microedition.khronos.opengles.GL


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloadManager: DownloadManager

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.glide_radio_button -> download(GLIDE_URL)
                R.id.project_radio_button -> download(PROJECT_URL)
                R.id.retrofit_radio_button -> download(RETROFIT_URL)
                else -> Toast.makeText(
                    this,
                    "Please select the file to download",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {
                Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show()
            }

            val contentIntent = Intent(applicationContext, DetailActivity::class.java)
            when (radioGroup.checkedRadioButtonId) {
                R.id.glide_radio_button -> contentIntent.putExtra(
                    FILENAME_EXTRA,
                    getString(R.string.glide_content_desc)
                )
                R.id.project_radio_button -> contentIntent.putExtra(
                    FILENAME_EXTRA,
                    getString(R.string.project_content_desc)
                )
                R.id.retrofit_radio_button -> contentIntent.putExtra(
                    FILENAME_EXTRA,
                    getString(R.string.retrofit_content_desc)
                )
            }

            val cursor: Cursor =
                downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            while (cursor.moveToNext()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        custom_button.buttonState = ButtonState.Completed
                        contentIntent.putExtra(STATUS_EXTRA, "Failed")
                        pendingIntent = PendingIntent.getActivity(
                            applicationContext,
                            NOTIFICATION_ID,
                            contentIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        notificationManager.sendNotification(
                            getString(R.string.notification_description),
                            applicationContext,
                            pendingIntent
                        )
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        custom_button.buttonState = ButtonState.Completed
                        contentIntent.putExtra(STATUS_EXTRA, "Success")
                        pendingIntent = PendingIntent.getActivity(
                            applicationContext,
                            NOTIFICATION_ID,
                            contentIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        notificationManager.sendNotification(
                            getString(R.string.notification_description),
                            applicationContext,
                            pendingIntent
                        )
                    }
                }
            }
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        custom_button.buttonState = ButtonState.Loading
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val PROJECT_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.YELLOW
                enableVibration(true)
                description = "Download Complete"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
