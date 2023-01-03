package com.example.wagba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.data.User
import com.example.wagba.data.UserViewModel
import com.example.wagba.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email:String
    private lateinit var pass:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginbtn.setOnClickListener {
            email = binding.loginMail.text.toString().lowercase(Locale.ROOT)
            pass = binding.loginPassword.text.toString()

            if (authenticateUser()){
                login()
            }
        }
        binding.loginRegisterText.setOnClickListener {
            toActivity(RegisterActivity::class.java)
        }

    }


    private fun toActivity(activity: Class<*>){
        val intent = Intent(this,activity)
        startActivity(intent)
        finish()
    }

    private fun authenticateUser(): Boolean {
        // not empty check
        if (email.isNotEmpty() && pass.isNotEmpty()
        ) {
            return true
        }else {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT)
                .show()
        }
        return false
    }
    private fun login() {
        // creation of user
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                     insertDataToRoomDB()
                    toActivity(MainActivity::class.java)
                } else {
                    Toast.makeText(
                        this,
                        "Email or password is incorrect",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /*
    this function is used to insert data to room database
    if sign out feature is made, this function logic will need to be modified to save multiple users to the room database
    the current logic states that saving to local DB could either be done on login or on register, when user is logged in,
    his credentials are saved to local DB, when he logs out, his credentials are deleted from local DB
    */
    private fun insertDataToRoomDB() {
        val uid = firebaseAuth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("users").child(uid)
        val mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        var username = ""
        // get user from firebase and insert it into room db
        myRef.get().addOnSuccessListener {
            username = it.child("username").value.toString()
            val user = User(
                uid,
                email,
                username,
                it.child("phone").value.toString()
            )
            mUserViewModel.addUser(user)
        }
        // sign in successful toast
        Toast.makeText(this, "Welcome $username", Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            toActivity(MainActivity::class.java)
        }
    }
}