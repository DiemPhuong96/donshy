package com.example.donshy.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.donshy.R
import com.example.donshy.databinding.SignUpFragmentBinding
import com.example.donshy.ui.MainActivity
import com.example.donshy.utils.Result

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignUpFragment : Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private lateinit var viewModel: SignUpViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, AuthViewModelFactory())[SignUpViewModel::class.java]
        val txtLoginFullText = getString(R.string.already_have_account)
        val txtLogin = "Login"
        val startIndex = txtLoginFullText.indexOf(txtLogin)
        val endIndex = startIndex + txtLogin.length
        val spannableString = SpannableString(txtLoginFullText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                findNavController().navigate(R.id.action_SignUpFragment_to_LoginFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = ContextCompat.getColor(requireContext(), R.color.blue)
            }
        }
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.loginRedirectText.text = spannableString
        binding.loginRedirectText.movementMethod = LinkMovementMethod.getInstance()
        binding.signupButton.setOnClickListener {
            val name = binding.fullNameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            val confirmPassword = binding.confirmPasswordInput.text.toString()
            var isValid = true
            if (name.isEmpty()) {
                binding.fullNameInput.error = getString(R.string.input_user_name)
                isValid = false
            }
            if (email.isEmpty()) {
                binding.emailInput.error = getString(R.string.input_email)
                isValid = false
            }
            if (password.isEmpty()) {
                binding.passwordInput.error = getString(R.string.password)
                isValid = false
            }
            if (confirmPassword.isEmpty()) {
                binding.confirmPasswordInput.error = getString(R.string.confirm_password)
                isValid = false
            } else if (confirmPassword != password) {
                binding.confirmPasswordInput.error =
                    getString(R.string.confirm_password_not_matching)
                isValid = false
            }

            if (isValid) {
                viewModel.register(name, email, password)
            }
        }
        viewModel.registerState.observe(requireActivity()) {state ->
            when (state) {
                is Result.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progress.visibility = View.GONE
                    binding.txtSignUpError.visibility = View.VISIBLE
                    binding.txtSignUpError.text = getString(R.string.verify_email, binding.emailInput.text.toString())
                    viewModel.startEmailCheckPolling()
                }
                is Result.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.txtSignUpError.visibility = View.VISIBLE
                    binding.txtSignUpError.text  = if (state.message.isNotEmpty())  state.message else getString(R.string.error_registration_failed)
                }
            }
        }
        viewModel.emailVerified.observe(requireActivity()) {verified ->
            if (verified) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}