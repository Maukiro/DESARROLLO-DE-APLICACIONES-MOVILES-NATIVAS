package com.example.habitflow.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.habitflow.R

sealed class BottomNavItem(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
) {
    object Home : BottomNavItem("home", R.drawable.ic_home, R.string.home)
    object Add : BottomNavItem("add", R.drawable.ic_add, R.string.add)
    object Stats : BottomNavItem("stats", R.drawable.ic_stats, R.string.stats)
}