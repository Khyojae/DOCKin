package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MemberForm {
    private Long id;
    @NotBlank(message="이름을 입력하세요")
    private String name;
    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 잘못 되었습니다")
    private String email;
    private String password;
    private String passwordConfirm;
}
