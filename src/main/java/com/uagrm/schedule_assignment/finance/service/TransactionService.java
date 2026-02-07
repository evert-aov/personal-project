package com.uagrm.schedule_assignment.finance.service;

import com.uagrm.schedule_assignment.finance.dto.TransactionRequestDto;
import com.uagrm.schedule_assignment.finance.dto.TransactionResponseDto;
import com.uagrm.schedule_assignment.finance.entity.Transaction;
import com.uagrm.schedule_assignment.finance.entity.WorkingDay;
import com.uagrm.schedule_assignment.finance.mapper.TransactionMapper;
import com.uagrm.schedule_assignment.finance.repository.TransactionRepository;
import com.uagrm.schedule_assignment.finance.repository.WorkingDayRepository;
import com.uagrm.schedule_assignment.security.entity.User;
import com.uagrm.schedule_assignment.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final WorkingDayRepository workingDayRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponseDto addMyTransaction(TransactionRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();

        Transaction transaction = transactionMapper.toEntity(requestDto);
        transaction.setUser(userCurrent);

        if (requestDto.workingDayId() != null) {
            WorkingDay workingDay = workingDayRepository.findById(requestDto.workingDayId())
                    .orElseThrow(() -> new RuntimeException("Working day not found"));

            if (!workingDay.getUser().getId().equals(userCurrent.getId()))
                throw new RuntimeException("You cannot assign a transaction to a working day that is not yours.");

            transaction.setWorkingDay(workingDay);
        } else
            transaction.setWorkingDay(null);

        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public Iterable<TransactionResponseDto> getAllMyTransactions() {
        User userCurrent = userService.getCurrentUser();
        return transactionRepository.findAllByUserId(userCurrent.getId())
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Transactional
    public TransactionResponseDto getMyTransactionById(Long id) {
        User userCurrent = userService.getCurrentUser();
        return transactionRepository.findById(id)
                .filter(transaction -> transaction.getUser().getId().equals(userCurrent.getId()))
                .map(transactionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        User userCurrent = userService.getCurrentUser();
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !transaction.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to update this record.");

        transaction.setAmount(requestDto.amount());
        transaction.setDescription(requestDto.description());
        transaction.setType(requestDto.type());
        transaction.setDate(requestDto.date());

        if (requestDto.workingDayId() != null) {
            WorkingDay workingDay = workingDayRepository.findById(requestDto.workingDayId())
                    .orElseThrow(() -> new RuntimeException("Working day not found"));

            if (!isAdmin && !workingDay.getUser().getId().equals(userCurrent.getId()))
                throw new RuntimeException("You do not have permission to update this record.");
            transaction.setWorkingDay(workingDay);
        } else
            transaction.setWorkingDay(null);

        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Transactional
    public void deleteTransaction(Long id) {
        User userCurrent = userService.getCurrentUser();
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !transaction.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to delete this record.");

        transactionRepository.delete(transaction);
    }
}
