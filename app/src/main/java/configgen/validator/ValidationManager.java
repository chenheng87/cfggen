package configgen.validator;

import com.google.gson.reflect.TypeToken;
import configgen.validator.ast.EvaluationContext;
import configgen.validator.ast.Expression;
import configgen.validator.parser.ScriptLexer;
import configgen.validator.parser.ScriptParser;
import configgen.validator.parser.Token;
import configgen.value.CfgValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import configgen.value.ForeachVStruct;

/**
 * Manages loading, parsing, and caching validation scripts from a configuration
 * file.
 */
public class ValidationManager {
    private static final Gson gson = new Gson();

    public interface ValidationRule {
        boolean evaluate(CfgValue.VStruct vStruct);

        String getDescription();
    }

    private static class RuleDto {
        String code;
        String desc;
    }

    private record SimpleRule(Expression expression, String description) implements ValidationRule {
        @Override
        public boolean evaluate(CfgValue.VStruct vStruct) {
            Object res = expression.evaluate(new EvaluationContext(vStruct));
            if (res instanceof Boolean b) {
                return b;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    private record JsRule(String script, String description, JavaScriptValidator jsValidator)
            implements ValidationRule {
        @Override
        public boolean evaluate(CfgValue.VStruct vStruct) {
            return jsValidator.evaluate(vStruct, script);
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    private final String engineType;
    private final Map<String, List<ValidationRule>> rulesCache = new HashMap<>();
    private JavaScriptValidator jsValidator; // Lazily initialized

    public ValidationManager(String engineType) {
        this.engineType = Objects.requireNonNull(engineType);
        if (engineType.equals("js")) {
            this.jsValidator = new JavaScriptValidator();
        }
    }

    /**
     * Loads validation rules from the specified file.
     * The file format is expected to be:
     * tableName: [{code:"...", desc:"..."}, {code:"...", desc:"..."}]
     *
     * @param path The path to the validation script file.
     * @throws IOException If there is an error reading the file.
     */
    public void loadScripts(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return; // Not an error if the validation file doesn't exist.
        }

        String content = Files.readString(path);
        Map<String, List<RuleDto>> allRules = gson.fromJson(content, new TypeToken<Map<String, List<RuleDto>>>() {}.getType());

        if (allRules != null) {
            for (Map.Entry<String, List<RuleDto>> entry : allRules.entrySet()) {
                String tableName = entry.getKey();
                List<RuleDto> ruleDtos = entry.getValue();

                List<ValidationRule> rules = new ArrayList<>();
                for (RuleDto dto : ruleDtos) {
                    rules.add(parseRuleObject(dto));
                }
                rulesCache.put(tableName, rules);
            }
        }
    }


    private ValidationRule parseRuleObject(RuleDto dto) {
        String script = dto.code;
        String desc = dto.desc;

        return switch (engineType) {
            case "js" -> new JsRule(script, desc, jsValidator);
            case "simple" -> {
                ScriptLexer lexer = new ScriptLexer(script);
                List<Token> tokens = lexer.scanTokens();
                ScriptParser parser = new ScriptParser(tokens);
                Expression expression = parser.parse();
                yield new SimpleRule(expression, desc);
            }
            default -> throw new IllegalArgumentException("Unknown validator engine type: " + engineType);
        };
    }

    /**
     * Retrieves the list of validation rules for a given table.
     *
     * @param tableName The name of the table.
     * @return A list of validation rules, or null if no scripts are defined for the
     *         table.
     */
    public List<ValidationRule> getRulesForTable(String tableName) {
        return rulesCache.get(tableName);
    }

    public void validate(CfgValue cfgValue) {
        AtomicInteger errorCount = new AtomicInteger(0);
        for (var tableRules : rulesCache.entrySet()) {
            var table = cfgValue.getTable(tableRules.getKey());
            if (table == null) {
                System.err.println("No table found for " + tableRules.getKey() + ", skipping validation.");
                continue;
            }
            ForeachVStruct.foreachVTable((vStruct, ctx) -> {
                for (ValidationRule rule : tableRules.getValue()) {
                    try {
                        if (!rule.evaluate(vStruct)) {
                            errorCount.incrementAndGet();
                            String key = ctx.pkValue().packStr();
                            System.err.printf("Validation failed for table '%s' at key=[%s]: %s.\n",
                                    tableRules.getKey(), key, rule.getDescription());
                        }
                    } catch (Exception e) {
                        errorCount.incrementAndGet();
                        String key = ctx.pkValue().packStr();
                        System.err.printf("Exception during validation for table '%s' at key=[%s] (rule: '%s'): %s%n\n",
                                tableRules.getKey(), key, rule.getDescription(), e.getMessage());
                    }
                }
            }, table);
        }
        if (errorCount.get() > 0) {
            throw new RuntimeException("Configuration validation failed with " + errorCount.get() + " errors.");
        }
    }


}