package fr.soat.banking.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static fr.soat.banking.domain.AccountStatus.OPEN;

@Service
@Slf4j
public class TransferProcessManager  {

    private final AccountRepository accountRepository;

    @Autowired
    public TransferProcessManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @EventListener
    public void on(TransferRequested transferRequested) {
        log.info("consuming {}", transferRequested.getClass().getSimpleName());
        Account senderAccount = accountRepository.load(transferRequested.getAccountId());
        AccountId senderAccountId = senderAccount.getId();
        AccountId receiverAccountId = transferRequested.getReceiverAccountId();
        Account receiverAccount = accountRepository.load(receiverAccountId);
        int amount = transferRequested.getAmount();

        if (amount <= senderAccount.getBalance() &&
                receiverAccount.getStatus() == OPEN) {
            receiverAccount.receiveTransfer(senderAccountId, amount);
            accountRepository.save(receiverAccount);
        } else {
            senderAccount.refuseTransfer(receiverAccountId, amount);
            accountRepository.save(senderAccount);
        }
    }

    @EventListener
    public void on(TransferReceived transferReceived) {
        log.info("consuming {}", transferReceived.getClass().getSimpleName());
        Account senderAccount = accountRepository.load(transferReceived.getSenderAccountId());
        AccountId receiverAccountId = transferReceived.getAccountId();
        int amount = transferReceived.getAmount();
        senderAccount.sendTransfer(receiverAccountId, amount);
        accountRepository.save(senderAccount);
    }

    @EventListener
    public void on(TransferRefused transferRefused) {
        log.info("consuming {}", transferRefused.getClass().getSimpleName());
        // nothing to do (final step)
    }

    @EventListener
    public void on(TransferSent transferSent) {
        log.info("consuming {}", transferSent.getClass().getSimpleName());
        // nothing to do (final step)
    }
}
