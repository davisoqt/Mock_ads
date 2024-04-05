package com.example.quotesreels.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

class DialogManager {

    private var _dialogSate = DialogState()

    fun updateState(dialogState: DialogState) {
        _dialogSate = dialogState
    }

    @Composable
    fun showAlertDialog() {
        AlertDialog(
            icon = {
                Icon(painterResource(id = _dialogSate.icon), contentDescription = "Example Icon")
            },
            title = {
                Text(text = _dialogSate.title)
            },
            text = {
                Text(text = _dialogSate.text)
            },
            onDismissRequest = {

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        _dialogSate.onConfirmation()
                    }
                ) {
                    Text(_dialogSate.confirmationText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        _dialogSate.onCancel()
                    }
                ) {
                    Text(_dialogSate.cancelText)
                }
            }
        )
    }
}

data class DialogState(
    val icon: Int = 0,
    val text: String = "",
    val title: String = "",
    val confirmationText: String = "",
    val cancelText: String = "",
    val onConfirmation: () -> Unit = {},
    val onCancel: () -> Unit = {}
)