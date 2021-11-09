package com.caner.noteplanner.view.notes

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.caner.noteplanner.R
import com.caner.noteplanner.presentation.viewmodel.MainViewModel
import com.caner.noteplanner.domain.utils.OrderType
import com.caner.noteplanner.domain.utils.NoteOrder
import com.caner.noteplanner.ui.components.DefaultRadioButton

@Composable
fun SortSection(
    viewModel: MainViewModel = hiltViewModel(),
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    orderSelected: (NoteOrder) -> (Unit)
) {
    AnimatedVisibility(
        visible = viewModel.noteState.value.isOrderSectionVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultRadioButton(
                    text = stringResource(id = R.string.title),
                    selected = noteOrder is NoteOrder.Title,
                    onSelect = {
                        orderSelected(NoteOrder.Title(noteOrder.orderType))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = stringResource(id = R.string.date),
                    selected = noteOrder is NoteOrder.Date,
                    onSelect = {
                        orderSelected(NoteOrder.Date(noteOrder.orderType))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                DefaultRadioButton(
                    text = stringResource(id = R.string.ascending),
                    selected = noteOrder.orderType is OrderType.Ascending,
                    onSelect = {
                        orderSelected(noteOrder.copy(OrderType.Ascending))
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                DefaultRadioButton(
                    text = stringResource(id = R.string.descending),
                    selected = noteOrder.orderType is OrderType.Descending,
                    onSelect = {
                        orderSelected(noteOrder.copy(OrderType.Descending))
                    }
                )
            }
        }
    }
}