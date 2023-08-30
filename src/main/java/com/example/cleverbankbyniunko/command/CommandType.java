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
    DEFAULT(new DefaultCommand()),
    CHOSE_ACCOUNT(new ChoseAccountCommand());
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
