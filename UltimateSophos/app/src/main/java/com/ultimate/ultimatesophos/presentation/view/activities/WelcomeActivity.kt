package com.ultimate.ultimatesophos.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.projects.sophosapp.R
import com.projects.sophosapp.databinding.ActivityWelcomeBinding
import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import com.ultimate.ultimatesophos.domain.definitions.Constants.DEFAULT_STRING
import com.ultimate.ultimatesophos.domain.definitions.Constants.DOCUMENTS_INFO
import com.ultimate.ultimatesophos.domain.definitions.Constants.DOCUMENT_DETAIL
import com.ultimate.ultimatesophos.domain.definitions.Constants.MENU_OPTIONS_FRAGMENT_TAG
import com.ultimate.ultimatesophos.domain.definitions.Constants.OFFICES_FRAGMENT_TAG
import com.ultimate.ultimatesophos.domain.definitions.Constants.OFFICES_INFO
import com.ultimate.ultimatesophos.domain.definitions.Constants.SEND_DOCUMENTS_FRAGMENT_TAG
import com.ultimate.ultimatesophos.domain.definitions.Constants.USER_EMAIL
import com.ultimate.ultimatesophos.domain.definitions.Constants.USER_INFO
import com.ultimate.ultimatesophos.domain.definitions.Constants.WATCH_DOCUMENTS_FRAGMENT_TAG
import com.ultimate.ultimatesophos.domain.models.DocumentsListDto
import com.ultimate.ultimatesophos.domain.models.OfficesResponseDto
import com.ultimate.ultimatesophos.domain.models.UserResponseDto
import com.ultimate.ultimatesophos.presentation.view.fragments.ImagePreviewFragment
import com.ultimate.ultimatesophos.presentation.view.fragments.SendDocumentsFragment
import com.ultimate.ultimatesophos.presentation.view.fragments.WatchDocumentsFragment
import com.ultimate.ultimatesophos.presentation.view.fragments.WelcomeFragment
import com.ultimate.ultimatesophos.presentation.viewmodel.DocumentsViewModel
import com.ultimate.ultimatesophos.presentation.viewmodel.OfficesViewModel
import com.projects.sophosapp.presentation.view.fragments.OfficesFragment

class WelcomeActivity :
    AppCompatActivity(),
    WelcomeFragment.WelcomeFragmentListener,
    SendDocumentsFragment.SendDocumentsFragmentListener,
    WatchDocumentsFragment.WatchDocumentsFragmentListener,
    OfficesFragment.OfficesFragmentListener {

    private lateinit var binding: ActivityWelcomeBinding
    private var officesInfo: OfficesResponseDto? = null
    private var documentList: DocumentsListDto? = null
    private var name: String = ""
    private var userInfo = UserResponseDto()
    private var tag: String = ""
    private val documentsViewModel: DocumentsViewModel by viewModels()
    private val officesViewModel: OfficesViewModel by viewModels()
    private val sharedPreferences: SharedPreferences by lazy {
        this.getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        subscribeToViewModel()
        intent.getParcelableExtra<UserResponseDto>(USER_INFO)?.let {
            userInfo = it
        }
        onBackPressedDispatcher.addCallback(this) {
            with(supportFragmentManager) {
                when (fragments.last()) {
                    is WelcomeFragment -> {
                        val drawer = binding.drawerLayout
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START)
                        }
                        goToLogin()
                    }
                    is ImagePreviewFragment -> {
                        backFromDocumentDetail()
                    }
                    else -> {
                        popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        tag = MENU_OPTIONS_FRAGMENT_TAG
                        navigateToFragment(WelcomeFragment())
                    }
                }
            }
        }
        initializeToolbar()
    }

    private fun handleOfficesObserver(result: OfficesResponseDto) {
        officesInfo = result
        val bundle = Bundle()
        val fragment = if (tag == OFFICES_FRAGMENT_TAG) {
            OfficesFragment()
        } else {
            SendDocumentsFragment()
        }
        if (tag == SEND_DOCUMENTS_FRAGMENT_TAG) {
            bundle.putString(USER_EMAIL, userInfo.email)
        }
        bundle.putParcelable(OFFICES_INFO, officesInfo)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content_menu, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun handleUploadDocumentsObserver(result: DocumentsUploadResponseModel) {
        if (result.uploaded) {
            showDialogMessage(
                title = getString(R.string.send_docs_success_title),
                message = getString(R.string.send_docs_success_message),
                acceptButtonMessage = getString(R.string.send_docs_success_accept_button),
                acceptAction = { resetDocumentsForm() },
                rejectButtonMessage = getString(R.string.return_title),
                rejectAction = {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    tag = MENU_OPTIONS_FRAGMENT_TAG
                    navigateToFragment(WelcomeFragment())
                },
                cancelable = false
            )
        } else {
            showDialogMessage(
                title = getString(R.string.send_docs_error_title),
                message = getString(R.string.send_docs_error_message),
                acceptButtonMessage = getString(R.string.accept_message),
                cancelable = false
            )
        }
    }

    private fun handleWatchDocumentsObserver(result: DocumentsListDto) {
        val bundle = Bundle()
        val fragment = WatchDocumentsFragment()
        bundle.putParcelable(DOCUMENTS_INFO, result)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content_menu, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun handleWatchDocumentsObserver(result: String) {
        val bundle = Bundle()
        val fragment = ImagePreviewFragment()
        bundle.putString(DOCUMENT_DETAIL, result)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content_menu, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun showLoader(visibility: Boolean) {
        binding.appBarMenu.contentMenu.loaderProgressBar.isVisible = visibility
    }

    private fun subscribeToViewModel() {
        officesViewModel.officesModel.observe(this) {
            handleOfficesObserver(it)
        }
        officesViewModel.isLoading.observe(this) {
            showLoader(it)
        }
        documentsViewModel.isLoading.observe(this) {
            showLoader(it)
        }
        documentsViewModel.putDocumentsModel.observe(this) {
            handleUploadDocumentsObserver(it)
        }
        documentsViewModel.getDocumentsListModel.observe(this) {
            documentList = it
            handleWatchDocumentsObserver(it)
        }
        documentsViewModel.getDocumentDetailModel.observe(this) {
            sharedPreferences.edit().putString(
                it.itemList.first().id,
                it.itemList.first().attachment
            ).apply()
            handleWatchDocumentsObserver(it.itemList.first().attachment)
        }
    }

    private fun initializeToolbar() {
        name = getFirstName()
        binding.appBarMenu.toolbar.setTitleTextColor(getColor(R.color.main_color))
        binding.appBarMenu.toolbar.textAlignment = TEXT_ALIGNMENT_TEXT_START
        setSupportActionBar(binding.appBarMenu.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = Html.fromHtml("<h3>$name</h3>", 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        setMenuItemsAlignment(menu)
        return true
    }

    private fun navigateToFragment(fragment: Fragment) {
        when (tag) {
            SEND_DOCUMENTS_FRAGMENT_TAG -> {
                officesInfo?.let {
                    handleOfficesObserver(it)
                } ?: officesViewModel.getOfficesList()
            }
            WATCH_DOCUMENTS_FRAGMENT_TAG -> userInfo.email?.let { userInfo ->
                if (supportFragmentManager.fragments.last() is ImagePreviewFragment) {
                    backFromDocumentDetail()
                } else {
                    documentsViewModel.getUploadedImagesList(userInfo)
                }
            }
            OFFICES_FRAGMENT_TAG -> {
                officesInfo?.let {
                    handleOfficesObserver(it)
                } ?: officesViewModel.getOfficesList()
            }
            else -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_content_menu, fragment, tag)
                    addToBackStack(tag)
                }.commit()
            }
        }
        updateVisuals(tag != MENU_OPTIONS_FRAGMENT_TAG)
    }

    private fun backFromDocumentDetail() {
        val bundle = Bundle()
        val fragment = WatchDocumentsFragment()
        bundle.putParcelable(DOCUMENTS_INFO, documentList)
        fragment.arguments = bundle
        tag = WATCH_DOCUMENTS_FRAGMENT_TAG
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_content_menu, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    private fun updateVisuals(isWelcomeFragment: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(isWelcomeFragment)
        binding.appBarMenu.toolbar.textAlignment = TEXT_ALIGNMENT_TEXT_START
        title = if (isWelcomeFragment) {
            Html.fromHtml("<h4>${getString(R.string.return_title)}</h4>", 0)
        } else {
            Html.fromHtml("<h3>$name</h3>", 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_send_documents -> {
                tag = SEND_DOCUMENTS_FRAGMENT_TAG
                navigateToFragment(SendDocumentsFragment())
                true
            }
            R.id.menu_watch_documents -> {
                tag = WATCH_DOCUMENTS_FRAGMENT_TAG
                navigateToFragment(WatchDocumentsFragment())
                true
            }
            R.id.menu_offices -> {
                tag = OFFICES_FRAGMENT_TAG
                navigateToFragment(OfficesFragment())
                true
            }
            R.id.menu_dark_mode -> {
                showToast(getString(R.string.menu_dark_mode))
                true
            }
            R.id.menu_english_language -> {
                showToast(getString(R.string.menu_change_language))
                true
            }
            R.id.menu_logout -> {
                goToLogin()
                true
            }
            android.R.id.home -> {
                if (supportFragmentManager.fragments.last() is ImagePreviewFragment) {
                    backFromDocumentDetail()
                } else {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    tag = MENU_OPTIONS_FRAGMENT_TAG
                    navigateToFragment(WelcomeFragment())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFirstName() = userInfo.name.let {
        val splitName = it?.split(" ")
        splitName?.get(0) ?: ""
    }

    private fun setMenuItemsAlignment(menu: Menu) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val s = SpannableString(item.title)
            s.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length, 0)
            item.title = s
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogMessage(
        title: String,
        message: String,
        acceptButtonMessage: String = "",
        rejectButtonMessage: String = "",
        acceptAction: () -> Unit = {},
        rejectAction: () -> Unit = {},
        cancelable: Boolean = true
    ) {
        AlertDialog
            .Builder(this, R.style.Theme_SophosApp_AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                acceptButtonMessage
            ) { dialog, _ ->
                dialog.dismiss()
                acceptAction()
            }
            .setNegativeButton(
                rejectButtonMessage
            ) { dialog, _ ->
                dialog.dismiss()
                rejectAction()
            }
            .setCancelable(cancelable)
            .create()
            .show()
    }

    private fun goToLogin() {
        showDialogMessage(
            title = getString(R.string.logout_title),
            message = getString(R.string.logout_message),
            acceptButtonMessage = getString(R.string.accept_message),
            acceptAction = {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            },
            rejectButtonMessage = getString(R.string.cancel_message)
        )
    }

    override fun goToSendDocuments() {
        tag = SEND_DOCUMENTS_FRAGMENT_TAG
        navigateToFragment(SendDocumentsFragment())
    }

    override fun goToWatchDocuments() {
        tag = WATCH_DOCUMENTS_FRAGMENT_TAG
        navigateToFragment(WatchDocumentsFragment())
    }

    override fun goToOffices() {
        tag = OFFICES_FRAGMENT_TAG
        navigateToFragment(OfficesFragment())
    }

    override fun showMessageFromOffices(
        title: String,
        message: String,
        acceptButtonMessage: String,
        rejectButtonMessage: String,
        acceptAction: () -> Unit,
        rejectAction: () -> Unit,
        cancelable: Boolean,
    ) {
        showDialogMessage(title, message, acceptButtonMessage, rejectButtonMessage, acceptAction, rejectAction, cancelable)
    }

    override fun showMessageFromSendDocuments(
        title: String,
        message: String,
        acceptButtonMessage: String,
        rejectButtonMessage: String,
        acceptAction: () -> Unit,
        rejectAction: () -> Unit,
        cancelable: Boolean,
    ) {
        showDialogMessage(title, message, acceptButtonMessage, rejectButtonMessage, acceptAction, rejectAction, cancelable)
    }

    override fun putDocumentToRemote(params: PutDocumentsRequestEntity) {
        documentsViewModel.putImageToRemote(params)
    }

    private fun resetDocumentsForm() {
        val fragmentSendDocuments =
            supportFragmentManager.findFragmentByTag(SEND_DOCUMENTS_FRAGMENT_TAG)
        if (fragmentSendDocuments is SendDocumentsFragment) {
            fragmentSendDocuments.resetForm()
        }
    }

    override fun retrieveDocumentInfo(id: String) {
        val attachment = sharedPreferences.getString(id, DEFAULT_STRING)
        if (attachment == DEFAULT_STRING) {
            documentsViewModel.getImageDetail(id)
        } else {
            handleWatchDocumentsObserver(attachment!!)
        }
    }
}
