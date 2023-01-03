package com.example.wagba

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.wagba.data.User
import com.example.wagba.data.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Setting : Fragment() {

    private lateinit var newUsenameTv : TextView
    private lateinit var newPhoneTv : TextView
    private lateinit var newPasswordTv : TextView
    private lateinit var confirmNewPasswordTv : TextView

    private lateinit var updateUsernameBtn: Button
    private lateinit var updatePhoneBtn: Button
    private lateinit var updatePasswordBtn: Button
    private lateinit var logoutBtn: Button

    private lateinit  var mUserViewModel : UserViewModel
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance("https://wagba-db-default-rtdb.firebaseio.com/")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newUsenameTv = view.findViewById(R.id.newUsername)
        newPhoneTv = view.findViewById(R.id.newPhone)
        newPasswordTv = view.findViewById(R.id.newPassword)
        confirmNewPasswordTv = view.findViewById(R.id.confirmNewPassword)

        updateUsernameBtn = view.findViewById(R.id.updateUsernameBtn)
        updatePhoneBtn = view.findViewById(R.id.updatePhoneBtn)
        updatePasswordBtn = view.findViewById(R.id.updatePasswordBtn)
        logoutBtn = view.findViewById(R.id.logoutBtn)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        updateUsernameBtn.setOnClickListener {
            updateUsername()
        }
        updatePhoneBtn.setOnClickListener {
            updatePhone()
        }
        updatePasswordBtn.setOnClickListener {
            updatePassword()
        }
        logoutBtn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        firebaseAuth.signOut()
        EmptyRoomDB()
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        toLoginActivity()
    }

    private fun EmptyRoomDB() {
        mUserViewModel.deleteAllUsers()
    }

    private fun toLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun updatePassword() {
        val newPassword = newPasswordTv.text.toString()
        val confirmNewPassword = confirmNewPasswordTv.text.toString()

        if (newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()){
            if (newPassword == confirmNewPassword){
                val user = firebaseAuth.currentUser
                user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Password update failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUsername() {
        val newUsername = newUsenameTv.text.toString()
        if (newUsername.isEmpty()) {
            Toast.makeText(context, "Please enter a valid username", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = firebaseAuth.currentUser!!.uid
        val userRef = database.getReference("users/$userId")
        try {
            userRef.child("username").setValue(newUsername)
            Toast.makeText(context, "Username updated", Toast.LENGTH_SHORT).show()
            updateRoomDB(userId)
            toDeliveryFragment()
        }catch (e: Exception){
            Toast.makeText(context, "Error updating username", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toDeliveryFragment() {
        val deliveryFragment = Delivery()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, deliveryFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun updatePhone() {
        val newPhone = newPhoneTv.text.toString()
        if (newPhone.isEmpty()) {
            Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = firebaseAuth.currentUser!!.uid
        try {
            val userRef = database.getReference("users/$userId")
            userRef.child("phone").setValue(newPhone)
            Toast.makeText(context, "Phone updated", Toast.LENGTH_SHORT).show()
            updateRoomDB(userId)
            toDeliveryFragment()

        }catch (e: Exception){
            Toast.makeText(context, "Error updating phone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRoomDB(userId: String){
        val userRef = database.getReference("users/$userId")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = User(
                    userId,
                    dataSnapshot.child("email").value.toString(),
                    dataSnapshot.child("username").value.toString(),
                    dataSnapshot.child("phone").value.toString(),
                )
                //mUserViewModel.updateUser(user)
                //Toast.makeText(requireContext(), "User Updated", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "User not found in DB", error.toException())
            }
        })
    }
}