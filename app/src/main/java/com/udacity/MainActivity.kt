package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(CHANNEL_ID, getString(R.string.channel_name))


        // TODO: Implement code below
        binding.content.customButton.setOnClickListener {
            val udacity_checked = binding.content.radioUdacity.isChecked
            val glide_checked = binding.content.radioGlide.isChecked
            val retrofit_checked = binding.content.radioRetrofit.isChecked
            if (!udacity_checked && !glide_checked && !retrofit_checked) {
                Toast.makeText(applicationContext, R.string.download_none, Toast.LENGTH_SHORT)
                    .show()
            } else {
                val downloadUri: String = if (udacity_checked) {
                    URL_UDACITY
                } else if (glide_checked) {
                    URL_GLIDE
                } else {
                    URL_RETROFIT
                }
                binding.content.customButton.buttonState = ButtonState.Loading
                download(downloadUri)

            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            binding.content.customButton.buttonState = ButtonState.Completed
            Toast.makeText(applicationContext, "Download finished", Toast.LENGTH_SHORT).show()
        }
    }

    private fun download(uri: String) {
        val request =
            DownloadManager.Request(Uri.parse(uri))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL_UDACITY =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RETROFIT =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            // TODO: Step 2.4 change importance
            NotificationManager.IMPORTANCE_LOW
        )// TODO: Step 2.6 disable badges for this channel
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description =
            getString(R.string.channel_name)

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)

        // TODO: Step 1.6 END create a channel
    }
}