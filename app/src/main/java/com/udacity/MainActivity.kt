package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0
    private var downloadedFile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(CHANNEL_ID, getString(R.string.channel_name))

        binding.content.customButton.setOnClickListener {

            onDownloadClicked()
        }

    }

    private fun onDownloadClicked() {
        val udacity_checked = binding.content.radioUdacity.isChecked
        val glide_checked = binding.content.radioGlide.isChecked
        val retrofit_checked = binding.content.radioRetrofit.isChecked
        if (!udacity_checked && !glide_checked && !retrofit_checked) {
            Toast.makeText(applicationContext, R.string.download_none, Toast.LENGTH_SHORT)
                .show()
        } else {
            val downloadUri: String = if (udacity_checked) {
                downloadedFile = getString(R.string.download_udacity)
                URL_UDACITY
            } else if (glide_checked) {
                downloadedFile = getString(R.string.download_glide)
                URL_GLIDE
            } else {
                downloadedFile = getString(R.string.download_retrofit)
                URL_RETROFIT
            }
            binding.content.customButton.buttonState = ButtonState.Loading
            download(downloadUri)

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val status: String =
                if (id == downloadID) {
                    "Success"
                } else {
                    "Failed"
                }
            binding.content.customButton.buttonState = ButtonState.Completed
            Timber.d("Download finished")

            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.sendNotification(downloadedFile, status, applicationContext)
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
        internal const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
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
    }
}