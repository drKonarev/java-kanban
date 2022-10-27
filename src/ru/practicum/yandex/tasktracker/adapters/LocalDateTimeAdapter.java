package ru.practicum.yandex.tasktracker.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd. MM. yyyy; HH:mm");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd. MM. yyyy; HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, LocalDateTime localDate) throws IOException {
        LocalDateTime value = Objects.nonNull(localDate) ? localDate : LocalDateTime.MAX;
        jsonWriter.value(value.format(formatterWriter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}