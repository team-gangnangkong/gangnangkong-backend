package com.example.sorimap.profile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameUpdateRequest {

    @NotBlank(message = "닉네임은 2-10자로 입력해 주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2-10자로 입력해 주세요.")
    private String nickname;
}
