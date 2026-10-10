package com.gdb.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A tiny stand-in for a real database: named tables holding rows (objects).
 * Swapping this for JDBC later would only affect DatabaseLogDestination.
 */
public class SimulatedDatabase {

    private final Map<String, List<Object>> tables = new HashMap<>();

    public void insert(String table, Object row) {
        tables.computeIfAbsent(table, t -> new ArrayList<>()).add(row);
    }

    public List<Object> selectAll(String table) {
        return new ArrayList<>(tables.getOrDefault(table, new ArrayList<>()));
    }

    public void deleteAll(String table) {
        tables.remove(table);
    }
}
