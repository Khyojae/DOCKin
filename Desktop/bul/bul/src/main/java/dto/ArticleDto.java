package dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class ArticleDto {
    private Long id;
    private Long memberId;
    private String name;
    private String email;
    private String title;
    private String description;
    private Date Created;
    private Date updated;
    private Date created;
}
