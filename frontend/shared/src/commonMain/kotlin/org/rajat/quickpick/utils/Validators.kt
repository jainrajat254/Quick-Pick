package org.rajat.quickpick.utils

object Validators {

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private val phoneRegex = Regex("^\\d{10}$")

    fun isVendorFormValid(
        vendorName: String,
        storeName: String,
        email: String,
        phone: String,
        password: String,
        address: String,
        gstNumber: String,
        licenseNumber: String,
        selectedCollege: String
    ): Boolean {
        return vendorName.isNotBlank() &&
                storeName.isNotBlank() &&
                email.isNotBlank() && email.matches(emailRegex) &&
                phone.isNotBlank() && phone.matches(phoneRegex) &&
                password.isNotBlank() &&
                address.isNotBlank() &&
                gstNumber.isNotBlank() &&
                licenseNumber.isNotBlank() &&
                selectedCollege.isNotBlank()
    }

    fun isUserFormValid(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        collegeName: String
    ): Boolean {
        return fullName.isNotBlank() &&
                email.isNotBlank() && email.matches(emailRegex) &&
                phone.isNotBlank() && phone.matches(phoneRegex) &&
                password.isNotBlank() &&
                collegeName.isNotBlank()
    }

    fun isLoginFormValid(
        email: String,
        password: String
    ): Boolean {
        return email.isNotBlank() && email.matches(emailRegex) &&
                password.isNotBlank()
    }

}
