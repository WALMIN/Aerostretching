package se.aerostretching.booking

data class TrainingItem(
    val id: String,
    val date: String,
    val time: String,
    val length: String,
    val title: String,
    val place: String,
    val trainer: String,
    val spots: String,
    val participants: List<String>,
    val booked: Boolean
)