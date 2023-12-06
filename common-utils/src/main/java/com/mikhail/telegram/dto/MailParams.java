package com.mikhail.telegram.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailParams {

    private String id;
    private String recipientEmail;
}
