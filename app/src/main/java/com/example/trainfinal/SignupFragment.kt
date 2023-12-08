package com.example.trainfinal

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        val loginText = view.findViewById<TextView>(R.id.loginRedirect)
        val loginBtn = view.findViewById<Button>(R.id.loginBtn)
        val passwordView = view.findViewById<EditText>(R.id.signupPassword)
        val passwordConfirmationView = view.findViewById<EditText>(R.id.signupPasswordConfirmation)
        val emailView = view.findViewById<EditText>(R.id.signupEmail)
        auth = Firebase.auth
        database = Firebase.database.reference

        // Redirect to sign up
        loginText.setOnClickListener {parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, LoginFragment())
            commit()
        }}

        loginBtn.setOnClickListener {
            val email = emailView.text;
            val password = passwordView.text;
            val passwordConfirmation = passwordConfirmationView.text;

            if (TextUtils.isEmpty(email)){
                Toast.makeText(activity, "Email is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(activity, "Password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(passwordConfirmation)) {
                Toast.makeText(activity, "Password Confirmation is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!TextUtils.equals(password, passwordConfirmation)) {
                Toast.makeText(activity, "Password is not equal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Util.writeNewUser(user!!.uid,
                            UUID.randomUUID().toString(),
                            user.email.toString(),
                            database)
                        startActivity(Intent(activity, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            activity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }}