package com.example.wagba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.data.User
import com.example.wagba.data.UserViewModel
import com.example.wagba.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var username:String
    private lateinit var email:String
    private lateinit var phone:String
    private lateinit var pass:String
    private lateinit var confirmPass:String

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.registertologintext.setOnClickListener {
            toLogin()

        }
        binding.registerbtn.setOnClickListener {
            username = binding.registerUsername.text.toString().lowercase(Locale.ROOT)
            email = binding.registerMail.text.toString().lowercase(Locale.ROOT)
            phone = binding.registerPhone.text.toString()
            pass = binding.registerPassword.text.toString()
            confirmPass = binding.registerPassword2.text.toString()

            if (authenticateUser()){
                register()
            }
        }
    }

    private fun toLogin(){
        // start login activity and destroy this one
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun authenticateUser(): Boolean {
        // not empty check
        if (username.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()
            && confirmPass.isNotEmpty()
        ) {
            if (email.matches(".+@eng\\.asu\\.edu\\.eg".toRegex())) {
                // valid domain

                // passwords match
                if (pass == confirmPass) {
                    return true
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    "@eng.asu.edu domain is only allowed currently",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT)
                .show()
        }
        return false
    }

    private fun register(){
        // creation of user
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // user created successfully so we create user in database with username basket and orders history
                    insertDataToRealTimeDB()
                    insertDataToRoomDB()

                    toLogin()

                } else {
                    Toast.makeText(
                        this,
                        it.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun insertDataToRealTimeDB() {
        val uid = firebaseAuth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("users").child(uid)
        myRef.setValue("")
        myRef.child("username").setValue(username)
        myRef.child("email").setValue(email)
        myRef.child("phone").setValue(phone)
        myRef.child("Orders").setValue("")
    }

    private fun insertDataToRoomDB() {
        val uid = firebaseAuth.currentUser!!.uid
        val user = User(uid, email, username, phone)
        mUserViewModel.addUser(user)
        // register complete toast
        Toast.makeText(this, "Register complete!", Toast.LENGTH_SHORT).show()
    }

}