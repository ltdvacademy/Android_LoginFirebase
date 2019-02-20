package id.poncoe.loginfirebase

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivityKt : AppCompatActivity() {

    private var signOut: Button? = null
    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get firebase auth instance
        auth = FirebaseAuth.getInstance()

        //get current user
        val user = FirebaseAuth.getInstance().currentUser

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivityKt, LoginActivity::class.java))
                finish()
            }
        }

        signOut = findViewById<View>(R.id.logout) as Button

        signOut!!.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivityKt)
            builder.setMessage("Apakah Anda Yakin Ingin Logout?").setCancelable(false)
                    // tidak bisa tekan tombol back
                    // jika pilih yess
                    .setPositiveButton("Iya"
                    ) { dialog, id -> signOut() }
                    // jika pilih no
                    .setNegativeButton("Batal Logout"
                    ) { dialog, id -> dialog.cancel() }.show()
        }

    }

    //sign out method
    fun signOut() {
        auth!!.signOut()
    }

    override fun onStop() {

        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    override fun onBackPressed() {
        finish()
    }
}