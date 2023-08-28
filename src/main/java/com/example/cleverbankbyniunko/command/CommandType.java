package com.example.cleverbankbyniunko.command;

import com.example.cleverbankbyniunko.command.impl.DefaultCommand;
import com.example.cleverbankbyniunko.command.impl.LoginCommand;
import com.example.cleverbankbyniunko.command.impl.LogoutCommand;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {

    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
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
