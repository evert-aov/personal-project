package com.uagrm.schedule_assignment.finance.mapper;

import com.uagrm.schedule_assignment.finance.dto.TransactionRequestDto;
import com.uagrm.schedule_assignment.finance.dto.TransactionResponseDto;
import com.uagrm.schedule_assignment.finance.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDto requestDto) {
        if (requestDto == null)
            return null;

        return Transaction.builder()
                .description(requestDto.description())
                .date(requestDto.date())
                .amount(requestDto.amount())
                .type(requestDto.type())
                .build();
    }

    public TransactionResponseDto toDto(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getUser().getId(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.getPaidAmount()
        );
    }
}
