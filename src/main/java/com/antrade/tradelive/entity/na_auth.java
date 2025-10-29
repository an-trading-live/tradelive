package com.antrade.tradelive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity  // Maps to a table
@Table(name = "na_auth")  // Optional: specify table name
public class na_auth {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment ID
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255)  // For hashed passwords (e.g., bcrypt)
    @Column(nullable = false, length = 255)
    private String password;

    @NotNull(message = "OTP is required")
    @Column(nullable = false, length = 10)  // Assuming 6-10 digit OTP as string
    private String otp;

    @NotBlank(message = "Unique ID is required")
    @Column(unique = true, nullable = false, length = 255)  // UUID or custom unique string
    private String uniqueId;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 15)  // e.g., +1-123-456-7890 or 1234567890
    @Column(unique = true, nullable = false, length = 20)
    private String mobileNumber;

    // Constructors
    public na_auth() {}

    public na_auth(String name, String email, String password, String otp, String uniqueId, String mobileNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.uniqueId = uniqueId;
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uniqueId) { this.uniqueId = uniqueId; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}