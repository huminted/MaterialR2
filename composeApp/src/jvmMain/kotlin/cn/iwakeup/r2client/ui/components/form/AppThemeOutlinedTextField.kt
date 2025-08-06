package cn.iwakeup.r2client.ui.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.iwakeup.r2client.ui.theme.AppTheme


@Composable
fun AppThemeOutlinedTextField(modifier: Modifier, state: TextFieldState, label: String) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(0.6f),
        state = state,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = AppTheme.colors.primary,
        ),
        label = {

            Text(label, color = AppTheme.colors.primary)
        }
    )

}

