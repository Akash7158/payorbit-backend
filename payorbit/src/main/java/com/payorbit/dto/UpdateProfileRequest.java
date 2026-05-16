package com.payorbit.dto;

import lombok.Data;


@Data
public class UpdateProfileRequest {

    private String fullName;

    private String password;

    private String profilePhoto;
}