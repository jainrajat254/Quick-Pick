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


## College Endpoints

### Public Endpoints

#### Get all colleges (public)
- **GET /api/colleges/public**
  - Returns: List of CollegeResponseDto (id, name, address, city, state)

#### Get paginated colleges (public)
- **GET /api/colleges/public/paginated**
  - Query Params: page (default 0), size (default 10)
  - Returns: Page of CollegeResponseDto

#### Get college by ID (public)
- **GET /api/colleges/public/{id}**
  - Path Param: id
  - Returns: CollegeResponseDto

#### Search colleges (public)
- **GET /api/colleges/public/search**
  - Query Param: query
  - Returns: List of CollegeResponseDto

#### Get all cities (public)
- **GET /api/colleges/public/cities**
  - Returns: List of city names

#### Get all states (public)
- **GET /api/colleges/public/states**
  - Returns: List of state names

#### Get colleges by city (public)
- **GET /api/colleges/public/city/{city}**
  - Path Param: city
  - Returns: List of CollegeResponseDto

#### Get colleges by state (public)
- **GET /api/colleges/public/state/{state}**
  - Path Param: state
  - Returns: List of CollegeResponseDto

#### Get total colleges count (public)
- **GET /api/colleges/public/count**
  - Returns: { "count": long }

---

### Admin Endpoints 

#### Create college
- **POST /api/colleges/create**
  - Header: Authorization: Bearer {jwt_token}
  - Body: CollegeCreateDto (name, address, city, state)
  - Returns: CollegeResponseDto

#### Update college
- **PUT /api/colleges/update/{id}**
  - Header: Authorization: Bearer {jwt_token}
  - Path Param: id
  - Body: CollegeCreateDto
  - Returns: CollegeResponseDto

#### Delete college
- **DELETE /api/colleges/delete/{id}**
  - Header: Authorization: Bearer {jwt_token}
  - Path Param: id
  - Returns: { "message": "College deleted successfully" }


---

## Admin Management Endpoints

Verify a vendor.
Get list of all users/vendors.
Get list of users/vendors by college name.
Suspend/unsuspend a user/vendor.

### Users

- **GET /api/admin-management/users**
  - Returns: Page<AdminUserDto>
  - Query Params: page, size

- **GET /api/admin-management/users/college/{collegeName}**
  - Returns: Page<AdminUserDto>
  - Path Param: collegeName
  - Query Params: page, size

- **POST /api/admin-management/users/{userId}/suspend**
  - Returns: AdminUserDto
  - Path Param: userId
  - Body: SuspensionDto { reason }

- **POST /api/admin-management/users/{userId}/unsuspend**
  - Returns: AdminUserDto
  - Path Param: userId

### Vendors
- **GET /api/admin-management/vendors**
  - Returns: Page<AdminVendorDto>
  - Query Params: page, size

- **GET /api/admin-management/vendors/pending** 
  - Returns: Page<AdminVendorDto>
  - Query Params: page, size

- **POST /api/admin-management/vendors/{vendorId}/verify** 
  - Returns: AdminVendorDto
  - Path Param: vendorId
  - Body: VerificationDto { notes }

- **POST /api/admin-management/vendors/{vendorId}/reject**
  - Returns: AdminVendorDto
  - Path Param: vendorId
  - Body: VerificationDto { rejectionReason }

- **POST /api/admin-management/vendors/{vendorId}/suspend** 
  - Returns: AdminVendorDto
  - Path Param: vendorId
  - Body: SuspensionDto { reason }

- **POST /api/admin-management/vendors/{vendorId}/unsuspend**
  - Returns: AdminVendorDto
  - Path Param: vendorId
