package configgen.schema;

import java.util.Objects;

public class ForeignKeySchema {
    private final String name;
    private final KeySchema key;
    private final String refTable;
    private final RefKey refKey;
    private final Metadata meta;

    private TableSchema refTableSchema;

    public ForeignKeySchema(String name, KeySchema key, String refTable, RefKey refKey, Metadata meta) {
        this.name = name;
        this.key = key;
        this.refTable = refTable;
        this.refKey = refKey;
        this.meta = meta;
        Objects.requireNonNull(name);
        Objects.requireNonNull(key);
        Objects.requireNonNull(refTable);
        Objects.requireNonNull(refKey);
        Objects.requireNonNull( meta);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("struct name empty");
        }
    }

    public String name() {
        return name;
    }

    public KeySchema key() {
        return key;
    }

    public String refTable() {
        return refTable;
    }

    public RefKey refKey() {
        return refKey;
    }

    public TableSchema refTableSchema() {
        return refTableSchema;
    }

    void setRefTableSchema(TableSchema refTableSchema) {
        this.refTableSchema = refTableSchema;
    }

    public Metadata meta() {
        return meta;
    }

    @Override
    public String toString() {
        return "ForeignKeySchema{" +
                "name='" + name + '\'' +
                ", key=" + key +
                ", refTable='" + refTable + '\'' +
                ", refKey=" + refKey +
                '}';
    }


}