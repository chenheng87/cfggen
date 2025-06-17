package configgen.validator;

import configgen.value.CfgValue;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A validator that uses the GraalVM JavaScript engine to execute validation
 * scripts.
 * It operates in a sandboxed environment and provides caching for compiled
 * scripts.
 */
public class JavaScriptValidator {

    private final Context jsContext;
    private final Map<String, Source> scriptCache = new ConcurrentHashMap<>();

    public JavaScriptValidator() {
        this.jsContext = Context.newBuilder("js")
                .allowHostAccess(HostAccess.newBuilder(HostAccess.ALL).build())
                .build();
    }

    /**
     * Evaluates a JavaScript validation script against a row of data.
     *
     * @param vStruct The row data (VStruct) to validate.
     * @param script  The JavaScript code to execute. The script should return a
     *                boolean.
     * @return The boolean result of the script execution.
     */
    public boolean evaluate(CfgValue.VStruct vStruct, String script) {
        Source source = scriptCache.computeIfAbsent(script, s -> {
            try {
                return Source.create("js", s);
            } catch (Exception e) {
                throw new RuntimeException("Failed to compile JavaScript", e);
            }
        });

        // Prepare bindings
        Value bindings = jsContext.getBindings("js");
        Map<String, Object> rowMap = vStructToMap(vStruct);
        bindings.putMember("row", rowMap);

        Value result = jsContext.eval(source);

        if (result.isBoolean()) {
            return result.asBoolean();
        }
        // If the script doesn't return a boolean, validation fails by default.
        System.err.println("Warning: JS validation script did not return a boolean value. Got: " + result);
        return false;
    }

    /**
     * Converts a VStruct into a Map that is easily accessible from JavaScript.
     */
    private Map<String, Object> vStructToMap(CfgValue.VStruct vStruct) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < vStruct.schema().fields().size(); i++) {
            String fieldName = vStruct.schema().fields().get(i).name();
            Object unwrappedValue = unwrapValue(vStruct.values().get(i));
            map.put(fieldName, unwrappedValue);
        }
        return map;
    }

    /**
     * Unwraps a CfgValue.Value into a primitive Java type or a List/Map.
     */
    private Object unwrapValue(CfgValue.Value value) {
        return switch (value) {
            case CfgValue.VBool v -> v.value();
            case CfgValue.VInt v -> v.value();
            case CfgValue.VLong v -> v.value();
            case CfgValue.VFloat v -> v.value();
            case CfgValue.VString v -> v.value();
            case CfgValue.VText v -> v.value();
            case CfgValue.VList v -> v.valueList().stream().map(this::unwrapValue).toList();
            case CfgValue.VMap v -> {
                Map<Object, Object> unwrappedMap = new HashMap<>();
                v.valueMap().forEach((k, val) -> unwrappedMap.put(unwrapValue(k), unwrapValue(val)));
                yield unwrappedMap;
            }
            case CfgValue.VStruct v -> vStructToMap(v);
            default -> value.toString(); // Fallback for complex types
        };
    }
}