package configgen.editorserver;

import configgen.schema.TableSchema;
import configgen.value.CfgValue;
import configgen.value.ValueErrs;
import configgen.value.ValuePack;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static configgen.editorserver.PromptService.PromptResultCode.*;


public class PromptService {

    public record AIExample(String id,
                            String description) {
    }

    public record PromptRequest(String role,
                                String table,
                                List<AIExample> examples) {
    }

    public record PromptResponse(PromptResultCode resultCode,
                                 String prompt) {

    }

    public enum PromptResultCode {
        ok,
        tableNotSet,
        tableNotFound,
        tableNotJson,
        exampleIdParseErr,
        exampleIdNotFound,
        exampleDescriptionEmpty,
    }

    private record ResolvedExample(String id,
                                   String description,
                                   CfgValue.VStruct record) {
    }

    private final CfgValue cfgValue;
    private final PromptRequest req;

    public PromptService(CfgValue cfgValue, PromptRequest req) {
        this.cfgValue = cfgValue;
        this.req = req;
    }

    public PromptResponse gen() {
        String table = req.table;
        if (table == null || table.isEmpty()) {
            return ofErr(tableNotSet);
        }

        CfgValue.VTable vTable = cfgValue.vTableMap().get(table);
        if (vTable == null) {
            return ofErr(tableNotFound);
        }

        if (!vTable.schema().meta().isJson()) {
            return ofErr(tableNotJson);
        }

        List<ResolvedExample> resolvedExamples = new ArrayList<>(req.examples.size());
        for (AIExample ex : req.examples) {
            if (ex.id == null) {
                return ofErr(exampleIdParseErr);
            }
            ValueErrs errs = ValueErrs.of();
            CfgValue.Value pkValue = ValuePack.unpackTablePrimaryKey(ex.id, vTable.schema(), errs);

            if (!errs.errs().isEmpty()) {
                for (ValueErrs.VErr err : errs.errs()) {
                    System.err.println(err);
                }
                return ofErr(exampleIdParseErr);
            }

            CfgValue.VStruct vRecord = vTable.primaryKeyMap().get(pkValue);
            if (vRecord == null) {
                return ofErr(exampleIdNotFound);
            }
            if (ex.description == null || ex.description.isEmpty()) {
                return ofErr(exampleDescriptionEmpty);
            }
            resolvedExamples.add(new ResolvedExample(ex.id, ex.description, vRecord));
        }
        return new PromptResponse(ok, genPrompt(vTable.schema(), resolvedExamples));
    }

    private StringBuilder sb;

    private String genPrompt(TableSchema tableSchema, List<ResolvedExample> resolvedExamples) {
        sb = new StringBuilder(4096);

        add("""
                - Role: {0}��json����ר��
                - Background: ���»����{1}��صĽṹ���壬�������csv�����ݣ�Ȼ���������������{1}�ṹ��json�ļ�
                    - {1}��صĽṹ���壬�ṹ�����е�```->```ָ���˴��ֶ��������һ��������
                """, req.role, req.table);

        add("\t```");
        addSchema(tableSchema);
        add("\t```");
        add("\t- ���csv������,���е�һ���Ǳ�ͷ���������ƣ�֮���Ǿ�������\n");
        addRelatedCsv(tableSchema);
        add("""
                - Profile: ����һλ����ḻ�߼����ܵ�{0}���ó�����������ת��Ϊ���Ͻṹ��json����
                - Skills: ��߱�������ҵ���顢��json��ʽ��������⣬�����ܹ�������ø��ֱ�����Ժ͹��������ɺ���֤json����
                - Goals: ������ת��Ϊ����{1}�ṹ�����json����
                - Constrains: ���ɵ�json���ݱ����ϸ����ض�������ݽṹ��ȷ�����ݵ�һ���Ժ���Ч�ԡ��������¹���
                    - ����������ֶ�ΪĬ��ֵ������Ժ��Դ��ֶ�
                        - �ֶ�����Ϊnumber��Ĭ��Ϊ0
                        - �ֶ�����Ϊarray��Ĭ��Ϊ[]
                        - �ֶ�����Ϊstr��Ĭ��Ϊ���ַ���
                    - ����Ҫ����$type�ֶΣ��������˶��������
                      - $type��ֵ �����ǽṹ������ģ�interface���ƣ��������Ǿ����struct���ơ�
                    - ������Լ���$note�ֶΣ���Ϊע�ͣ�����ȫ�����ӣ������Щע�ͺ��������������
                    - json�в�Ҫ����```//```��ͷ��ע��
                - OutputFormat: json
                - Examples:
                """, req.role, req.table);
        addExamples(resolvedExamples);
        add("-Initialization: �ڵ�һ�ζԻ��У���ֱ��������£����ã����ṩid����Ҫ��buff�������ҽ�������Щ��ϢΪ�����ɷ���{0}�ṹ��json����",
                req.table);
        return sb.toString();
    }

    private void addSchema(TableSchema tableSchema) {
        
    }
    private void addRelatedCsv(TableSchema tableSchema) {

    }

    private void addExamples(List<ResolvedExample> resolvedExamples) {

    }



    private void add(String str) {
        sb.append(str);
    }

    private void add(String fmt, String arg1) {
        sb.append(MessageFormat.format(fmt, arg1));
    }

    private void add(String fmt, String arg1, String arg2) {
        sb.append(MessageFormat.format(fmt, arg1, arg2));
    }

    private PromptResponse ofErr(PromptResultCode code) {
        return new PromptResponse(code, "");
    }

}
