package configgen.data;

import configgen.Logger;
import configgen.schema.*;
import configgen.schema.EntryType.EEntry;
import configgen.schema.EntryType.EEnum;
import configgen.schema.FieldType.Primitive;
import configgen.schema.cfg.Metas;

import java.util.*;

import static configgen.schema.EntryType.ENo.NO;
import static configgen.schema.FieldFormat.*;

public enum CfgSchemaAlignToDataHeader {
    INSTANCE;

    public CfgSchema align(CfgSchema cfg, Map<String, DataHeader> dataHeaders) {
        CfgSchema alignedCfg = CfgSchema.of();
        for (Nameable item : cfg.items()) {
            switch (item) {
                case Fieldable fieldable -> {
                    alignedCfg.add(fieldable.copy());
                }
                case TableSchema table -> {
                    DataHeader header = dataHeaders.remove(table.name());
                    if (header != null) {
                        TableSchema alignedTable = alignTable(table, header);
                        alignedCfg.add(alignedTable);
                    }
                }
            }
        }

        for (Map.Entry<String, DataHeader> e : dataHeaders.entrySet()) {
            String name = e.getKey();
            DataHeader header = e.getValue();

            TableSchema newTable = newTable(name, header);
            alignedCfg.add(newTable);
        }
        return alignedCfg;
    }

    private TableSchema newTable(String name, DataHeader header) {
        List<FieldSchema> fields = new ArrayList<>(header.fields().size());
        for (DataHeader.HeaderField hf : header.fields()) {
            Metadata meta = Metadata.of();
            if (!hf.comment().isEmpty()) {
                Metas.putComment(meta, hf.comment());
            }

            FieldSchema field = new FieldSchema(hf.name(), Primitive.STR, AutoOrPack.AUTO, meta);
            fields.add(field);
        }

        if (fields.isEmpty()) {
            Logger.log(STR. "\{ name } header empty, ignored!" );
            return null;
        }

        String first = fields.iterator().next().name();
        KeySchema primaryKey = new KeySchema(List.of(first));

        return new TableSchema(name, primaryKey, NO, false, Metadata.of(), fields, List.of(), List.of());
    }

    private TableSchema alignTable(TableSchema table, DataHeader header) {
        String name = table.name();
        Map<String, FieldSchema> fieldSchemas = alignFields(table, header);
        if (fieldSchemas.isEmpty()) {
            return null;
        }

        KeySchema primaryKey;
        if (!isKeyInSchemaList(table.primaryKey(), fieldSchemas)) {
            String first = fieldSchemas.keySet().iterator().next();
            primaryKey = new KeySchema(List.of(first));
        } else {
            primaryKey = table.primaryKey().copy();
        }

        EntryType entry = NO;
        switch (table.entry()) {
            case NO -> {
            }
            case EEntry eEntry -> {
                if (fieldSchemas.containsKey(eEntry.field())) {
                    entry = new EEntry(eEntry.field());
                }
            }
            case EEnum eEnum -> {
                if (fieldSchemas.containsKey(eEnum.field())) {
                    entry = new EEnum(eEnum.field());
                }
            }
        }

        boolean isColumnMode = table.isColumnMode();
        Metadata meta = table.meta().copy();
        List<FieldSchema> fields = new ArrayList<>(fieldSchemas.values());

        List<ForeignKeySchema> fks = new ArrayList<>();
        for (ForeignKeySchema fk : table.foreignKeys()) {
            if (isKeyInSchemaList(fk.key(), fieldSchemas)) {
                fks.add(fk);
            }
        }

        List<KeySchema> uks = new ArrayList<>();
        for (KeySchema uk : table.uniqueKeys()) {
            if (isKeyInSchemaList(uk, fieldSchemas)) {
                uks.add(uk);
            }
        }

        return new TableSchema(name, primaryKey, entry, isColumnMode, meta, fields, fks, uks);
    }

    private boolean isKeyInSchemaList(KeySchema key, Map<String, FieldSchema> fieldSchemas) {
        for (String k : key.name()) {
            if (!fieldSchemas.containsKey(k)) {
                return false;
            }
        }
        return true;
    }


    private Map<String, FieldSchema> alignFields(TableSchema table, DataHeader header) {
        Map<String, FieldSchema> curFields = new LinkedHashMap<>();
        for (FieldSchema field : table.fields()) {
            curFields.put(field.name(), field);
        }

        Map<String, FieldSchema> alignedFields = new LinkedHashMap<>();
        int size = header.fields().size();
        for (int idx = 0; idx < size; ) {
            DataHeader.HeaderField hf = header.fields().get(idx);
            String name = hf.name();
            String comment = hf.comment();

            FieldSchema newField;
            FieldSchema curField = curFields.remove(name);
            if (curField != null) {
                int span = Spans.span(curField);
                idx += span;
                Metadata meta = curField.meta().copy();
                if (!comment.isEmpty()) {
                    String old = Metas.putComment(meta, comment);
                    if (!old.equals(comment)) {
                        Logger.log(STR. "\{ table.name() }[\{ name }] set comment: \{ comment }" );
                    }
                } else {
                    String old = Metas.removeComment(meta);
                    if (!old.isEmpty()) {
                        Logger.log(STR. "\{ table.name() }[\{ name }] remove old comment: \{ old }" );
                    }
                }
                newField = new FieldSchema(name, curField.type().copy(), curField.fmt(), meta);

            } else {
                idx++;
                Metadata meta = Metadata.of();
                if (!comment.isEmpty()) {
                    Metas.putComment(meta, comment);
                }
                newField = new FieldSchema(name, Primitive.STR, AutoOrPack.AUTO, meta);
                Logger.log(STR. "\{ table.name() } new field: \{ name }" );
            }

            alignedFields.put(name, newField);
        }

        for (FieldSchema remove : curFields.values()) {
            Logger.log(STR. "\{ table.name() } delete field: \{ remove.name() }" );
        }
        return alignedFields;
    }

}