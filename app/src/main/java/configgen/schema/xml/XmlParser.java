package configgen.schema.xml;

import configgen.schema.*;
import configgen.schema.RefKey.RefList;
import configgen.schema.RefKey.RefPrimary;
import configgen.util.DomUtils;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static configgen.schema.FieldFormat.AutoOrPack.AUTO;
import static configgen.schema.FieldFormat.AutoOrPack.PACK;
import static configgen.schema.FieldType.Primitive.*;

public class XmlParser {
    private final CfgSchema cfg;

    public XmlParser(CfgSchema dst) {
        this.cfg = dst;
    }

    public void parse(Path xml, boolean includeSubDirectory) {
        if (includeSubDirectory) {
            parseXmlInAllSubDirectory(xml, "");
        } else {
            parseXml(xml, "");
        }
    }

    private void parseXmlInAllSubDirectory(Path topXml, String pkgNameDot) {
        if (Files.exists(topXml)) {
            parseXml(topXml, pkgNameDot);
        }
        try {
            try (Stream<Path> paths = Files.list(topXml.toAbsolutePath().getParent())) {
                for (Path path : paths.toList()) {
                    if (Files.isDirectory(path)) {
                        String subPkgName = path.getFileName().toString().toLowerCase();
                        Path xml = path.resolve(subPkgName + ".xml");
                        parseXmlInAllSubDirectory(xml, pkgNameDot + subPkgName + ".");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseXml(Path xml, String pkgNameDot) {
        Element self = DomUtils.rootElement(xml.toFile());
        for (Element e : DomUtils.elements(self, "bean")) {
            Fieldable f;
            if (e.hasAttribute("enumRef")) {
                f = parseInterface(e, pkgNameDot);
            } else {
                f = parseStruct(e, pkgNameDot);
            }

            Fieldable old = cfg.structs().put(f.name(), f);
            require(null == old, "Bean名字重复: " + f.name());
        }
        for (Element e : DomUtils.elements(self, "table")) {
            TableSchema t = parseTable(e, pkgNameDot);
            TableSchema old = cfg.tables().put(t.name(), t);
            require(null == old, "table名字重复: " + t.name());
        }
    }

    private TableSchema parseTable(Element self, String pkgNameDot) {
        String name = self.getAttribute("name");
        KeySchema primaryKey = getKeySchema(self, "primaryKey");

        EntryType entry;
        if (self.hasAttribute("enum")) {
            entry = new EntryType.EEnum(self.getAttribute("enum"));
        } else if (self.hasAttribute("entry")) {
            entry = new EntryType.EEntry(self.getAttribute("entry"));
        } else {
            entry = EntryType.ENo.NO;
        }
        boolean isColumnMode = self.hasAttribute("isColumnMode");

        List<FieldSchema> fields = parseFieldList(self);
        List<ForeignKeySchema> foreignKeys = parseForeignKeyList(self);
        List<KeySchema> uniqueKeys = new ArrayList<>();
        for (Element ele : DomUtils.elements(self, "uniqueKey")) {
            uniqueKeys.add(getKeySchema(ele, "keys"));
        }
        return new TableSchema(pkgNameDot + name, primaryKey, entry, isColumnMode,
                new Metadata(new LinkedHashMap<>()), fields, foreignKeys, uniqueKeys);
    }

    private KeySchema getKeySchema(Element self, String attr) {
        String[] keys = DomUtils.parseStringArray(self, attr);
        return new KeySchema(Arrays.asList(keys));
    }

    private Fieldable parseStruct(Element self, String pkgNameDot) {
        String name = self.getAttribute("name");
        FieldFormat fmt = parseBeanFmt(self);
        List<FieldSchema> fields = parseFieldList(self);
        List<ForeignKeySchema> foreignKeys = parseForeignKeyList(self);
        return new StructSchema(pkgNameDot + name, fmt, new Metadata(new LinkedHashMap<>()),
                fields, foreignKeys);
    }

    private InterfaceSchema parseInterface(Element self, String pkgNameDot) {
        String name = self.getAttribute("name");
        FieldFormat fmt = parseBeanFmt(self);
        String enumRef = self.getAttribute("enumRef");
        String defaultBeanName = self.getAttribute("defaultBeanName");

        Map<String, StructSchema> impls = new HashMap<>();
        for (Element subSelf : DomUtils.elements(self, "bean")) {
            Fieldable subF = parseStruct(subSelf, "");
            StructSchema old = impls.put(subF.name(), (StructSchema) subF);
            require(null == old, "Bean名字重复: " + subF.name());
        }

        return new InterfaceSchema(pkgNameDot + name, enumRef, defaultBeanName,
                fmt, new Metadata(new LinkedHashMap<>()), impls);
    }

    private FieldFormat parseBeanFmt(Element self) {
        FieldFormat fmt = AUTO;
        String sep = null;
        if (self.hasAttribute("compress")) { // 改为packSep吧
            sep = self.getAttribute("compress");
        } else if (self.hasAttribute("packSep")) {
            sep = self.getAttribute("packSep");
        }
        if (sep != null) {
            require(sep.length() == 1, "分隔符pack长度必须为1");
            char packSeparator = sep.toCharArray()[0];
            fmt = new FieldFormat.Sep(packSeparator);
        }
        return fmt;
    }

    private List<FieldSchema> parseFieldList(Element self) {
        List<FieldSchema> fields = new ArrayList<>();
        for (Element ele : DomUtils.elements(self, "column")) {
            FieldSchema field = parseField(ele);
            fields.add(field);
        }
        return fields;
    }

    private List<ForeignKeySchema> parseForeignKeyList(Element self) {
        List<ForeignKeySchema> foreignKeys = new ArrayList<>();
        for (Element ele : DomUtils.elements(self, "column")) {
            if (ele.hasAttribute("ref")) {
                ForeignKeySchema fk = parseForeignKey(ele, true);
                foreignKeys.add(fk);
            }
        }

        for (Element ele : DomUtils.elements(self, "foreignKey")) {
            ForeignKeySchema fk = parseForeignKey(ele, false);
            foreignKeys.add(fk);
        }

        return foreignKeys;
    }

    private FieldSchema parseField(Element self) {
        Metadata meta = new Metadata(new LinkedHashMap<>());
        String name = self.getAttribute("name");
        String comment = self.getAttribute("desc");
        if (comment.trim().equalsIgnoreCase(name.trim())) {
            comment = "";
        }
        if (!comment.isEmpty()) {
            meta.data().putLast("m", new Metadata.MetaStr(comment));
        }

        FieldType type;
        FieldFormat fmt = AUTO;

        require(self.hasAttribute("type"), "column必须设置type");
        String typ = self.getAttribute("type").trim();

        if (typ.startsWith("list,")) {
            String[] sp = typ.split(",");
            String v = sp[1].trim();
            FieldType item = parseSimpleType(v);
            type = new FList(item);

            if (sp.length > 2) {
                int c = Integer.parseInt(sp[2].trim());
                fmt = new FieldFormat.Fix(c);
            }

        } else if (typ.startsWith("map,")) {
            String[] sp = typ.split(",");
            String k = sp[1].trim();
            String v = sp[2].trim();
            FieldType key = parseSimpleType(k);
            FieldType value = parseSimpleType(v);
            type = new FMap(key, value);

            if (sp.length > 3) {
                int c = Integer.parseInt(sp[3].trim());
                fmt = new FieldFormat.Fix(c);
            }

        } else {
            type = parseSimpleType(typ);
        }

        if (self.hasAttribute("block")) {
            fmt = new FieldFormat.Block(1); //block不允许和pack一起配置，简单点
        } else if (self.hasAttribute("pack") || self.hasAttribute("compressAsOne")) {
            fmt = PACK;
        } else if (self.hasAttribute("packSep") || self.hasAttribute("compress")) {  // compress改为packSep
            String sep = self.hasAttribute("packSep") ?
                    self.getAttribute("packSep") : self.getAttribute("compress");
            require(sep.length() == 1, "packSep字符串长度必须是1, " + sep);
            char packSeparator = sep.toCharArray()[0];
            fmt = new FieldFormat.Sep(packSeparator);
        }

        return new FieldSchema(name, type, fmt, meta);
    }

    private ForeignKeySchema parseForeignKey(Element self, boolean isTagColumn) {
        String name = self.getAttribute("name");
        KeySchema localKey;
        if (isTagColumn) {
            localKey = new KeySchema(List.of(name));
        } else {
            localKey = getKeySchema(self, "keys");
        }

        String refstr = self.getAttribute("ref");
        String[] r = refstr.split(",");
        String refTable = r[0];
        RefKey refKey;
        boolean nullable = false;
        boolean isList = false;
        if (self.hasAttribute("refType")) {
            String rt = self.getAttribute("refType");
            if (rt.equalsIgnoreCase("nullable")) {
                nullable = true;
            } else if (rt.equalsIgnoreCase("list")) {
                isList = true;
            }
        }
        if (r.length > 1) {
            String[] rs = Arrays.copyOfRange(r, 1, r.length);
            KeySchema keySchema = new KeySchema(Arrays.asList(rs));

            if (isList) {
                refKey = new RefList(keySchema);
            } else {
                refKey = new RefKey.RefUniq(keySchema, nullable);
            }
        } else {
            refKey = new RefPrimary(nullable);
        }

        return new ForeignKeySchema(name, localKey, refTable, refKey, new Metadata(new LinkedHashMap<>()));
    }

    private FieldType parseSimpleType(String typ) {
        return switch (typ) {
            case "int" -> INT;
            case "long" -> LONG;
            case "bool" -> BOOL;
            case "float" -> FLOAT;
            case "string" -> Primitive.STR;
            case "text" -> TEXT;
            case "res" -> RES;
            default -> new StructRef(typ);
        };
    }

    protected void require(boolean cond, Object detailMessage) {
        if (!cond)
            throw new AssertionError(detailMessage);
    }

    public static void main(String[] args) {
        CfgSchema cfg = new CfgSchema(new TreeMap<>(), new TreeMap<>());
        XmlParser parser = new XmlParser(cfg);
        parser.parse(Path.of("config.xml"), true);

        System.out.println(cfg);
    }
}
