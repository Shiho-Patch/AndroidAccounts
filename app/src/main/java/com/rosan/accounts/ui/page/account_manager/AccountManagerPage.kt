package com.rosan.accounts.ui.page.account_manager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ContentCopy
import androidx.compose.material.icons.twotone.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.rosan.accounts.R
import com.rosan.accounts.data.common.utils.copy
import com.rosan.accounts.data.common.utils.toast
import org.json.JSONArray
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun AccountManagerPage(
    userId: Int,
    navController: NavController,
    viewModel: AccountManagerViewModel = getViewModel {
        parametersOf(userId)
    }
) {
    SideEffect {
        viewModel.dispatch(AccountManagerViewAction.Load)
    }

    val context = LocalContext.current

    // M3 Expressive: 可折叠 LargeTopAppBar
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
                        text = stringResource(R.string.account_manager),
                        fontWeight = FontWeight.Medium
                    )
                },
                actions = {
                    IconButton(onClick = {
                        val array = JSONArray()
                        viewModel.state.authenticators.forEach {
                            array.put(it.auth.packageName)
                        }
                        context.copy(array.toString())
                        context.toast(R.string.copied_format_hail)
                    }) {
                        Icon(
                            imageVector = Icons.TwoTone.ContentCopy,
                            contentDescription = stringResource(R.string.copy_packages_cd),
                            modifier = Modifier.size(24.dp)
                        )
                    }
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
        val isEmpty = viewModel.state.authenticators.isEmpty()

        AnimatedVisibility(
            visible = isEmpty,
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    // M3 Expressive 空状态：大圆形容器 + 图标
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.TwoTone.SupervisorAccount,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.account_empty),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.account_empty_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isEmpty,
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
                items(viewModel.state.authenticators, key = { it.auth.type }) {
                    var alpha by remember { mutableStateOf(0f) }
                    SideEffect { alpha = 1f }
                    AuthenticatorItemCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                            .graphicsLayer {
                                this.alpha = animateFloatAsState(
                                    targetValue = alpha,
                                    animationSpec = spring(stiffness = 100f),
                                    label = "auth_alpha"
                                ).value
                            },
                        authenticator = it
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthenticatorItemCard(
    modifier: Modifier = Modifier,
    authenticator: AccountManagerViewState.Authenticator
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 应用图标：primaryContainer 圆形容器
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = rememberDrawablePainter(authenticator.auth.icon),
                        contentDescription = null
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = authenticator.auth.label,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = authenticator.auth.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = stringResource(
                                R.string.linked_accounts_count,
                                authenticator.accounts.size
                            ),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        borderColor = Color.Transparent,
                        borderWidth = 0.dp
                    )
                )
            }
        }
    }
}
