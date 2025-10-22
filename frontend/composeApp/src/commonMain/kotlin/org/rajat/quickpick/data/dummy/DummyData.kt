package org.rajat.quickpick.data.dummy

object DummyData {

    val colleges = listOf(
        College("1", "Indian Institute of Technology, Delhi"),
        College("2", "Delhi University"),
        College("3", "Jawaharlal Nehru University"),
        College("4", "Jamia Millia Islamia"),
        College("5", "Indian Institute of Technology, Mumbai"),
        College("6", "Indian Institute of Technology, Bangalore"),
        College("7", "BITS Pilani"),
        College("8", "Amity University")
    )

    val branches = listOf(
        Branch("1", "Computer Science"),
        Branch("2", "Information Technology"),
        Branch("3", "Electronics & Communication"),
        Branch("4", "Mechanical Engineering"),
        Branch("5", "Civil Engineering"),
        Branch("6", "Business Administration"),
        Branch("7", "Commerce"),
        Branch("8", "Arts & Humanities")
    )

    val genders = listOf("Male", "Female", "Prefer not to say")
}

data class College(
    val id: String,
    val name: String
)

data class Branch(
    val id: String,
    val name: String
)