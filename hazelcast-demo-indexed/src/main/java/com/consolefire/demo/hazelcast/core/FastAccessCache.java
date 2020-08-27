package com.consolefire.demo.hazelcast.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hazelcast.map.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.PredicateBuilder.EntryObject;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.impl.PredicateBuilderImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FastAccessCache {

    public static final Comparator<Map.Entry<Long, Double>> ENTRY_COMPARATOR = new SimpleKeyValueComparator();

    private final HazelcastObjectFactory hazelcastObjectFactory;

    public static String groupToMapName(@NonNull String groupName) {
        return "GROUPED_DATA_VALUE." + groupName.toUpperCase();
    }

    public Data add(@NonNull String groupName, @NonNull Data data) {
        final IMap<Long, Double> leadMap = hazelcastObjectFactory.getMap(groupToMapName(groupName));
        leadMap.put(data.getIdentifier(), data.getScore());
        return data;
    }

    public Set<Data> getTopDataByScoreWithPredicate(String groupName, int limit, Set<Long> excluding) {

        final IMap<Long, Double> leadMap = hazelcastObjectFactory.getMap(groupToMapName(groupName));

        Long[] excludeArray = new Long[] {};
        if (!CollectionUtils.isEmpty(excluding)) {
            excludeArray = excluding.toArray(new Long[0]);
        }

        EntryObject entryObject = new PredicateBuilderImpl().getEntryObject();
        PredicateBuilder builder = entryObject.key().in(excludeArray);

        PagingPredicate<Long, Double> predicate =
                Predicates.pagingPredicate(Predicates.not(builder), ENTRY_COMPARATOR, limit);

        Set<Data> picked = new HashSet<>();
        log.info("#### Picking started for group {} >@> {}", groupName, System.currentTimeMillis());
        Collection<Map.Entry<Long, Double>> selectedData = leadMap.entrySet(predicate);
        log.info("####  Picking completed for group {} << {}", groupName, System.currentTimeMillis());
        if (!CollectionUtils.isEmpty(selectedData)) {
            Set<Data> pickedAtPage = selectedData.stream().map(entry -> new Data(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
            picked.addAll(pickedAtPage);
        }

        return picked;
    }


    public Set<Data> getTopDataByScoreWithoutPredicate(String groupName, int limit, Set<Long> excluding) {
        final IMap<Long, Double> leadMap = hazelcastObjectFactory.getMap(groupToMapName(groupName));
        Set<Data> picked = new HashSet<>();
        log.info("#### Picking started for group {} >@> {}", groupName, System.currentTimeMillis());
        Collection<Data> selectedData = leadMap.entrySet().parallelStream().sorted(ENTRY_COMPARATOR)
                .filter(entry -> !excluding.contains(entry.getKey()))
                .limit(limit)
                .map(entry -> new Data(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
        log.info("####  Picking completed for group {} << {}", groupName, System.currentTimeMillis());
        if (!CollectionUtils.isEmpty(selectedData)) {
            picked.addAll(selectedData);
        }
        return picked;
    }
}
