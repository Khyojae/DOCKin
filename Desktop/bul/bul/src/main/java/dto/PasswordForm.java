package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordForm {
    @NotBlank(message="기존 패스워드를 입력해 주세요")
    private String old;
    @Size(min=8,message="8글자 이상 입력해 주세요")
    @NotBlank(message="새로운 패스워드를 입력해 주세요")
    private String password;
    @Size(min=8,message="8글자 이상 입력해 주세요")
    @NotBlank(message="새로운 패스워드를 입력해 주세요")
    private String passwordConfirm;
}
