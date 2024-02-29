package net.lyflow.skyblock.loader.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

public class EmptyListToNullFactory implements TypeAdapterFactory {

    private final boolean wasCreatedByJsonAdapter;

    public static final EmptyListToNullFactory INSTANCE = new EmptyListToNullFactory(false);

    private EmptyListToNullFactory(boolean wasCreatedByJsonAdapter) {
        this.wasCreatedByJsonAdapter = wasCreatedByJsonAdapter;
    }

    private EmptyListToNullFactory() {
        this(true);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<?> rawType = type.getRawType();
        if (!List.class.isAssignableFrom(rawType)) {
            return null;
        }

        // Safe; the check above made sure type is List
        @SuppressWarnings("unchecked")
        TypeAdapter<List<Object>> delegate = (TypeAdapter<List<Object>>) (wasCreatedByJsonAdapter ?
                gson.getAdapter(type) : gson.getDelegateAdapter(this, type));

        @SuppressWarnings("unchecked")
        TypeAdapter<T> adapter = (TypeAdapter<T>) new TypeAdapter<List<Object>>() {
            @Override
            public List<Object> read(JsonReader in) throws IOException {
                return delegate.read(in);
            }

            @Override
            public void write(JsonWriter out, List<Object> value) throws IOException {
                if (value == null || value.isEmpty()) {
                    // Call the delegate instead of directly writing null in case the delegate has
                    // special null handling
                    delegate.write(out, null);
                } else {
                    delegate.write(out, value);
                }
            }
        };
        return adapter;
    }
}