package id.poncoe.loginfirebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordKt : Activity() {

    private var inputEmail: EditText? = null
    private var btnReset: Button? = null
    private val btnBack: Button? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.resetpassword)

        inputEmail = findViewById<View>(R.id.email) as EditText
        btnReset = findViewById<View>(R.id.btn_reset_password) as Button
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar

        auth = FirebaseAuth.getInstance()

        btnReset!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(application, "Masukan Email Anda", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar!!.visibility = View.VISIBLE
            auth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ResetPasswordKt, "Kami telah mengirim petunjuk untuk mereset kata sandi Anda!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@ResetPasswordKt, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@ResetPasswordKt, "Gagal mengirim email atau Email Anda Tidak Terdaftar. Silahkan Coba Lagi!", Toast.LENGTH_SHORT).show()
                        }

                        progressBar!!.visibility = View.GONE
                    }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

}
