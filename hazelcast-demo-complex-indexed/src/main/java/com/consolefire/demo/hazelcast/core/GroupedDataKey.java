package com.consolefire.demo.hazelcast.core;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupedDataKey implements Serializable, IdentifiedDataSerializable {

    private String groupName;
    private Long identifier;

    @Override
    public int getFactoryId() {
        return 2001;
    }

    @Override
    public int getClassId() {
        return 2002;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(groupName);
        out.writeLong(identifier);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        groupName = in.readUTF();
        identifier = in.readLong();
    }
}
