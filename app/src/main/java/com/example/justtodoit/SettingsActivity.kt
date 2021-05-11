package com.example.justtodoit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

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
            "pastel" -> theme.applyStyle(R.style.OverlayThemePastel, true)
            "blue" -> theme.applyStyle(R.style.OverlayThemeBlue, true)
            "red" -> theme.applyStyle(R.style.OverlayThemeRed, true)
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

    fun notifications(view: View){
        startActivity(Intent(this, NotificationActivity::class.java))
        finish()
    }

    fun theme(view: View) {
        startActivity(Intent(this, ThemeActivity::class.java))
        finish()
    }

    fun questions(view: View) {
        startActivity(Intent(this, FAQSettingsActivity::class.java))
        finish()
    }

    fun delete(view: View) {
        var alert = AlertDialog.Builder(this)
        alert.setNegativeButton(R.string.back_string, null)
        alert.setPositiveButton(R.string.confirm) { dialogInterface: DialogInterface, i: Int ->
            val user = Firebase.auth.currentUser!!
            var myRef = FirebaseDatabase.getInstance().getReference("userTasks").child(auth.currentUser.uid)
            myRef.removeValue()
            var myRef2 = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser.uid)
            myRef2.removeValue()
            user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                        }
                    }
        }
        alert.setCancelable(true)
        alert.setTitle("Are you sure you want to delete your account?")
        var alert2 = alert.create()
        alert2.show()
        alert2.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.RED)

    }
}