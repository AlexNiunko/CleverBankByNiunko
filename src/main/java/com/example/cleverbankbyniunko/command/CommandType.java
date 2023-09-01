package com.example.cleverbankbyniunko.command;

import com.example.cleverbankbyniunko.command.impl.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {

    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    SHOW_ACCOUNTS(new ShowAccountsCommand()),
    CHOSE_ACCOUNT(new ChoseAccountCommand()),
    REFILL_ACCOUNT(new RefillAccountCommand()),
    WITHDRAWALS_ACCOUNT(new WithdrawalsAccountCommand()),
    TRANSFER_ACCOUNT(new TransferAccountCommand()),
    GO_TO_REFILL_ACCOUNT(new GoToRefillAccountPageCommand()),
    GO_TO_WITHDRAWALS_ACCOUNT(new GoToWithdrawalsAccountPageCommand()),
    GO_TO_TRANSFER_ACCOUNT(new GoToTransferAccountPageCommand()),
    BACK_TO_ACCOUNT_PAGE(new BackToAccountPage()),
    BACK_TO_USERS_ACCOUNTS_PAGE(new BackToPageUserAccountsCommand()),
    BACK_TO_USER_PAGE(new BackToUserPageCommand()),
    DEFAULT(new DefaultCommand());
    private static final Logger logger = LogManager.getLogger();

    @Getter
    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command defineCommand(String commandStr) {
        logger.warn("In command define");
        CommandType currentCommand;
        Optional<CommandType> ifExist = Arrays.stream(CommandType.values())
                .filter(s -> s == CommandType.valueOf(commandStr))
                .findAny();
        currentCommand = (ifExist.orElse(CommandType.DEFAULT));
        return currentCommand.command;
    }

}
