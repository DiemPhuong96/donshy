package com.example.donshy.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.donshy.R
import com.example.donshy.databinding.LoginFragmentBinding
import com.example.donshy.ui.MainActivity
import com.example.donshy.utils.Result
import android.graphics.Color
import android.text.Spanned
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class]
        val txtSignUpFullText = getString(R.string.do_not_have_account)
        val txtSignUp = "Sign up now"
        val spannableString = SpannableString(txtSignUpFullText)
        val startIndex = txtSignUpFullText.indexOf(txtSignUp)
        val endIndex = startIndex + txtSignUp.length
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                findNavController().navigate(R.id.action_LoginFragment_to_SignUpFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(requireContext(), R.color.blue)
            }

        }
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.signupRedirectText.text = spannableString
        binding.signupRedirectText.movementMethod = LinkMovementMethod.getInstance()
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
        }
        viewModel.loginState.observe(requireActivity()) { loginState ->
            when (loginState) {
                is Result.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progress.visibility = View.GONE
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                is Result.Error -> {
                    binding.txtLoginError.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.txtLoginError.text = loginState.message.toString()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}