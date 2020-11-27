package se.aerostretching.booking

data class MessageItem(
    val id: String,
    val user: String,
    val date: String,
    val name: String,
    val text: String,
    val email: String,
    val read: Boolean
)