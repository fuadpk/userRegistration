package com.mykare.task.interfaces.generic;

import java.util.UUID;

public interface GenericCrudController<T> {
    T post(T dto);
    T get(UUID uuid);
    T update(UUID uuid, T dto);
    void delete(UUID uuid);
}
