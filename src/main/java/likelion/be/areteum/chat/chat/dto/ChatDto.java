package likelion.be.areteum.chat.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {
    private Long id;
    private String content;
    private String createdAt;
}
