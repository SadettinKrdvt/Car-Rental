package com.example.carrental.dto;

import com.example.carrental.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Ad alanı boş bırakılamaz.")
    private String firstName;

    @NotBlank(message = "Soyad alanı boş bırakılamaz.")
    private String lastName;

    @NotBlank(message = "E-posta alanı boş bırakılamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    private String email;

    @NotBlank(message = "Şifre alanı boş bırakılamaz.")
    private String password;

    @NotNull(message = "Rol alanı boş bırakılamaz.")
    private Role role;
}
