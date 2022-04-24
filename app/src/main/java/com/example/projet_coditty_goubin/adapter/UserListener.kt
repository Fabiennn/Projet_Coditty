package com.example.projet_coditty_goubin.adapter

import com.example.projet_coditty_goubin.model.User


class UserListener (val clickListener: (userId: Long) -> Unit) {
    fun onClick(user : User) = clickListener(user.id)
}