package com.example.DOCKin.mapper; // 패키지 경로 확인!
import org.apache.ibatis.annotations.Mapper;
import com.example.DOCKin.model.Account; // Account 클래스 임포트
import org.springframework.data.repository.query.Param;


@Mapper
public interface UserMapper {
    //사번을 통해 Account 객체를 찾아 반환
    Account findUser(Account account);
    Account findAuthority(Account account);
    String findAuthorityById(@Param("id") String id);

}
