package com.consolefire.demo.hazelcast.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consolefire.demo.hazelcast.core.Data;
import com.consolefire.demo.hazelcast.core.FastAccessComplexCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/data")
public class DataController {


    private final FastAccessComplexCache fastAccessComplexCache;

    @PostMapping("/create")
    public Data create(@RequestBody DataPostDto dataPostDto) {
        return fastAccessComplexCache.add(dataPostDto.getGroupName(), dataPostDto.getData());
    }

    @PostMapping("/pick/predicate")
    public Collection<Data> pickWithPredicate(@RequestBody LeadPickRequest pickRequest) {
        List<GroupLeadPickRequest> groupLeadPickRequests =
                pickRequest.getGroupRequests().stream().sorted().collect(Collectors.toList());
        final Set<Data> pickList = new HashSet<>();
        final Set<Long> pickedCustomerList = new HashSet<>();
        groupLeadPickRequests.forEach(request -> {
            String groupName = request.getGroupName();
            Set<Data> pickListByGroup = fastAccessComplexCache.getTopDataByScoreWithPredicate(groupName, request.getQuantity(),
                    pickedCustomerList);
            if (null != pickListByGroup) {
                pickList.addAll(pickListByGroup);
            }
            pickedCustomerList.addAll(pickList.stream().map(Data::getIdentifier).collect(Collectors.toSet()));
        });
        return pickList;
    }

    @PostMapping("/pick/iterate")
    public Collection<Data> pickWithoutPredicate(@RequestBody LeadPickRequest pickRequest) {
        List<GroupLeadPickRequest> groupLeadPickRequests =
                pickRequest.getGroupRequests().stream().sorted().collect(Collectors.toList());
        final Set<Data> pickList = new HashSet<>();
        final Set<Long> pickedCustomerList = new HashSet<>();
        groupLeadPickRequests.forEach(request -> {
            String groupName = request.getGroupName();
            Set<Data> pickListByGroup = fastAccessComplexCache.getTopDataByScoreWithoutPredicate(groupName,
                    request.getQuantity(), pickedCustomerList);
            if (null != pickListByGroup) {
                pickList.addAll(pickListByGroup);
            }
            pickedCustomerList.addAll(pickList.stream().map(Data::getIdentifier).collect(Collectors.toSet()));
        });
        return pickList;
    }
}
