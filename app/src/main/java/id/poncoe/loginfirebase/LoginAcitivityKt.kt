package id.poncoe.loginfirebase

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginAcitivityKt : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var mProgressDialogLogin: ProgressDialog? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null) {
            startActivity(Intent(this@LoginAcitivityKt, MainActivity::class.java))
            finish()
        }

        // set the view now
        setContentView(R.layout.masuk)

        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        btnSignup = findViewById<View>(R.id.btn_signup) as Button
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnReset = findViewById<View>(R.id.btn_reset_password) as Button

        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar

        btnSignup!!.setOnClickListener { startActivity(Intent(this@LoginAcitivityKt, SignupActivity::class.java)) }

        btnReset!!.setOnClickListener { startActivity(Intent(this@LoginAcitivityKt, ResetPassword::class.java)) }

        btnLogin!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Masukan Alamat Email!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Masukan Password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar!!.visibility = View.VISIBLE

            //authenticate user
            auth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@LoginAcitivityKt) { task ->
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar!!.visibility = View.GONE
                        if (!task.isSuccessful) {
                            // there was an error
                            if (password.length < 6) {
                                inputPassword!!.error = getString(R.string.minimum_password)
                            } else {
                                Toast.makeText(this@LoginAcitivityKt, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            showProgressDialogLogin()
                            val intent = Intent(this@LoginAcitivityKt, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed:$connectionResult")
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                Log.e(TAG, "Google Sign In failed.")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mFirebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful)
                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(this@LoginAcitivityKt, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(Intent(this@LoginAcitivityKt, MainActivity::class.java))
                        finish()
                    }
                }

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()
    }

    private fun showProgressDialogLogin() {
        if (mProgressDialogLogin == null) {
            mProgressDialogLogin = ProgressDialog(this)
            mProgressDialogLogin!!.setMessage(getString(R.string.loading))
            mProgressDialogLogin!!.isIndeterminate = true
        }

        mProgressDialogLogin!!.show()
    }

    override fun onBackPressed() {

        this.finish()
    }

    companion object {

        private val TAG = "SignInActivity"
        private val RC_SIGN_IN = 9001
    }
}
