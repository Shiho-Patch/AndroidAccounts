package com.rosan.accounts.ui.page.user_manager

import android.os.Process
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DeleteOutline
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rosan.accounts.R
import com.rosan.accounts.data.common.utils.help
import com.rosan.accounts.data.service.entity.UserEntity
import com.rosan.accounts.ui.page.main.MainScreen
import org.koin.androidx.compose.getViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalAnimationApi::class
)
@Composable
fun UserManagerPage(
    navController: NavController,
    viewModel: UserManagerViewModel = getViewModel()
) {
    SideEffect {
        viewModel.dispatch(UserManagerViewAction.Load)
    }

    // M3 Expressive: 可折叠的 LargeTopAppBar，滚动时标题会缩小
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.user_manager),
                        fontWeight = FontWeight.Medium
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { padding ->

        AnimatedVisibility(
            visible = viewModel.state.cause != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.TwoTone.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Text(
                        text = viewModel.state.cause
                            ?.help()
                            ?: viewModel.state.cause?.localizedMessage
                            ?: viewModel.state.cause?.toString()
                            ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = viewModel.state.cause == null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(viewModel.state.users, key = { it.id }) { user ->
                    var alpha by remember { mutableStateOf(0f) }
                    SideEffect { alpha = 1f }
                    UserItemCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .graphicsLayer {
                                this.alpha = animateFloatAsState(
                                    targetValue = alpha,
                                    animationSpec = spring(stiffness = 100f),
                                    label = "item_alpha"
                                ).value
                            },
                        user = user,
                        onOpenAccounts = {
                            navController.navigate(MainScreen.AccountManager.builder(user.id))
                        },
                        onRemove = {
                            viewModel.dispatch(UserManagerViewAction.Remove(user))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserItemCard(
    modifier: Modifier = Modifier,
    user: UserEntity,
    onOpenAccounts: () -> Unit,
    onRemove: () -> Unit
) {
    val canRemove = Process.myUserHandle().id != user.id
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // M3 Expressive: 圆形 primaryContainer 图标容器
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.TwoTone.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name ?: stringResource(R.string.user_name_default),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.user_id_format, user.id),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (canRemove) {
                    IconButton(onClick = { showConfirm = true }) {
                        Icon(
                            imageVector = Icons.TwoTone.DeleteOutline,
                            contentDescription = stringResource(R.string.remove),
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 状态 chip：表示用户类型
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = if (Process.myUserHandle().id == user.id)
                                stringResource(R.string.current_user_label)
                            else stringResource(R.string.other_user_label),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.TwoTone.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(SuggestionChipDefaults.IconSize)
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = Color.Transparent,
                        borderWidth = 0.dp
                    )
                )

                Spacer(Modifier.weight(1f))

                FilledTonalButton(
                    onClick = onOpenAccounts,
                    contentPadding = PaddingValues(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.account_manager),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    if (showConfirm) {
        RemoveUserDialog(
            user = user,
            onDismiss = { showConfirm = false },
            onConfirm = {
                onRemove()
                showConfirm = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RemoveUserDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.TwoTone.DeleteOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = stringResource(R.string.remove_user_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.delete_user_warning),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "${user.id} — ${user.name ?: stringResource(R.string.user_name_default)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete_user_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.delete_user_cancel))
            }
        },
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    )
}
