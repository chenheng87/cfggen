package configgen.schema;

import java.util.*;

import static configgen.schema.EntryType.*;
import static configgen.schema.SchemaErrs.FilterRefIgnoredByRefKeyNotFound;
import static configgen.schema.SchemaErrs.FilterRefIgnoredByRefTableNotFound;

/**
 * tag： 只标注field的就行，不用标注foreign key，
 * foreign key是否提取，只由是否可行决定，能包含就包含。
 * <p>
 * 如果在structural上配置了tag，分3种情况
 * 1，所有field都没tag，-tag, 则包含所有field
 * 2，有部分field设了tag，则取这设置了tag的field
 * 3，没有设置tag的，但有部分设置了-tag，则提取没设-tag的field
 * <p>
 * 一般情况下，impl不需要设置tag，
 * 如果impl上设置tag，则则是为了能filter出空结构，相当于只用此impl类名字做标志，
 * 普通的struct不支持filter出空结构。
 */
public class CfgSchemaFilterByTag {
    private final CfgSchema cfg;
    private final String tag;
    private final String minusTag;
    private final SchemaErrs errs;

    public CfgSchemaFilterByTag(CfgSchema cfg, String tag, SchemaErrs errs) {
        cfg.requireResolved();
        Objects.requireNonNull(tag);
        if (tag.isEmpty()) {
            throw new IllegalArgumentException("filter tag empty");
        }
        Objects.requireNonNull(errs);
        this.cfg = cfg;
        this.tag = tag;
        minusTag = "-" + tag;
        this.errs = errs;
    }

    public CfgSchema filter() {
        Map<String, TableRule> tableMap = new HashMap<>();
        for (Nameable item : cfg.items()) {
            if (item instanceof TableSchema tableSchema && hasTag(tableSchema)) {
                TableRule tr = filterTablePhase1(tableSchema);
                tableMap.put(tr.table.name(), tr);
            }
        }

        CfgSchema filtered = CfgSchema.of();
        for (Nameable item : cfg.items()) {
            switch (item) {
                case InterfaceSchema interfaceSchema -> {
                    if (interfaceSchema.meta().hasTag(tag)) {
                        filtered.add(filterInterface(interfaceSchema, tableMap));
                    }
                }
                case StructSchema structSchema -> {
                    if (hasTag(structSchema)) {
                        filtered.add(filterStruct(structSchema, false, tableMap));
                    }
                }
                case TableSchema tableSchema -> {
                    TableRule tr = tableMap.get(tableSchema.name());
                    if (tr != null) {
                        TableSchema table = filterTablePhase2(tableSchema, tr, tableMap);
                        filtered.items().add(table);
                    }
                }
            }
        }
        return filtered;
    }


    private boolean hasTag(Structural struct) {
        if (struct.meta().hasTag(tag)) {
            return true;
        }
        for (FieldSchema field : struct.fields()) {
            if (field.meta().hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    private StructSchema filterStruct(StructSchema struct, boolean isImpl, Map<String, TableRule> tableMap) {
        FieldsRule ff = filterFields(struct, isImpl);
        List<ForeignKeySchema> fks = filterForeignKeys(struct, ff.rule, tableMap);
        return new StructSchema(struct.name(), struct.fmt(), struct.meta().copy(), ff.filteredFields, fks);
    }

    private InterfaceSchema filterInterface(InterfaceSchema sInterface, Map<String, TableRule> tableMap) {
        List<StructSchema> impls = new ArrayList<>(sInterface.impls().size());
        for (StructSchema impl : sInterface.impls()) {
            impls.add(filterStruct(impl, true, tableMap));
        }
        return new InterfaceSchema(sInterface.name(), sInterface.enumRef(), sInterface.defaultImpl(),
                sInterface.fmt(), sInterface.meta().copy(), impls);
    }

    private record TableRule(TableSchema table,
                             IncludeRule rule) {
    }

    private enum IncludeRule {
        ALL,
        WITH_TAG,
        WITH_NO_MINUS_TAG,
        IMPL_EMPTY //只有interface下的struct，可以为空
    }

    private record FieldsRule(List<FieldSchema> filteredFields,
                              IncludeRule rule) {

        boolean hasField(String name) {
            return filteredFields.stream().anyMatch(f -> f.name().equals(name));
        }

        boolean hasAllFields(List<String> names) {
            return names.stream().allMatch(this::hasField);
        }
    }

    private TableRule filterTablePhase1(TableSchema table) {
        FieldsRule ff = filterFields(table, false);
        EntryType entry = ENo.NO;
        switch (table.entry()) {
            case ENo.NO -> {
            }
            case EEntry ee -> {
                if (ff.hasField(ee.field)) {
                    entry = new EEntry(ee.field);
                }
            }
            case EEnum ee -> {
                if (ff.hasField(ee.field)) {
                    entry = new EEnum(ee.field);
                }
            }
        }
        List<KeySchema> uks = filterUniqKeys(table, ff);
        TableSchema t = new TableSchema(table.name(), table.primaryKey().copy(), entry, table.isColumnMode(),
                table.meta().copy(), ff.filteredFields(), List.of(), uks);
        return new TableRule(t, ff.rule);
    }

    private FieldsRule filterFields(Structural structural, boolean isImpl) {
        List<FieldSchema> withTagFields = new ArrayList<>();
        int withMinusTagFieldCount = 0;
        for (FieldSchema field : structural.fields()) {
            if (field.meta().hasTag(tag)) {
                withTagFields.add(field.copy());
            } else if (field.meta().hasTag(minusTag)) {
                withMinusTagFieldCount++;
            }
        }

        IncludeRule rule;
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<FieldSchema> filteredFields = withTagFields;
        if (withTagFields.isEmpty()) {
            if (isImpl && structural.meta().hasTag(tag)) {
                rule = IncludeRule.IMPL_EMPTY;
            } else if (withMinusTagFieldCount > 0) {
                for (FieldSchema field : structural.fields()) {
                    if (!field.meta().hasTag(minusTag)) {
                        filteredFields.add(field.copy());
                    }
                }
                rule = IncludeRule.WITH_NO_MINUS_TAG;
            } else {
                for (FieldSchema field : structural.fields()) {
                    filteredFields.add(field.copy());
                }
                rule = IncludeRule.ALL;

            }
        } else {
            rule = IncludeRule.WITH_TAG;
        }

        return new FieldsRule(filteredFields, rule);
    }

    private TableSchema filterTablePhase2(TableSchema originalTable, TableRule tr,
                                          Map<String, TableRule> phase1TableMap) {
        List<ForeignKeySchema> fks = filterForeignKeys(originalTable, tr.rule, phase1TableMap);
        TableSchema table = tr.table;

        return new TableSchema(table.name(), table.primaryKey(), table.entry(), table.isColumnMode(),
                table.meta(), table.fields(), fks, table.uniqueKeys());

    }


    private List<ForeignKeySchema> filterForeignKeys(Structural structural, IncludeRule rule,
                                                     Map<String, TableRule> phase1TableMap) {
        List<ForeignKeySchema> fks = new ArrayList<>();
        switch (rule) {
            case ALL -> {
                for (ForeignKeySchema fk : structural.foreignKeys()) {
                    recordForeignKeyIfOk(fks, fk, structural, phase1TableMap);
                }
            }
            case WITH_TAG -> {
                for (ForeignKeySchema fk : structural.foreignKeys()) {
                    if (fk.meta().hasTag(tag)) {
                        recordForeignKeyIfOk(fks, fk, structural, phase1TableMap);
                    }
                }
            }
            case WITH_NO_MINUS_TAG -> {
                for (ForeignKeySchema fk : structural.foreignKeys()) {
                    if (!fk.meta().hasTag(minusTag)) {
                        recordForeignKeyIfOk(fks, fk, structural, phase1TableMap);
                    }
                }
            }
            case IMPL_EMPTY -> {
            }
        }
        return fks;
    }

    private void recordForeignKeyIfOk(List<ForeignKeySchema> fks, ForeignKeySchema fk,
                                      Structural structural, Map<String, TableRule> phase1TableMap) {
        RefErr err = isForeignKeyIn(structural, fk, phase1TableMap);
        switch (err) {
            case OK -> fks.add(fk.copy());
            case TABLE_NOT_FOUND -> errs.addWarn(new FilterRefIgnoredByRefTableNotFound(
                    structural.name(), fk.name(), fk.refTable()));
            case KEY_NOT_FOUND -> errs.addWarn(new FilterRefIgnoredByRefKeyNotFound(
                    structural.name(), fk.name(), fk.refTable(), fk.refKey().keyNames()));
        }
    }

    private enum RefErr {
        OK,
        TABLE_NOT_FOUND,
        KEY_NOT_FOUND
    }

    private RefErr isForeignKeyIn(Structural structural, ForeignKeySchema fk, Map<String, TableRule> phase1TableMap) {
        TableRule refTable = null;
        // 本模块找
        String namespace = structural.namespace();
        if (!namespace.isEmpty()) {
            String fullName = Nameable.makeName(namespace, fk.refTable());
            refTable = phase1TableMap.get(fullName);
        }

        // 全局找
        if (refTable == null) {
            refTable = phase1TableMap.get(fk.refTable());
        }

        if (refTable == null) {
            return RefErr.TABLE_NOT_FOUND;
        }

        switch (fk.refKey()) {
            case RefKey.RefPrimary _ -> {
                return RefErr.OK;
            }
            case RefKey.RefUniq refUniq -> {
                KeySchema uk = refTable.table.findUniqueKey(refUniq.key());
                if (uk != null) {
                    return RefErr.OK;
                } else {
                    return RefErr.KEY_NOT_FOUND;
                }
            }
            case RefKey.RefList refList -> {
                Set<String> names = new HashSet<>();
                for (FieldSchema field : refTable.table.fields()) {
                    names.add(field.name());
                }
                if (names.containsAll(refList.key().fields())) {
                    return RefErr.OK;
                } else {
                    return RefErr.KEY_NOT_FOUND;
                }
            }
        }
    }

    private List<KeySchema> filterUniqKeys(TableSchema table, FieldsRule ff) {
        List<KeySchema> uks = new ArrayList<>();
        switch (ff.rule) {
            case ALL -> {
                for (KeySchema uk : table.uniqueKeys()) {
                    uks.add(uk.copy());
                }
            }
            case WITH_TAG, WITH_NO_MINUS_TAG -> {
                for (KeySchema uk : table.uniqueKeys()) {
                    if (ff.hasAllFields(uk.fields())) {
                        uks.add(uk.copy());
                    }
                }
            }
            case IMPL_EMPTY -> {
            }
        }
        return uks;
    }

}
