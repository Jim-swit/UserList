package org.project.userlist.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


fun AppCompatActivity.makeToast(id: Int) {
    Toast.makeText(this, this.getString(id), Toast.LENGTH_LONG).show()
}
fun AppCompatActivity.makeToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Fragment.makeToast(id: Int) {
    Toast.makeText(this.requireContext(), this.getString(id), Toast.LENGTH_LONG).show()
}

fun Fragment.makeToast(text: String) {
    Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG).show()
}