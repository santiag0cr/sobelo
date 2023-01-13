package com.ultimate.ultimatesophos.presentation.view.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.projects.sophosapp.R
import com.projects.sophosapp.databinding.FragmentSendDocumentsBinding
import com.ultimate.ultimatesophos.core.DocumentConverterHelper
import com.ultimate.ultimatesophos.core.hideKeyboard
import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.domain.definitions.Constants.CC
import com.ultimate.ultimatesophos.domain.definitions.Constants.CE
import com.ultimate.ultimatesophos.domain.definitions.Constants.OFFICES_INFO
import com.ultimate.ultimatesophos.domain.definitions.Constants.PA
import com.ultimate.ultimatesophos.domain.definitions.Constants.TI
import com.ultimate.ultimatesophos.domain.definitions.Constants.USER_EMAIL
import com.ultimate.ultimatesophos.domain.models.OfficesResponseDto

class SendDocumentsFragment : Fragment() {

    interface SendDocumentsFragmentListener {
        fun showMessageFromSendDocuments(
            title: String,
            message: String,
            acceptButtonMessage: String,
            rejectButtonMessage: String = "",
            acceptAction: () -> Unit = {},
            rejectAction: () -> Unit = {},
            cancelable: Boolean,
        )
        fun putDocumentToRemote(params: PutDocumentsRequestEntity)
    }

    private var officesInfo: OfficesResponseDto? = null
    private var citiesNamesList = mutableListOf<String>()
    private var userEmail: String = ""
    private var encodedImage: String? = null
    private lateinit var camUri: Uri
    private var _binding: FragmentSendDocumentsBinding? = null
    private val binding get() = _binding!!
    private val documentsTypeList = listOf(CC, CE, PA, TI)
    private val fileStorageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            with(result) {
                if (data != null && data?.data != null) {
                    val photoUri = result.data?.data
                    encodedImage = photoUri?.let { DocumentConverterHelper.encodeUriToBase64(requireContext(), it) }
                }
            }
        }
    }
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK && ::camUri.isInitialized) {
            encodedImage = DocumentConverterHelper.encodeUriToBase64(requireContext(), camUri)
        }
    }

    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        camUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, camUri)
        cameraLauncher.launch(cameraIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSendDocumentsBinding.inflate(inflater, container, false)
        initialize()
        arguments?.getParcelable<OfficesResponseDto>(OFFICES_INFO)?.let {
            officesInfo = it
        }
        arguments?.getString(USER_EMAIL)?.let {
            userEmail = it
        }
        return binding.root
    }

    private fun initialize() {
        binding.attachDocumentButton.setOnClickListener {
            this.hideKeyboard()
            removeFocus()
            val attachmentMenu = PopupMenu(requireActivity(), it)
            attachmentMenu.menuInflater.inflate(R.menu.attachment_menu, attachmentMenu.menu)
            attachmentMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_take_photo -> { verifyCameraPermission() }
                    R.id.menu_attach_photo -> { attachImageFromStorage() }
                }
                true
            }
            attachmentMenu.show()
        }

        binding.sendFormButton.setOnClickListener {
            this.hideKeyboard()
            removeFocus()
            sendForm()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        officesInfo?.let { offices ->
            offices.items.forEach { items ->
                citiesNamesList.add(items.city)
            }
        }
        citiesNamesList = citiesNamesList.distinct().toMutableList().apply {
            sort()
        }
        binding.documentTypeTextView.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, documentsTypeList))
        binding.cityTextView.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, citiesNamesList))
        binding.emailEditText.setText(userEmail)
    }

    private fun verifyCameraPermission() {
        if (isCameraPermissionGranted()) {
            pickCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            (activity as SendDocumentsFragmentListener).showMessageFromSendDocuments(
                getString(R.string.camera_title),
                getString(R.string.camera_subtitle),
                getString(R.string.accept_message),
                acceptAction = { showCameraPermissionDialog() },
                cancelable = false
            )
        } else {
            (activity as SendDocumentsFragmentListener).showMessageFromSendDocuments(
                getString(R.string.camera_title),
                getString(R.string.camera_subtitle_negative),
                getString(R.string.accept_message),
                cancelable = false
            )
        }
    }

    private fun showCameraPermissionDialog() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_CAMERA
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickCamera()
                } else {
                    (activity as SendDocumentsFragmentListener).showMessageFromSendDocuments(
                        getString(R.string.camera_title),
                        getString(R.string.camera_subtitle_negative),
                        getString(R.string.accept_message),
                        cancelable = false
                    )
                }
            }
            else -> { }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun sendForm() {
        if (validateForm() && encodedImage != null) {
            encodedImage?.let {
                val params = with(binding) {
                    PutDocumentsRequestEntity(
                        idType = documentTypeTextView.text.toString(),
                        identification = documentNumberEditText.text.toString(),
                        name = namesEditText.text.toString(),
                        lastName = lastnamesEditText.text.toString(),
                        city = cityTextView.text.toString(),
                        email = emailEditText.text.toString(),
                        attachmentType = attachmentTypeText.text.toString(),
                        attachment = it
                    )
                }
                (activity as SendDocumentsFragmentListener).putDocumentToRemote(params)
            }
        } else {
            (activity as SendDocumentsFragmentListener).showMessageFromSendDocuments(
                title = getString(R.string.send_docs_incomplete_info_title),
                message = getString(R.string.send_docs_incomplete_info_message),
                acceptButtonMessage = getString(R.string.accept_message),
                cancelable = false
            )
        }
    }

    private fun validateForm() = with(binding) {
        !documentTypeTextView.text.isNullOrBlank() && !documentNumberEditText.text.isNullOrBlank() &&
            !namesEditText.text.isNullOrBlank() && !lastnamesEditText.text.isNullOrBlank() &&
            !emailEditText.text.isNullOrBlank() && !cityTextView.text.isNullOrBlank() &&
            !attachmentTypeText.text.isNullOrBlank()
    }

    private fun attachImageFromStorage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        fileStorageLauncher.launch(intent)
    }

    fun resetForm() {
        binding.apply {
            documentTypeTextView.setText(getString(R.string.document_type_hint))
            documentTypeTextView.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, documentsTypeList))
            documentNumberEditText.text?.clear()
            namesEditText.text?.clear()
            lastnamesEditText.text?.clear()
            emailEditText.setText(userEmail)
            cityTextView.setText(getString(R.string.city_hint))
            cityTextView.setAdapter(ArrayAdapter(requireContext(), R.layout.list_item, citiesNamesList))
            attachmentTypeText.text?.clear()
        }
        encodedImage = null
    }

    private fun removeFocus() {
        val view: View? = activity?.currentFocus
        view?.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 103
    }
}
