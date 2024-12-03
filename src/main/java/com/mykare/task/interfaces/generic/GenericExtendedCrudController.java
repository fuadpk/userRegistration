package com.mykare.task.interfaces.generic;

import java.util.List;

public interface GenericExtendedCrudController<T> {
    List<T> getAll(int page, int size);
}
