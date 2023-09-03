package com.example.cleverbankbyniunko.service;

import java.io.IOException;

public interface BankCheck {
    boolean writeCheck(String appPath) throws IOException;
}
