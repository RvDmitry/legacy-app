package my.home.legacyapp.dto;

import lombok.Data;

@Data
public class MessageDto {

    private long id;
    private Action action;
    private long eventTime;
}
