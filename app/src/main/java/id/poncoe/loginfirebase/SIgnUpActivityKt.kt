package id.poncoe.loginfirebase

import android.app.Activity
import android.app.ProgressDialog
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
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SIgnUpActivityKt : Activity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignUp: Button? = null
    private var progressBar: ProgressBar? = null
    private var mProgressDialog: ProgressDialog? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.daftar_akun)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        btnSignUp = findViewById<View>(R.id.sign_up_button) as Button
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar

        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim { it <= ' ' }
            val password = inputPassword!!.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Masukan Alamat Email!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Masukan Password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(applicationContext, "Kata Sandi Terlalu Pendek, Masukan Minimal 6 Karakter!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar!!.visibility = View.VISIBLE
            //create user
            auth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SIgnUpActivityKt, OnCompleteListener<AuthResult> { task ->
                        Toast.makeText(this@SIgnUpActivityKt, "Terimakasih Sudah Melakukan Registrasi, Selamat Datang!", Toast.LENGTH_SHORT).show()
                        progressBar!!.visibility = View.GONE
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful) {
                            Toast.makeText(this@SIgnUpActivityKt, "Otentikasi Gagal." + task.exception!!,
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            showProgressDialog()
                            startActivity(Intent(this@SIgnUpActivityKt, MainActivity::class.java))
                            finish()
                        }
                    })
        })
    }

    private fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage(getString(R.string.loading))
            mProgressDialog!!.isIndeterminate = true
        }

        mProgressDialog!!.show()
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }
}
