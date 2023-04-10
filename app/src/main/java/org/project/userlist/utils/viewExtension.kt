package org.project.userlist.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.makeToast(id: Int) {
    Toast.makeText(this, this.getString(id), Toast.LENGTH_LONG).show()
}
fun AppCompatActivity.makeToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}