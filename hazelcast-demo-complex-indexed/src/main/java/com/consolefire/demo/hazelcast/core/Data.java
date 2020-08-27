package com.consolefire.demo.hazelcast.core;


import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Data implements IdentifiedDataSerializable, Serializable, Comparable<Data> {

    private static final long serialVersionUID = 9170460399133849831L;
    private Long identifier;
    private Double score;


    @JsonCreator
    public Data(@JsonProperty(value = "identifier", required = true) Long identifier,
            @JsonProperty(value = "score", required = true) Double score) {
        this.identifier = identifier;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Data)) {
            return false;
        }
        Data that = (Data) o;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    @JsonIgnore
    public int getFactoryId() {
        return 1001;
    }

    @Override
    @JsonIgnore
    public int getClassId() {
        return 1002;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(identifier);
        out.writeDouble(score);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        identifier = in.readLong();
        score = in.readDouble();
    }

    @Override
    public int compareTo(Data y) {
        if (y == null) {
            return 1;
        }
        if (this.equals(y)) {
            return 0;
        }
        if (score.equals(y.score)) {
            return 1;
        }
        return y.score.compareTo(this.score);
    }
}
