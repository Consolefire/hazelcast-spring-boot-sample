package com.consolefire.demo.hazelcast.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HazelcastObjectFactory {

    private final HazelcastInstance hazelcastInstance;

    private final Map<String, IMap<?, ?>> mapMap = new HashMap<>();

    public <K, V> IMap<K, V> getMap(String mapName) {
        if (mapMap.containsKey(mapName)) {
            return (IMap<K, V>) mapMap.get(mapName);
        }
        log.info("Getting Hazelcast Map with name: {}", mapName);
        IMap<K, V> createdMap = hazelcastInstance.getMap(mapName);
//        createdMap.addIndex(IndexType.SORTED, "this");
//        createdMap.addIndex(IndexType.HASH, "__key");
        mapMap.put(mapName, createdMap);
        return createdMap;
    }


}
