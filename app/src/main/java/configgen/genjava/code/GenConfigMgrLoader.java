package configgen.genjava.code;

import configgen.util.CachedIndentPrinter;
import configgen.value.AllValue;
import configgen.value.VTable;

class GenConfigMgrLoader {

    static void generate(AllValue vdb, CachedIndentPrinter ps) {
        ps.println("package %s;", Name.codeTopPkg);
        ps.println();

        ps.println("import java.util.LinkedHashMap;");
        ps.println("import java.util.Map;");
        ps.println();

        ps.println("public class ConfigMgrLoader {");
        ps.println();

        ps.println1("public static ConfigMgr load(configgen.genjava.ConfigInput input) {");
        ps.println2("ConfigMgr mgr = new ConfigMgr();");

        int cnt = 0;
        for (VTable vTable : vdb.getVTables()) {
            if (vTable.getTTable().getTableDefine().isEnumFull() && vTable.getTTable().getTableDefine().isEnumHasOnlyPrimaryKeyAndEnumStr()) {
                continue;
            }
            cnt++;
        }

        ps.println2("int c = input.readInt();");
        ps.println2("if (c < %d) {", cnt);
        ps.println3("throw new IllegalArgumentException();");
        ps.println2("}");
        ps.println();

        ps.println2("Map<String, ConfigLoader> allConfigLoaders = getAllConfigLoaders();");
        ps.println2("for (int i = 0; i < c; i++) {");
        ps.println3("String tableName = input.readStr();");
        ps.println3("int tableSize = input.readInt();");
        ps.println3("ConfigLoader configLoader = allConfigLoaders.get(tableName);");
        ps.println3("if (configLoader != null) {");
        ps.println4("configLoader.createAll(mgr, input);");
        ps.println3("} else {");
        ps.println4("input.skipBytes(tableSize);");
        ps.println3("}");
        ps.println2("}");
        ps.println();

        ps.println2("for (ConfigLoader configLoader : allConfigLoaders.values()) {");
        ps.println3("configLoader.resolveAll(mgr);");
        ps.println2("}");
        ps.println();

        ps.println2("return mgr;");
        ps.println1("}");
        ps.println();

        ps.println1("private static Map<String, ConfigLoader> getAllConfigLoaders() {");
        ps.println2("Map<String, ConfigLoader> allConfigLoaders = new LinkedHashMap<>();");
        for (VTable vTable : vdb.getVTables()) {
            if (vTable.getTTable().getTableDefine().isEnumFull() && vTable.getTTable().getTableDefine().isEnumHasOnlyPrimaryKeyAndEnumStr()) {
                continue;
            }

            ps.println2("allConfigLoaders.put(\"%s\", new %s._ConfigLoader());", vTable.name, Name.tableDataFullName(vTable.getTTable()));
        }
        ps.println();
        ps.println2("return allConfigLoaders;");
        ps.println1("}");
        ps.println("}");
    }

}
