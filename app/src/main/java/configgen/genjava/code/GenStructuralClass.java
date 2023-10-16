package configgen.genjava.code;


import configgen.genjava.GenJavaUtil;
import configgen.schema.*;
import configgen.util.CachedIndentPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static configgen.gen.Generator.*;
import static configgen.schema.EntryType.*;
import static configgen.schema.FieldType.*;
import static configgen.value.CfgValue.*;

class GenStructuralClass {


    static void generate(Structural structural, VTable vtable,
                         NameableName name, CachedIndentPrinter ps, boolean isTableNeedBuilder) {
        boolean isTable = vtable != null;
        boolean isStruct = vtable == null;
        InterfaceSchema nullableInterface = structural instanceof StructSchema struct ? struct.nullableInterface() : null;
        boolean isImpl = nullableInterface != null;
        boolean isStructAndHasNoField = isStruct && structural.fields().isEmpty();

        ps.println(STR. "package \{ name.pkg };" );
        ps.println();
        if (isImpl) {
            ps.println(STR. "public class \{ name.className } implements \{ Name.fullName(nullableInterface) } {" );
            ps.println1("@Override");
            ps.println1(STR. "public \{ Name.refType(nullableInterface.enumRefTable()) } type() {" );
            ps.println2(STR. "return \{ Name.refType(nullableInterface.enumRefTable()) }.\{ structural.name().toUpperCase() };" );
            ps.println1("}");
            ps.println();
        } else {
            ps.println(STR. "public class \{ name.className } {" );
        }

        // field
        for (FieldSchema field : structural.fields()) {
            ps.println1(STR. "private \{ TypeStr.type(field.type()) } \{ lower1(field.name()) };" );
        }

        // fk
        for (ForeignKeySchema fk : structural.foreignKeys()) {
            ps.println1(STR. "private \{ Name.refType(fk) } \{ Name.refName(fk) };" );
        }
        ps.println();

        // constructor
        //noinspection StatementWithEmptyBody
        if (isStructAndHasNoField) {
            // 如果是没有field的struct
            // 后面会生成空参数的public构造函数
            // 这里忽略
        } else {
            ps.println1(STR. "private \{ name.className }() {" );
            ps.println1("}");
            ps.println();
        }

        if (isStruct) {
            // struct有public构造器
            ps.println1(STR. "public \{ name.className }(\{ MethodStr.formalParams(structural.fields()) }) {" );
            for (FieldSchema field : structural.fields()) {
                String ln = lower1(field.name());
                ps.println2(STR. "this.\{ ln } = \{ ln };" );
            }
            ps.println1("}");
            ps.println();
        } else if (isTableNeedBuilder) {
            GenStructuralClassTablePart.generateTableBuild(structural, name, ps);
        }

        // static create from ConfigInput
        ps.println1(STR. "public static \{ name.className } _create(configgen.genjava.ConfigInput input) {" );
        ps.println2(STR. "\{ name.className } self = new \{ name.className }();" );
        for (FieldSchema field : structural.fields()) {
            String ln = lower1(field.name());
            switch (field.type()) {
                case SimpleType simpleType -> {
                    ps.println2(STR. "self.\{ ln } = \{ TypeStr.readValue(simpleType) };" );
                }
                case FList fList -> {
                    ps.println2(STR. "self.\{ ln } = new java.util.ArrayList<>();" );
                    ps.println2("for (int c = input.readInt(); c > 0; c--) {");
                    ps.println3("self.%s.add(%s);", ln, TypeStr.readValue((fList.item())));
                    ps.println2("}");
                }
                case FMap fMap -> {
                    ps.println2(STR. "self.\{ ln } = new java.util.LinkedHashMap<>();" );
                    ps.println2("for (int c = input.readInt(); c > 0; c--) {");
                    ps.println3("self.%s.put(%s, %s);", ln, TypeStr.readValue(fMap.key()),
                            TypeStr.readValue((fMap.value())));
                    ps.println2("}");
                }
            }
        }
        ps.println2("return self;");
        ps.println1("}");
        ps.println();


        // getter
        for (FieldSchema field : structural.fields()) {
            String n = field.name();
            String comment = field.comment();
            if (!comment.isEmpty()) {
                ps.println1("/**");
                ps.println1(" * " + comment);
                ps.println1(" */");
            }

            ps.println1("public " + TypeStr.type(field.type()) + " get" + upper1(n) + "() {");
            ps.println2("return " + lower1(n) + ";");
            ps.println1("}");
            ps.println();
        }

        for (ForeignKeySchema fk : structural.foreignKeys()) {
            ps.println1("public %s %s() {", Name.refType(fk), lower1(Name.refName(fk)));
            ps.println2("return %s;", Name.refName(fk));
            ps.println1("}");
            ps.println();
        }

        if (isStructAndHasNoField) {
            ps.println1("@Override");
            ps.println1("public int hashCode() {");
            ps.println2("return " + name.className + ".class.hashCode();");
            ps.println1("}");
            ps.println();

            ps.println1("@Override");
            ps.println1("public boolean equals(Object other) {");
            ps.println2("return this == other || other instanceof " + name.className + ";");
            ps.println1("}");
            ps.println();


        } else if (isStruct) {
            ps.println1("@Override");
            ps.println1("public int hashCode() {");
            ps.println2("return " + MethodStr.hashCodes(structural.fields()) + ";");
            ps.println1("}");
            ps.println();

            ps.println1("@Override");
            ps.println1("public boolean equals(Object other) {");
            ps.println2("if (!(other instanceof " + name.className + "))");
            ps.println3("return false;");
            ps.println2(name.className + " o = (" + name.className + ") other;");
            ps.println2("return " + MethodStr.equals(structural.fields()) + ";");
            ps.println1("}");
            ps.println();
        }

        // toString
        String beanName = "";
        if (isImpl)
            beanName = name.className;
        ps.println1("@Override");
        ps.println1("public String toString() {");
        if (isStructAndHasNoField) {
            ps.println2("return \"%s\";", beanName);
        } else {
            String params = structural.fields().stream().map(f -> lower1(f.name())).collect(
                    Collectors.joining(" + \",\" + "));
            ps.println2("return \"%s(\" + %s + \")\";", beanName, params);
        }
        ps.println1("}");
        ps.println();


        // _resolve
        if (HasRef.hasRef(structural)) {
            generateResolve(structural, nullableInterface, ps);
        }

        if (isTable) {
            GenStructuralClassTablePart.generate(structural, vtable, name, ps);
        }
        ps.println("}");
    }


    private static void generateResolve(Structural structural, InterfaceSchema nullableInterface, CachedIndentPrinter ps) {
        boolean isImpl = nullableInterface != null;
        if (isImpl) {
            ps.println1("@Override");
        }
        ps.println1("public void _resolve(%s.ConfigMgr mgr) {", Name.codeTopPkg);


        // 1,先调用子_resolve
        for (FieldSchema field : structural.fields()) {
            FieldType type = field.type();
            if (!HasRef.hasRef(type)) {
                continue;
            }
            String ln = lower1(field.name());
            switch (type) {
                case StructRef _ -> {
                    ps.println2("%s._resolve(mgr);", ln);
                }
                case FList _ -> {
                    ps.println2("%s.forEach( e -> {", ln);
                    ps.println3("e._resolve(mgr);");
                    ps.println2("});");

                }
                case FMap _ -> {
                    ps.println2("%s.forEach( (k, v) -> {", ln);
                    ps.println3("v._resolve(mgr);");
                    ps.println2("});");
                }
                case Primitive _ -> {
                }
            }
        }

        // 2,处理本struct里的refSimple，
        for (ForeignKeySchema fk : structural.foreignKeys()) {
            if (!(fk.refKey() instanceof RefKey.RefSimple refSimple)) {
                continue;
            }
            FieldSchema firstField = fk.key().fieldSchemas().get(0);
            String refName = Name.refName(fk);
            TableSchema refTable = fk.refTableSchema();
            switch (firstField.type()) {
                case SimpleType _ -> {
                    ps.println2(refName + " = " + MethodStr.tableGet(refTable, refSimple,
                            MethodStr.actualParams(fk.key().fields())));
                    if (!refSimple.nullable())
                        ps.println2("java.util.Objects.requireNonNull(" + refName + ");");
                }
                case FList _ -> {
                    ps.println2("%s = new java.util.ArrayList<>();", refName);
                    ps.println2("%s.forEach( e -> {", lower1(firstField.name()));
                    ps.println3(Name.refType(refTable) + " r = " + MethodStr.tableGet(refTable, refSimple, "e"));
                    ps.println3("java.util.Objects.requireNonNull(r);");
                    ps.println3(refName + ".add(r);");
                    ps.println2("});");
                }
                case FMap _ -> {
                    ps.println2("%s = new java.util.LinkedHashMap<>();", refName);
                    ps.println2("%s.forEach( (k, v) -> {", lower1(firstField.name()));
                    ps.println3(Name.refType(refTable) + " rv = " + MethodStr.tableGet(refTable, refSimple, "v"));
                    ps.println3("java.util.Objects.requireNonNull(rv);");
                    ps.println3(refName + ".put(k, rv);");
                    ps.println2("});");
                }
            }
        }

        // 3,处理本struct里的refList
        for (ForeignKeySchema fk : structural.foreignKeys()) {
            if (!(fk.refKey() instanceof RefKey.RefList refList)) {
                continue;
            }
            String refName = Name.refName(fk);
            TableSchema refTable = fk.refTableSchema();

            ps.println2("%s = new java.util.ArrayList<>();", refName);

            NameableName refn = new NameableName(refTable);
            boolean isEnumAndNoDetail = GenJavaUtil.isEnumAndHasOnlyPrimaryKeyAndEnumStr(refTable);
            boolean isEnum = refTable.entry() instanceof EEnum && !isEnumAndNoDetail;
            if (isEnumAndNoDetail) {
                ps.println2("for (%s v : %s.values()) {", refn.fullName, refn.fullName);
            } else if (isEnum) {
                ps.println2("for (%s vv : %s.values()) {", refn.fullName, refn.fullName);
                String primK = refTable.primaryKey().fields().get(0);
                ps.println3("%s v = mgr.%sAll.get(vv.get%s());", refn.fullName + "_Detail", refn.containerPrefix,
                        upper1(primK));
            } else {
                ps.println2("mgr.%sAll.values().forEach( v -> {", refn.containerPrefix); // 为了跟之前兼容
            }

            List<String> eqs = new ArrayList<>();
            for (int i = 0; i < fk.key().fields().size(); i++) {
                FieldSchema k = fk.key().fieldSchemas().get(i);
                String rk = refList.keyNames().get(i); // refKey不可能是refTable的primary key，所以可以直接调用keyNames
                eqs.add(MethodStr.equal("v.get" + upper1(rk) + "()", lower1(k.name()), k.type()));
            }
            ps.println3("if (" + String.join(" && ", eqs) + ")");

            if (isEnumAndNoDetail) {
                ps.println4(refName + ".add(v);");
                ps.println2("}");
            } else if (isEnum) {
                ps.println4(refName + ".add(vv);");
                ps.println2("}");
            } else {
                ps.println4(refName + ".add(v);");
                ps.println2("});");
            }
        }

        ps.println1("}");
        ps.println();
    }

}
