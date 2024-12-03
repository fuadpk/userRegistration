package com.mykare.task.interfaces.generic;

import java.util.UUID;

public interface GenericCrudService<T> {
    T create(T dto);
    T read(UUID uuid);
    T update(UUID uuid, T dto);
    void delete(UUID uuid);
}
