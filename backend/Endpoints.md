# API Endpoints

## Auth Endpoints

### Register User
- **POST /api/auth/register/user**
  - Fields: fullName, gender, phone, email, password, collegeName, department, studentId

### Register Vendor
- **POST /api/auth/register/vendor**
  - Fields: vendorName, storeName, email, phone, password, address, collegeName, vendorDescription, foodCategories, gstNumber, licenseNumber, foodLicenseNumber

### User Login
- **POST /api/auth/login/user**
  - Fields: email, password

### Vendor Login
- **POST /api/auth/login/vendor**
  - Fields: email, password

### Verify Email
- **GET /api/auth/verify-email**
  - Query Params: token, type

### Forgot Password
- **POST /api/auth/forgot-password**
  - Fields: email, userType
  
### Refresh Token
- **POST /api/auth/refresh-token**
  - Fields: refreshToken
  
### Reset Password
- **POST /api/auth/reset-password**
  - Fields: token, type, newPassword

### Change Password
- **POST /api/auth/change-password**
  - Header: Authorization: Bearer {jwt_token}
  - Fields: currentPassword, newPassword

### Resend Verification Email
- **POST /api/auth/resend-verification**
  - Query Params: email, userType

---

## Profile Endpoints

### Get User Profile
- **GET /api/profile/user**
  - Header: Authorization: Bearer {jwt_token}

### Get Vendor Profile
- **GET /api/profile/vendor**
  - Header: Authorization: Bearer {jwt_token}

### Update User Profile
- **PUT /api/profile/user**
  - Header: Authorization: Bearer {jwt_token}
  - Fields: fullName, phone, department, profileImageUrl

### Update Vendor Profile
- **PUT /api/profile/vendor**
  - Header: Authorization: Bearer {jwt_token}
  - Fields: vendorName, storeName, phone, address, vendorDescription, foodCategories, profileImageUrl





---