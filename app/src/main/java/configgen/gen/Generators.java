package configgen.gen;

import java.util.LinkedHashMap;
import java.util.Map;

public class Generators {
    private static final Map<String, GeneratorProvider> providers = new LinkedHashMap<>();

    public static Generator create(String arg) {
        Argument parameter = new Argument(arg);
        GeneratorProvider provider = providers.get(parameter.type);
        if (provider == null) {
            System.err.println(parameter.type + " not support");
            return null;
        }
        return provider.create(parameter);
    }

    static void addProvider(String name, GeneratorProvider provider) {
        providers.put(name, provider);
    }

    static Map<String, GeneratorProvider> getAllProviders() {
        return providers;
    }
}
