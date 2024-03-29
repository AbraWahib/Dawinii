package com.grad.dawinii.view.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grad.dawinii.databinding.FragmentSignUpBinding
import com.grad.dawinii.view.main.MainScreenActivity
import com.grad.dawinii.model.entities.User
import com.grad.dawinii.util.Constants
import com.grad.dawinii.util.Prevalent
import com.grad.dawinii.util.makeToast
import com.grad.dawinii.viewModel.AuthViewModel
import com.grad.dawinii.viewModel.LocalViewModel
import io.paperdb.Paper

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var localViewModel: LocalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Paper.init(context as Context)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        localViewModel = ViewModelProvider(this)[LocalViewModel::class.java]
        authViewModel.userMutableLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                getUser(it)
            }
        })
        localViewModel.userMutableLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                getUser(it)
            }
        })
        initView()
    }

    private fun getUser(it: User) {
        Prevalent.currentUser = it
        startActivity(Intent(context, MainScreenActivity::class.java))
        activity?.onBackPressed()
        activity?.finish()
    }

    override fun onResume() {
        super.onResume()
        val uid = Paper.book().read<String>(Constants.UID_PAPER_KEY)
        if (!uid.isNullOrEmpty()) {
            localViewModel.getLocalUser(uid)
        }
    }

    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            inputUserData()
        }
    }

    private fun inputUserData() {
        val name = "${binding.etFirstName.text.toString()} ${binding.etLastName.text.toString()}"
        val email = binding.etSignUpEmail.text.toString()
        val password = binding.etSignUpPassword.text.toString()
        val rePassword = binding.etSignUpRePassword.text.toString()
        val inputAge = binding.etSignUpAge.text.toString()
        var age:Int
        val gender = getGender()

        if (!(email.isNullOrEmpty() || name.isNullOrEmpty() || password.isNullOrEmpty() || rePassword.isNullOrEmpty() || inputAge.isNullOrEmpty())) {
            age = inputAge.toInt()
            if (age < 12) {
                makeToast(context, "Only +12")
            } else {
                if (password.equals(rePassword)){
                    val user = User(name = name, email = email, password = password, age = age, gender = gender)
                    signUp(user)
                } else {
                    makeToast(context, "Check Password Confirmation")
                }
            }
        } else {
            makeToast(context, "Empty Field!")
        }


    }

    private fun getGender(): String {
        var gender:String = ""
        when(binding.rbgGender.checkedRadioButtonId) {
            binding.rbMale.id -> gender = Constants.GENDER_MALE
            binding.rbFemale.id -> gender = Constants.GENDER_FEMALE
        }
        return gender
    }

    private fun signUp(user: User) {
        authViewModel.signUp(user)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SignUpFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}