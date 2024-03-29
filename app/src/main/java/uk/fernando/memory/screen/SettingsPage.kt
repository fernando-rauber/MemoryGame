package uk.fernando.memory.screen

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.inject
import uk.fernando.memory.BuildConfig
import uk.fernando.memory.R
import uk.fernando.memory.activity.MainActivity
import uk.fernando.memory.component.NavigationBarTop
import uk.fernando.memory.datastore.PrefsStore
import uk.fernando.memory.viewmodel.SettingsViewModel
import uk.fernando.snackbar.CustomSnackBar
import uk.fernando.util.ext.clickableSingle

@Composable
fun SettingsPage(
    navController: NavController = NavController(LocalContext.current),
    viewModel: SettingsViewModel = getViewModel()
) {
    val context = LocalContext.current
    val prefs: PrefsStore by inject()
    val isSoundEnable = prefs.isSoundEnabled().collectAsState(initial = true)
    val isPremium = prefs.isPremium().collectAsState(initial = false)

    Box {
        Column(Modifier.fillMaxSize()) {

            NavigationBarTop(
                title = R.string.settings_title,
                onLeftIconClick = { navController.popBackStack() }
            )

            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                CustomSettingsResourcesCard(
                    modifier = Modifier.padding(top = 24.dp, bottom = 10.dp),
                    text = R.string.sound,
                    subText = R.string.sound_subtext,
                    isChecked = isSoundEnable.value,
                    onCheckedChange = viewModel::updateSound
                )

                CustomSettingsPremiumCard(
                    text = R.string.premium,
                    subText = R.string.premium_subtext,
                    isPremium = isPremium.value,
                    onClick = { viewModel.requestPayment(context as MainActivity) }
                )

                CustomSettingsResourcesCard(
                    modifier = Modifier.padding(vertical = 10.dp),
                    modifierRow = Modifier.clickableSingle { viewModel.restorePremium() },
                    text = R.string.restore_premium_action,
                    isChecked = false,
                    onCheckedChange = {},
                    showArrow = false
                )

                CustomSettingsResourcesCard(
                    modifierRow = Modifier
                        .clickableSingle {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://funmath-5c418.web.app/memory-privacy-policy.html"))
                            context.startActivity(browserIntent)
                        },
                    text = R.string.privacy_policy,
                    isChecked = false,
                    onCheckedChange = {},
                    showArrow = true
                )

                Spacer(Modifier.weight(0.9f))

                Text(
                    text = stringResource(id = R.string.version, BuildConfig.VERSION_NAME),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    textAlign = TextAlign.Center
                )

                if (BuildConfig.BUILD_TYPE == "debug") {
                    Text(
                        text = "Dev Build",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        SnackBarDisplay(viewModel)
    }
}


@Composable
private fun BoxScope.SnackBarDisplay(viewModel: SettingsViewModel) {
    val snackBar = viewModel.snackBar.collectAsState()
    CustomSnackBar(snackBarSealed = snackBar.value)
}

@Composable
private fun CustomSettingsResourcesCard(
    modifier: Modifier = Modifier,
    modifierRow: Modifier = Modifier,
    @StringRes text: Int,
    @StringRes subText: Int? = null,
    isChecked: Boolean,
    isPremium: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    showArrow: Boolean? = null
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(25),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifierRow.padding(16.dp)
        ) {

            Column(
                Modifier
                    .padding(end = 20.dp)
                    .weight(1f),
            ) {

                Row {

                    Text(
                        text = stringResource(id = text),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    if (isPremium)
                        Image(painter = painterResource(id = R.drawable.ic_crown), contentDescription = null)
                }

                subText?.let {
                    Text(
                        text = stringResource(id = subText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (showArrow == null)
                Switch(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = Color.White,
                    )
                )
            else if (showArrow)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

        }
    }
}


@Composable
private fun CustomSettingsPremiumCard(
    @StringRes text: Int,
    @StringRes subText: Int? = null,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(25),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickableSingle {
                    if (!isPremium)
                        onClick()
                }
                .padding(16.dp)
        ) {

            Column(
                Modifier
                    .padding(end = 20.dp)
                    .weight(1f),
            ) {

                Row {

                    Text(
                        text = stringResource(id = text),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_crown),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }

                subText?.let {
                    Text(
                        text = stringResource(id = subText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = if (isPremium) stringResource(id = R.string.purchased) else "£3.99",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
