package com.example.justtodoit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    private lateinit var googleApiClient: GoogleApiClient

    override fun onStart() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        googleApiClient.connect()
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add this to every activity if you wish to have the theme apply
        ThemeActivity.sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )

        when (ThemeActivity.sharedPreferences.getString(ThemeActivity.themeKey, "light")) {
            "light" -> theme.applyStyle(R.style.OverlayThemeLight, true)
            "dark" -> theme.applyStyle(R.style.OverlayThemeDark, true)
        }

        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
    }

    fun home(view: View) {
        startActivity(Intent(this,HomePageActivity::class.java))
        finish()
    }

    fun add(view: View) {
        startActivity(Intent(this,AddTaskActivity::class.java))
        finish()
    }

    fun plus(view: View) {
        startActivity(Intent(this, MembershipActivity::class.java))
        finish()
    }

    fun signOut(view: View) {
        auth.signOut()
        Auth.GoogleSignInApi.signOut(googleApiClient)
        val logoutIntent = Intent(this, LoginActivity::class.java)
        startActivity(logoutIntent)

    }

    fun theme(view: View) {
        startActivity(Intent(this, ThemeActivity::class.java))
        finish()
    }

    fun questions(view: View) {
        startActivity(Intent(this, FAQSettingsActivity::class.java))
        finish()
    }
}