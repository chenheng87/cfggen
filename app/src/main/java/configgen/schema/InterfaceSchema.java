package configgen.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static configgen.schema.FieldFormat.AutoOrPack.AUTO;
import static configgen.schema.FieldFormat.AutoOrPack.PACK;

public final class InterfaceSchema implements Fieldable, Nameable {
    private final String name;

    /**
     * 必须索引到一个枚举表中，枚举表中要包含等价于但更策划易读的impls信息，因为策划不会去看schema
     */
    private final String enumRef;
    /**
     * 可以为空，如果设置了，则有以下两个作用
     * <1> 允许格子为空，此时会选择这个struct
     * <2> 可以不加此类型名前缀
     */
    private final String defaultImpl;

    private final FieldFormat fmt;
    private final Metadata meta;

    /**
     * 所有的实现都放在这
     */
    private final List<StructSchema> impls;

    private TableSchema enumRefTable;
    private StructSchema defaultImplStruct;

    public InterfaceSchema(String name, String enumRef, String defaultImpl,
                           FieldFormat fmt, Metadata meta,
                           List<StructSchema> impls) {
        this.name = name;
        this.enumRef = enumRef;
        this.defaultImpl = defaultImpl;
        this.fmt = fmt;
        this.meta = meta;
        this.impls = impls;

        Objects.requireNonNull(name);
        Objects.requireNonNull(enumRef);
        Objects.requireNonNull(defaultImpl);
        Objects.requireNonNull(fmt);
        Objects.requireNonNull(impls);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("interface name empty");
        }
        if (enumRef.isEmpty()) {
            throw new IllegalArgumentException("interface enumRef empty");
        }
        if (fmt != AUTO && fmt != PACK) {
            throw new IllegalArgumentException("interface fmt must be auto/pack");
        }
    }

    @Override
    public InterfaceSchema copy() {
        List<StructSchema> implsCopy = new ArrayList<>(impls.size());
        for (StructSchema impl : impls) {
            implsCopy.add(impl.copy());
        }
        return new InterfaceSchema(name, enumRef, defaultImpl, fmt, meta.copy(), implsCopy);
    }

    public String name() {
        return name;
    }

    public FieldFormat fmt() {
        return fmt;
    }

    public String enumRef() {
        return enumRef;
    }

    public String defaultImpl() {
        return defaultImpl;
    }

    public Metadata meta() {
        return meta;
    }

    public List<StructSchema> impls() {
        return impls;
    }

    public StructSchema findImpl(String name) {
        for (StructSchema impl : impls) {
            if (impl.name().equals(name)) {
                return impl;
            }
        }
        return null;
    }

    public TableSchema enumRefTable() {
        return enumRefTable;
    }

    void setEnumRefTable(TableSchema enumRefTable) {
        this.enumRefTable = enumRefTable;
    }

    public StructSchema defaultImplStruct() {
        return defaultImplStruct;
    }

    void setDefaultImplStruct(StructSchema defaultImplStruct) {
        this.defaultImplStruct = defaultImplStruct;
    }

    @Override
    public String toString() {
        return "InterfaceSchema{" +
                "name='" + name + '\'' +
                ", enumRef='" + enumRef + '\'' +
                ", defaultImpl='" + defaultImpl + '\'' +
                ", fmt=" + fmt +
                ", meta=" + meta +
                ", impls=" + impls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceSchema that = (InterfaceSchema) o;
        return Objects.equals(name, that.name) && Objects.equals(enumRef, that.enumRef) && Objects.equals(defaultImpl, that.defaultImpl) && Objects.equals(fmt, that.fmt) && Objects.equals(meta, that.meta) && Objects.equals(impls, that.impls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, enumRef, defaultImpl, fmt, meta, impls);
    }


}
