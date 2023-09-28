package configgen.schema;

import java.util.List;
import java.util.Objects;

public sealed interface RefKey {
    List<String> keyNames();

    RefKey copy();

    record RefPrimary(boolean nullable) implements RefKey {
        @Override
        public List<String> keyNames() {
            return List.of();
        }

        @Override
        public RefPrimary copy() {
            return this;
        }
    }

    record RefUniq(KeySchema key, boolean nullable) implements RefKey {
        public RefUniq {
            Objects.requireNonNull(key);
        }

        @Override
        public List<String> keyNames() {
            return key.name();
        }

        @Override
        public RefUniq copy() {
            return new RefUniq(key.copy(), nullable);
        }
    }

    record RefList(KeySchema key) implements RefKey {
        public RefList {
            Objects.requireNonNull(key);
        }

        @Override
        public List<String> keyNames() {
            return key.name();
        }

        @Override
        public RefList copy() {
            return new RefList(key.copy());
        }
    }
}
