package com.exam.spring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketResponseDTO extends ResponseDTO{
    private TicketInfoDTO ticket;
}
