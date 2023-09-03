package com.example.cleverbankbyniunko.service.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class YamlReaderImpl {
    private static final Logger logger = LogManager.getLogger();
    public static final String PATH="info.yml";
    public Map<String,Integer> read(){
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PATH);
            Yaml yaml=new Yaml();
            Map<String,Integer>data=yaml.load(inputStream);
            return data;
    }
}
