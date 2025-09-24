package com.example.donshy.ui.profile

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.donshy.R
import com.example.donshy.ViewModel.profile.ProfileModelFactory
import com.example.donshy.ViewModel.profile.ProfileViewModel
import com.example.donshy.databinding.ProfileFragmentBinding
import com.example.donshy.ui.login.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    // Gallery picker
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.saveImageFileLocally(uri = uri)
        }
    }

    // Camera capture
    private lateinit var photoUri: Uri
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.saveImageFileLocally(photoUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ProfileModelFactory(requireContext())
        )[ProfileViewModel::class.java]
        viewModel.loadAvatar()
        binding.lnLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.imgAvatar.setOnClickListener {
            showAvatarPickerDialog()
        }
        viewModel.isDeleted.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                binding.imgAvatar.setImageResource(R.drawable.icon_user)
            }
        }
        viewModel.savedProfileImage.observe(viewLifecycleOwner) { savedProfileImage ->
            if (savedProfileImage != null && savedProfileImage.exists()) {
                binding.imgAvatar.setImageURI(Uri.fromFile(savedProfileImage))
            }
        }
        viewModel.isLogout.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    private fun showAvatarPickerDialog() {
        val options =
            arrayOf(getString(R.string.choose_from_gallery), getString(R.string.take_a_photo))
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.select_avatar))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> checkAndRequestPermissions()
                }
            }.show()
    }

    // Permission launcher
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value == true }
            if (granted) {
                takePhotoWithCamera()
            } else {
                Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }


    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            takePhotoWithCamera()
        } else {
            requestPermissionsLauncher.launch(notGranted.toTypedArray())
        }
    }

    private fun takePhotoWithCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Avatar")
            put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )!!

        if (true) {
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT)
                .show()
        }

        cameraLauncher.launch(photoUri)
    }

    private fun pickImageFromGallery() {
        galleryLauncher.launch("image/*")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

