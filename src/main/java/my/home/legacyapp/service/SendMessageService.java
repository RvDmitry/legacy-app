package my.home.legacyapp.service;

import my.home.legacyapp.dto.MessageDto;

public interface SendMessageService {

    void sendMessage(MessageDto message);
}
