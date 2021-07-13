package pt.uninova.s4h.citizenhub.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import pt.uninova.s4h.citizenhub.persistence.AgentStateAnnotation.StateAnnotation;

@Entity(tableName = "device", primaryKeys = {"address"})
public class Device {
    //TODO state passar a type para guardar o tipo de agente
    private String name;
    @NonNull
    private String address;
    @ColumnInfo(name = "connection_kind")
    @TypeConverters(ConnectionKindTypeConverter.class)
    private int connectionKind;
    private StateAnnotation state; //desired state
    @ColumnInfo(name = "type")
    private String agentType;
    //agent_type -ID do agent

    @Ignore
    public Device() {
        address = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return address.equals(device.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public Device(String name, String address, int connectionKind, StateAnnotation state, String agentType) {
        this.name = name;
        this.address = address;
        this.connectionKind = connectionKind;
        this.state = state;
        this.agentType = agentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnectionKind() {
        return connectionKind;
    }

    public void setConnectionKind(int connectionKind) {
        this.connectionKind = connectionKind;
    }

    public StateAnnotation getState() {
        return state;
    }

    public void setState(StateAnnotation state) {
        this.state = state;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
}