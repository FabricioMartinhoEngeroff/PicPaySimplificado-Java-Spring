package com.DvFabricio.PicPaySimplificado.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionaDTO (BigDecimal value, Long senderId, Long receiverId){
}
