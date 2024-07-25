package com.happy.friendogly.presentation.ui.group.add.model

class GroupCounter(val count: Int = MAX_COUNT) {
    companion object {
        const val MAX_COUNT = 5
        const val MIN_COUNT = 1
    }
}