package com.exam.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketInfoDTO {
    private static Integer idCounter = 0;
    private Integer id;
    private List<PurchaseDTO> articles;
    private Double total;

    public TicketInfoDTO() {
        this.id = idCounter;
        idCounter++;
    }
}
