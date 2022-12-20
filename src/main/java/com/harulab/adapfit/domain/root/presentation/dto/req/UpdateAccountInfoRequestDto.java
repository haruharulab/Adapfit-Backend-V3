package com.harulab.adapfit.domain.root.presentation.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static com.harulab.adapfit.global.utils.ValidMessageConstants.*;

@Getter
@NoArgsConstructor
public class UpdateAccountInfoRequestDto {

    private String authId;
    private String email;

    @Size(message = NICKNAME_SIZE)
    private String nickname;
    private String phoneNumber;

    @Builder
    public UpdateAccountInfoRequestDto(String authId, String email, String nickname, String phoneNumber) {
        this.authId = authId;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public boolean authIdIsNull() {
        return authId == null;
    }

    public boolean emailIsNull() {
        return email == null;
    }

    public boolean nicknameIsNull() {
        return nickname == null;
    }

    public boolean phoneNumberIsNull() {
        return phoneNumber == null;
    }
}
