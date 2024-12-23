package org.rotclub.area.composes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rotclub.area.R
import org.rotclub.area.lib.fontFamily
import org.rotclub.area.lib.apilink.AccAction
import org.rotclub.area.lib.apilink.AccReaction
import org.rotclub.area.lib.apilink.NodeType
import org.rotclub.area.ui.theme.FrispyTheme

@Composable
fun RadioButtonSelectAction(
    accAction: AccAction,
    service: String,
    selectedAction: AccAction?,
    onClick: (AccAction, String) -> Unit
) {
    val selected = selectedAction == accAction

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FrispyTheme.Surface700),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(accAction, service)
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = FrispyTheme.Primary500,
                unselectedColor = FrispyTheme.Surface400
            )
        )
        Text(
            text = accAction.displayName,
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun RadioButtonSelectReaction(
    accReaction: AccReaction,
    service: String,
    selectedReaction: AccReaction?,
    onClick: (AccReaction, String) -> Unit
) {
    val selected = selectedReaction == accReaction

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FrispyTheme.Surface700),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(accReaction, service)
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = FrispyTheme.Primary500,
                unselectedColor = FrispyTheme.Surface400
            )
        )
        Text(
            text = accReaction.displayName,
            color = Color.White,
            fontFamily = fontFamily,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun ListViewActions(
    action: NodeType,
    selectedAction: AccAction?,
    onClick: (AccAction, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(FrispyTheme.Surface700)
            .clip(RoundedCornerShape(8.dp))
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FrispyTheme.Surface700)
                .clip(RoundedCornerShape(8.dp))
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .weight(1f)
                    .fillMaxWidth()
                    .background(FrispyTheme.Surface700)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FrispyTheme.Surface700)
                ) {
                    IconButton(
                        onClick = { expanded = !expanded },
                    ) {
                        Icon(
                            painter = if (expanded) {
                                painterResource(id = R.drawable.chevron_down)
                            } else {
                                painterResource(id = R.drawable.chevron_up)
                            },
                            contentDescription = if (expanded) {
                                "Collapse"
                            } else {
                                "Expand"
                            },
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = action.service.toString(),
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                    )
                }
                if (expanded) {
                    for (accAction in action.actions) {
                        RadioButtonSelectAction(
                            accAction = accAction,
                            service = action.service.toString(),
                            selectedAction = selectedAction,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListViewReactions(
    action: NodeType,
    selectedReaction: AccReaction?,
    onClick: (AccReaction, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(FrispyTheme.Surface700)
            .clip(RoundedCornerShape(8.dp))
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FrispyTheme.Surface700)
                .clip(RoundedCornerShape(8.dp))
                .animateContentSize()
        ) {
            Column(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .weight(1f)
                    .fillMaxWidth()
                    .background(FrispyTheme.Surface700)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FrispyTheme.Surface700)
                ) {
                    IconButton(
                        onClick = { expanded = !expanded },
                    ) {
                        Icon(
                            painter = if (expanded) {
                                painterResource(id = R.drawable.chevron_down)
                            } else {
                                painterResource(id = R.drawable.chevron_up)
                            },
                            contentDescription = if (expanded) {
                                "Collapse"
                            } else {
                                "Expand"
                            },
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = action.service.toString(),
                        color = Color.White,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                    )
                }
                if (expanded) {
                    for (accReaction in action.reactions) {
                        RadioButtonSelectReaction(
                            accReaction = accReaction,
                            service = action.service.toString(),
                            selectedReaction = selectedReaction,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}