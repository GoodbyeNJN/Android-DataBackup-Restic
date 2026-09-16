package com.xayah.feature.main.settings.restic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.xayah.core.ui.util.LocalNavController
import com.xayah.feature.main.settings.R

/**
 * Restic 密码设置界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResticPasswordScreen() {
    val navController = LocalNavController.current!!
    val viewModel = hiltViewModel<ResticViewModel>()

    // 本地编辑状态
    var password by remember { mutableStateOf("") }
    var useNoPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // 初始化时加载已有密码
    LaunchedEffect(Unit) {
        password = viewModel.getPassword()
        useNoPassword = password.isEmpty()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.restic_password)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.savePassword(if (useNoPassword) "" else password)
                            navController.navigateUp()
                        }
                    ) {
                        Text(stringResource(id = R.string.save))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 密码输入框
            OutlinedTextField(
                value = if (useNoPassword) "" else password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.restic_password)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                enabled = !useNoPassword,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = useNoPassword,
                    onCheckedChange = { checked ->
                        useNoPassword = checked
                        if (checked) password = ""
                    }
                )
                Text(text = stringResource(id = R.string.restic_no_password_init))
            }

            // 描述文本
            Text(
                text = stringResource(id = R.string.restic_password_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (useNoPassword) {
                Text(
                    text = stringResource(id = R.string.restic_no_password_warning),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}