package com.caner.noteplanner.view.notes.state

import com.caner.noteplanner.domain.utils.NoteOrder
import com.caner.noteplanner.domain.utils.OrderType

data class NoteState(
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
