package com.consolefire.demo.hazelcast.api;

import com.consolefire.demo.hazelcast.core.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataPostDto {

    private String groupName;
    private Data data;
    
}
