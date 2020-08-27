package com.consolefire.demo.hazelcast.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupLeadPickRequest implements Comparable<GroupLeadPickRequest> {

    @EqualsAndHashCode.Include
    private String groupName;
    private short quantity;
    private short priority;

    @Override
    public int compareTo(GroupLeadPickRequest o) {
        if (null == o) {
            return 1;
        }
        return new Short(priority).compareTo(o.priority);
    }


}
