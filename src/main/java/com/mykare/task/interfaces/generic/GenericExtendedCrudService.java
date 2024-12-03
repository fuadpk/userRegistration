package com.mykare.task.interfaces.generic;

import java.util.List;

public interface GenericExtendedCrudService<T> {
    List<T> getAll(int page, int size);
}
