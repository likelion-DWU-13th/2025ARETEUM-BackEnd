package likelion.be.areteum.chat.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatDto {
    private Long id;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd, hh:mm a", locale = "en_US")
    private LocalDateTime createdAt;
}
