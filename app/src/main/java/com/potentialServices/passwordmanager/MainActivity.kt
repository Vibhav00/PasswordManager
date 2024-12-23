package com.potentialServices.passwordmanager

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.potentialServices.passwordmanager.activities.AddPasswordActivity
import com.potentialServices.passwordmanager.activities.AppPasswordActivity
import com.potentialServices.passwordmanager.adapters.ViewPagerAdapterMain
import com.potentialServices.passwordmanager.databinding.ActivityMainBinding
import com.potentialServices.passwordmanager.databinding.BottomFilterViewBinding
import com.potentialServices.passwordmanager.databinding.HamburgerHeaderBinding
import com.potentialServices.passwordmanager.databinding.SelectLanguageDialogueBinding
import com.potentialServices.passwordmanager.databinding.SelectThemeDialogueBinding
import com.potentialServices.passwordmanager.db.PasswordDatabase
import com.potentialServices.passwordmanager.repositories.PasswordRepository
import com.potentialServices.passwordmanager.utils.AppPasswordEvents
import com.potentialServices.passwordmanager.utils.MainActivityEvents
import com.potentialServices.passwordmanager.utils.PrintFeature
import com.potentialServices.passwordmanager.utils.SortingOder
import com.potentialServices.passwordmanager.utils.preferenceutils.PreferenceUtils
import com.potentialServices.passwordmanager.viewmodelprovider.MainViewModelProviderFactory
import com.potentialServices.passwordmanager.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.potentialServices.passwordmanager.db.PasswordDatabase.Companion.DATABASE_NAME
import com.potentialServices.passwordmanager.toast.PasswordManagerToast
import com.potentialServices.passwordmanager.utils.LargelyUsedFunctions.Companion.setLanguageToUi
import com.potentialServices.passwordmanager.utils.constants.Constants.BANGOLI
import com.potentialServices.passwordmanager.utils.constants.Constants.ENG
import com.potentialServices.passwordmanager.utils.constants.Constants.GUJRATI
import com.potentialServices.passwordmanager.utils.constants.Constants.HINDI
import com.potentialServices.passwordmanager.utils.constants.Constants.MARATHI
import com.potentialServices.passwordmanager.utils.constants.Constants.ORIYA
import com.potentialServices.passwordmanager.utils.constants.Constants.RUSSIAN
import com.potentialServices.passwordmanager.utils.constants.Constants.SECURE_KEY_LOC
import com.potentialServices.passwordmanager.utils.constants.Constants.SERIALIZABLE_EXTRA_KEY
import com.potentialServices.passwordmanager.utils.constants.Constants.TAMIL
import com.potentialServices.passwordmanager.utils.constants.Constants.TELGU
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStreamWriter
import java.util.Locale
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // private val themeTitleList = arrayOf("Light", "Dark", "Auto(System Default)")
    private lateinit var mainBinding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel
    private lateinit var selectLanguageDialogueBinding: SelectLanguageDialogueBinding
    private lateinit var selectThemeDialogueBinding: SelectThemeDialogueBinding
    private lateinit var db: PasswordDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * setting theme before super.onCreate
         * **/
        System.loadLibrary(SECURE_KEY_LOC)

        val savedTheme = getSavedThemePreference()
        setTheme(savedTheme)
        setLanguageToUi(this,PreferenceUtils.getSharedPreferences(this).getLang())

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setStatusBartextColor(savedTheme)
        mainBinding.searchTextContainer.visibility = View.GONE





        db = PasswordDatabase(this)
        /** creating the instance of the repository  **/
        var passwordRepository = PasswordRepository(db)

        /**  creating the instance of the main view model  **/
        var mainViewModelProviderFactory =
            MainViewModelProviderFactory(application, passwordRepository)
        mainViewModel =
            ViewModelProvider(this, mainViewModelProviderFactory)[MainViewModel::class.java]


        /**  tool bar **/
        setSupportActionBar(mainBinding.toolbar)
        /** toggler for the hamburger menu  **/
        val toggle = ActionBarDrawerToggle(
            this,
            mainBinding.drawer,
            mainBinding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        if (supportActionBar != null) {

            // Resolve the color from the theme using the attribute ?attr/myPrimaryTextColor
            val typedValue = TypedValue()
            this.theme.resolveAttribute(R.attr.myTabIndicatorColor, typedValue, true)
            val color = typedValue.data
            val togler = toggle.drawerArrowDrawable
            if (togler != null) {
                togler.color = color
            }
        }


        mainBinding.drawer.addDrawerListener(toggle)
        toggle.syncState()


        mainBinding.pager.adapter = ViewPagerAdapterMain(this)
        val hamburgerHeaderBinding =
            HamburgerHeaderBinding.bind(mainBinding.NavigationView.getHeaderView(0))
        hamburgerHeaderBinding.username.setText(
            PreferenceUtils.getSharedPreferences(this).getUsername()
        )
        mainBinding.NavigationView.setNavigationItemSelectedListener(this)



        mainBinding.searchIcon.setOnClickListener {
            if (mainBinding.searchTextContainer.visibility == View.GONE) {
                mainBinding.searchTextContainer.visibility = View.VISIBLE

            } else {
                mainBinding.searchTextContainer.visibility = View.GONE

            }


        }
        mainBinding.lockIcon.setOnClickListener {
            PasswordManagerToast.showToast(this,
                getString(R.string.shutting_down_the_app), Toast.LENGTH_SHORT)

            // Add a small delay before shutting down to allow the toast to show
            mainBinding.lockIcon.postDelayed({
                finishAffinity()  // Closes the app and all its activities
                System.exit(0)    // Optional: Stops the app's process
            }, 500)  // 1-second delay to show the toast
        }

        mainBinding.searchText.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                mainViewModel.handleMainEvents(MainActivityEvents.SearchEvent(it.toString()))
            } else {

                mainViewModel.handleMainEvents(MainActivityEvents.NoEvent())
            }
//            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
        mainBinding.sortIcon.setOnClickListener {
            mainBinding.searchTextContainer.visibility = View.GONE
            var event: MainActivityEvents = MainActivityEvents.SortByTitle(SortingOder.ASCENDING)
            var sortingOder: SortingOder = SortingOder.NONE
            var num = 0;
            val bottomSheetView = BottomFilterViewBinding.inflate(layoutInflater)
            val bottomSheet = BottomSheetDialog(this)
            bottomSheet.setContentView(bottomSheetView.root)
            bottomSheet.show()
            bottomSheetView.cgSortType.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.by_website -> {
                        num = 1
                    }

                    R.id.by_username -> {
                        num = 2
                    }

                    R.id.by_title -> {
                        num = 3
                    }
                }
            }
            bottomSheetView.cgSortAscDesc.setOnCheckedChangeListener { group, checkId ->
                when (checkId) {
                    R.id.ascending -> {
                        sortingOder = SortingOder.ASCENDING
                    }

                    R.id.descending -> {
                        sortingOder = SortingOder.DESCENDING
                    }
                }
            }
            bottomSheetView.btnCancel.setOnClickListener {
                bottomSheet.dismiss()
            }
            bottomSheetView.btnApply.setOnClickListener {
                when (sortingOder) {
                    SortingOder.ASCENDING, SortingOder.NONE -> {
                        when (num) {
                            1 -> {
                                event = MainActivityEvents.SortByWebsite(SortingOder.ASCENDING)
                            }

                            2 -> {
                                event = MainActivityEvents.SortByUsername(SortingOder.ASCENDING)
                            }

                            3, 0 -> {
                                event = MainActivityEvents.SortByTitle(SortingOder.ASCENDING)
                            }
                        }
                    }

                    SortingOder.DESCENDING -> {
                        when (num) {
                            1 -> {
                                event = MainActivityEvents.SortByWebsite(SortingOder.DESCENDING)
                            }

                            2 -> {
                                event = MainActivityEvents.SortByUsername(SortingOder.DESCENDING)
                            }

                            3, 0 -> {
                                event = MainActivityEvents.SortByTitle(SortingOder.DESCENDING)
                            }

                        }
                    }
                }
                mainViewModel.handleMainEvents(event)
                bottomSheet.dismiss()
            }
        }



        TabLayoutMediator(
            mainBinding.tableLayout, mainBinding.pager
        ) { tab: TabLayout.Tab, pos: Int ->
            when (pos) {
                0 -> tab.text = getString(R.string.all)
                1 -> tab.text = getString(R.string.liked)
                2 -> tab.text = getString(R.string.recent)
            }

        }.attach()

        mainBinding.addnewpassword.setOnClickListener {
            startActivity(Intent(this, AddPasswordActivity::class.java))
        }


        mainViewModel.passwordList.observe(this) {
            val all = it.size
            val liked = it.filter {
                it.liked
            }.size
            val recent = it.filter {
                it.lastTime > (System.currentTimeMillis() - 1000 * 60 * 60 * 5)
            }.size
            setHamburgurData(hamburgerHeaderBinding, all, liked, recent)
        }


    }

    private fun setStatusBartextColor(savedTheme: Int) {
      if(savedTheme !in arrayOf(R.style.RedTheme,R.style.darkTheme))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setHamburgurData(
        hamburgerHeaderBinding: HamburgerHeaderBinding,
        all: Int,
        liked: Int,
        recent: Int
    ) {
        hamburgerHeaderBinding.apply {
            allPasswordCount.text = all.toString()
            recentPasswordCount.text = recent.toString()
            likedPasswordCount.text = liked.toString()
        }
    }

    private fun getSavedThemePreference(): Int {
        return PreferenceUtils.getSharedPreferences(this).getTheme()
//        val preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
//        return preferences.getInt("theme", R.style.default_theme) // Replace R.style.DefaultTheme with your default theme
    }

//    private fun setLanguage(activity: AppCompatActivity, language: String) {
//        var local: Locale = Locale(language)
//        Locale.setDefault(local)
//        var configuration = activity.resources.configuration
//        configuration.locale = local
//        baseContext.resources.updateConfiguration(
//            configuration, baseContext.resources.displayMetrics
//        )
//    }


    override fun onBackPressed() {
        if (mainBinding.drawer.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language -> {
                setLanguage()

            }

            R.id.theme -> {
                setThemes()

            }

            R.id.save_database -> {
                if(backupDatabase(this, DATABASE_NAME)){
                    PasswordManagerToast.showToast(this,"Backup Successful",Toast.LENGTH_SHORT)
                    startActivity(Intent(this@MainActivity,MainActivity::class.java))
                    finish()
                }else{

                }
//                PasswordManagerToast.showToast(
//                    this,
//                    backupDatabase(this, DATABASE_NAME).toString(),
//                    Toast.LENGTH_SHORT
//                )
            }

            R.id.restore_database -> {
                readPermission()
            }

            R.id.export_csv -> {
                convertToCsv()
                //https://stackoverflow.com/questions/47593205/how-to-pass-custom-object-via-intent-in-kotlin
            }

            R.id.print_pdf -> {
                val pf = PrintFeature(this)
                pf.createPdf(mainViewModel.mainList)
            }

            R.id.passkey -> {
                val iHome = Intent(this, AppPasswordActivity::class.java)
                iHome.putExtra(SERIALIZABLE_EXTRA_KEY, AppPasswordEvents.CREATE_PASSWORD)
                startActivity(iHome)
            }

            R.id.pin -> {
                val iHome = Intent(this, AppPasswordActivity::class.java)
                iHome.putExtra(SERIALIZABLE_EXTRA_KEY, AppPasswordEvents.CREATE_PIN)
                startActivity(iHome)
            }

            R.id.fingerprint -> {
                showFingerprintLockDialog(this)
            }
        }


        mainBinding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showFingerprintLockDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.do_you_want_to_turn_on_fingerprint_lock))

        // "On" button
        builder.setPositiveButton(getString(R.string.on)) { dialog, _ ->
            // Handle the "On" action here
            PreferenceUtils.getSharedPreferences(this).setLockedByFingereprint(true)
            PreferenceUtils.getSharedPreferences(this).setLockedByFourPin(false)
            dialog.dismiss() // Close the dialog
        }

        // "Off" button
        builder.setNegativeButton(getString(R.string.off)) { dialog, _ ->
            // Handle the "Off" action here
            PreferenceUtils.getSharedPreferences(this).setLockedByFingereprint(false)
            dialog.dismiss() // Close the dialog
        }

        // Create and show the dialog
        val dialog: AlertDialog = builder.create()

        // Set background and animations
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.popup_background_all_corner
            )
        )
        dialog.window?.setWindowAnimations(R.style.ZoomDialogAnimation)

        // Show the dialog first
        dialog.show()

        // Resolve the color from the theme using the attribute ?attr/myPrimaryTextColor
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.myTabIndicatorColor, typedValue, true)
        val color = typedValue.data

        // Set button colors after the dialog is shown
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(color)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(color)
//        dialog.show()
    }


    private fun hasPermission(vararg permission: String): Boolean {
        return permission.all {
            ActivityCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }
        return ContextCompat.checkSelfPermission(
            this, permission.toString()
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun getFileType(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        return contentResolver.getType(uri)
    }


    fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        fileName = it.getString(displayNameIndex)
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.path?.let {
                val cut = it.lastIndexOf('/')
                if (cut != -1) {
                    it.substring(cut + 1)
                } else {
                    it
                }
            }
        }
        return fileName
    }


    private fun openFile(uri: Uri) {
        try {

            val fileType: String? = getFileType(this, uri)
            fileType?.let {
                // Use the file type as needed
            } ?: run {
                println("Unable to determine file type")
            }
            val name = getFileName(this, uri)

            // Get the content resolver
            val contentResolver: ContentResolver = contentResolver

            // Open an input stream to the content URI
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            val file = getDatabasePath(DATABASE_NAME)
            val parent = file.parent
            db.close()
            file.delete()

            // Create a new file
            val newFile = File(parent, DATABASE_NAME)
            val fileOutputStream = FileOutputStream(newFile)

            // Copy the content from the input stream to the output stream

            inputStream?.use { input ->
                fileOutputStream.use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to read or write file", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data
            Log.e("the result = %s", uri.toString())
            if (uri != null) {
                openFile(uri)
//                restartApp()
            }
        }
    }


    private fun restartApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntentId = 123456
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            pendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
        finish()
        exitProcess(0) // Terminate the process
//        finishAffinity() // Close all activities
    }
    // Helper function to write CSV data
    private fun writeCsvData(output: Appendable) {
        output.append("title,username,password,website,description\n")
        mainViewModel.mainList.forEach {
            output.append("${it.title},${it.userName},${it.password},${it.website},${it.description}\n")
        }
    }


    private fun convertToCsv() {

        var writer: FileWriter? = null
        try {
            val fileName = "pm_csv_${System.currentTimeMillis()}.csv"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 and above, save the file using MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    contentResolver.openOutputStream(it)?.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writeCsvData(writer)
                        }
                    }
                    PasswordManagerToast.showToast(this, "CSV file saved successfully in Downloads", Toast.LENGTH_SHORT)
                } ?: run {
                    PasswordManagerToast.showToast(this, "Error creating file", Toast.LENGTH_SHORT)
                }
            } else {
                // For Android 9 and below, save the file directly to the Downloads folder
                val backUpCsvPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .absolutePath + "/$fileName"
                val file = File(backUpCsvPath)
                file.writer().use { writer ->
                    writeCsvData(writer)
                }
                PasswordManagerToast.showToast(this, "CSV file saved successfully in Downloads", Toast.LENGTH_SHORT)
            }
        } catch (e: Exception) {
            PasswordManagerToast.showToast(this, "Failed to export CSV: ${e.message}", Toast.LENGTH_LONG)
            e.printStackTrace()
        }finally {
            writer?.close()
        }

    }


    private fun backupDatabase(context: Context, databaseName: String): Boolean {
        return try {
            // close the database
            db.close()

            // Close the database before backing up
            val dbPath = context.getDatabasePath(databaseName).absolutePath
            val backupFileName = "pm_backup_${System.currentTimeMillis()}.db"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 and above: Use MediaStore to save the backup in Downloads
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, backupFileName)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        FileInputStream(File(dbPath)).use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    true
                } ?: false
            } else {
                // Android 9 and below: Save directly to the Downloads folder
                val backupFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .absolutePath + "/$backupFileName"
                val currentDB = File(dbPath)
                val backupDB = File(backupFilePath)

                if (currentDB.exists()) {
                    FileInputStream(currentDB).channel.use { src ->
                        FileOutputStream(backupDB).channel.use { dst ->
                            dst.transferFrom(src, 0, src.size())
                        }
                    }
                    true
                } else {
                    false
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    /**
     * this is main function for importing database
     *
     * **/
//    fun restoreDatabase(context: Context, databaseName: String): Boolean {
//        return try {
//
//            db.close()
//            val backupDBPath =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/backup_$databaseName"
//            val currentDBPath = context.getDatabasePath(databaseName).absolutePath
//
//
//            val currentDB = File(currentDBPath)
//            val backupDB = File(backupDBPath)
//
//
//            if (backupDB.exists()) {
//                val src = FileInputStream(backupDB).channel
//                val dst = FileOutputStream(currentDB).channel
//                dst.transferFrom(src, 0, src.size())
//                src.close()
//                dst.close()
//                val currentDBPath = context.getDatabasePath(databaseName).absolutePath
//                Log.e("vibhav", currentDBPath)
//
//                Log.e("vibhav", currentDB.length().toString())
//                Toast.makeText(this@MainActivity, currentDBPath.toString(), Toast.LENGTH_SHORT)
//                    .show()
//                true
//            } else {
//                false
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            false
//        }
//    }

    private fun readPermission() {


        var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableListOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            mutableListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasPermission(*permissions.toTypedArray())) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 10)
        } else {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            val mimeTypes = arrayOf("text/csv","*/ics" ,"text/comma-separated-values")
            intent.setType("*/*")
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            10 -> {
                if (grantResults.all { it === PackageManager.PERMISSION_GRANTED }) {
                    PasswordManagerToast.showToast(this,
                        getString(R.string.permission_granted), Toast.LENGTH_SHORT)
                } else {
                    PasswordManagerToast.showToast(this,
                        getString(R.string.permission_required), Toast.LENGTH_SHORT)
                }
            }
        }
    }


    private fun setLanguage() {
        selectLanguageDialogueBinding = SelectLanguageDialogueBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(selectLanguageDialogueBinding.root)


        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER

        dialog.window!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pop_up_background
            )
        )

        dialog.window!!.attributes = lp



        dialog.show()


        selectLanguageDialogueBinding.saveLang.setOnClickListener {
            // Get the ID of the selected RadioButton from the RadioGroup
            val selectedLanguageCode =
                when (selectLanguageDialogueBinding.languageRadioGroup.checkedRadioButtonId) {
                    R.id.hindi_lang -> HINDI     // Hindi language code
                    R.id.oriya_lang -> ORIYA   // Oriya language code
                    R.id.bengali_lang -> BANGOLI   // Bengali language code
                    R.id.telugu_lang -> TELGU
                    R.id.gujarati_lang -> GUJRATI
                    R.id.tamil_lang ->TAMIL
                    R.id.marathi_lang -> MARATHI
                    R.id.russian_lang -> RUSSIAN
                    else ->ENG             // Default to English if no selection
                }

            // Determine the language name for the Toast message
            val languageName = when (selectedLanguageCode) {
                HINDI -> "Hindi"
                ORIYA   -> "Odia"
                BANGOLI -> "Bengali"
                TELGU -> "Telugu"
                GUJRATI-> "Gujarati"
                TAMIL -> "Tamil"
                MARATHI-> "Marathi"
                RUSSIAN -> "Russian"
                else -> "English"
            }

            PreferenceUtils.getSharedPreferences(this).setLang(selectedLanguageCode)
             // If a language is selected, apply the language and recreate the activity
            // if (selectedLanguageCode != "en") { // Only recreate if a different language is selected
            setLanguageToUi(this,selectedLanguageCode) // Apply the selected language
            PasswordManagerToast.showToast(this,
                languageName+getString(R.string.language_has_been_updated), Toast.LENGTH_SHORT)
//            Toast.makeText(this, "$languageName language has been updated", Toast.LENGTH_SHORT)
//                .show() // Show a Toast message with the language name
            recreate() // Recreate the activity to apply the language change
            dialog.dismiss()
        }
        selectLanguageDialogueBinding.cancelButton.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when cancel button is clicked

        }


    }

    private fun setThemes() {
        // Inflate the theme selection dialog layout
        selectThemeDialogueBinding = SelectThemeDialogueBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(selectThemeDialogueBinding.root)

        // Set dialog layout parameters
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.pop_up_background
            )
        )

        dialog.window!!.attributes = lp

        dialog.show()

        // Handle Save button click
        selectThemeDialogueBinding.saveTheme.setOnClickListener {
            val selectedTheme =
                when (selectThemeDialogueBinding.themeRadioGroup.checkedRadioButtonId) {
                    R.id.purple_theme -> R.style.PurpleFantasyTheme
                    R.id.pink_theme -> R.style.PinkTheme1
                    R.id.Orange_theme -> R.style.default_theme
                    R.id.green_theme -> R.style.GreenTheme
                    R.id.red_theme -> R.style.RedTheme
                    R.id.ocean_theme -> R.style.OceanBlueTheme
                    R.id.dark_theme -> R.style.darkTheme
                    else -> {
                        R.style.default_theme
                    }
                }

            // Save the selected theme
            saveThemePreference(selectedTheme)

//            // Apply the selected theme
            setTheme(selectedTheme)
            // Clear back stack and restart the activity with the new theme


            PasswordManagerToast.showToast(this,
                getString(R.string.theme_has_been_updated),Toast.LENGTH_SHORT)
            recreate() // Recreate the activity to apply the new theme
            dialog.dismiss()
        }

        // Handle Cancel button click
        selectThemeDialogueBinding.cancelButton.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog if the cancel button is clicked
        }
    }

    private fun saveThemePreference(themeId: Int) {
        if (PreferenceUtils.getSharedPreferences(this).setTheme(themeId)) {
//            PasswordManagerToast.showToast(this,
//                getString(R.string.theme_is_changed),Toast.LENGTH_SHORT)
        } else {
            PasswordManagerToast.showToast(this,
                getString(R.string.got_some_issue),Toast.LENGTH_SHORT)
        }

//        val preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
//        val editor = preferences.edit()
//        editor.putInt("theme", themeId)
//        editor.apply()
    }


    override fun onResume() {
        super.onResume()
        mainViewModel.getAllPass()
    }

}


//<!--main tab color-->
//<!--tab secondary color-->
//<!--tab text color-->
//<!--tab text selected color-->
//
//<!--mian background color-->
//
//<!--icon color-->
//<!--icon secondary color-->