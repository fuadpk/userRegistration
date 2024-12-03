package com.mykare.task.dto;

import java.util.UUID;

public class IdentifiableDTO {
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
