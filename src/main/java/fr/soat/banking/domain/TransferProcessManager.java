package fr.soat.banking.domain;

import static fr.soat.banking.domain.AccountStatus.OPEN;

public class TransferProcessManager implements TransferEventListener {

    private final AccountRepository accountRepository;

    public TransferProcessManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void on(TransferEvent event) {
        event.applyOn(this);
    }

    public void on(TransferRequested transferRequested) {
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

    public void on(TransferReceived transferReceived) {
        Account senderAccount = accountRepository.load(transferReceived.getSenderAccountId());
        AccountId receiverAccountId = transferReceived.getAccountId();
        int amount = transferReceived.getAmount();
        senderAccount.sendTransfer(receiverAccountId, amount);
        accountRepository.save(senderAccount);
    }

    public void on(TransferRefused transferRefused) {
        // nothing to do (final step)
    }

    public void on(TransferSent transferSent) {
        // nothing to do (final step)
    }
}
