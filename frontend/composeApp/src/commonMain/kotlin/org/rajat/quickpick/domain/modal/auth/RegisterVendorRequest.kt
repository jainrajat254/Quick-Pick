package org.rajat.quickpick.domain.modal.auth
data class RegisterVendorRequest(
    val address: String ?,
    val collegeName: String ?,
    val email: String ?,
    val foodCategories: List<String> ?,
    val foodLicenseNumber: String ?,
    val gstNumber: String ?,
    val licenseNumber: String ?,
    val password: String ?,
    val phone: String ?,
    val storeName: String ?,
    val vendorDescription: String ?,
    val vendorName: String ?
)