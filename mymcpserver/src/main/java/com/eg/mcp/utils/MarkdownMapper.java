package com.eg.mcp.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

/*
 * Just a quick Mapper.
 * If it breaks please fix yourself.
 */
@Component
public class MarkdownMapper {

    public String writeValueAsString(Object obj) {
        return toMarkdown(obj, 0);
    }

    private String toMarkdown(Object obj, int depth) {
        if (obj == null) return indent(depth) + "_null_\n";

        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder();

        if (depth == 0) {
            sb.append("## ").append(clazz.getSimpleName()).append("\n\n");
        }

        if (clazz.isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object item = Array.get(obj, i);
                sb.append(renderAnonymousItem(item, depth));
            }
            return sb.toString();
        }

        if (clazz.isRecord()) {
            for (RecordComponent component : clazz.getRecordComponents()) {
                try {
                    Object value = component.getAccessor().invoke(obj);
                    sb.append(renderValue(component.getName(), value, depth));
                } catch (Exception e) {
                    sb.append(indent(depth)).append("- **").append(component.getName()).append("**: _<error>_\n");
                }
            }
        } else if (!isPrimitiveOrWrapper(clazz)) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    sb.append(renderValue(field.getName(), value, depth));
                } catch (Exception e) {
                    sb.append(indent(depth)).append("- **").append(field.getName()).append("**: _<inaccessible>_\n");
                }
            }
        } else {
            sb.append(indent(depth)).append("- ").append(obj).append("\n");
        }

        return sb.toString();
    }

    private String renderValue(String name, Object value, int depth) {
        if (value == null) {
            return indent(depth) + "- **" + name + "**: _null_\n";
        }

        Class<?> valueClass = value.getClass();

        // Render image if it's a URL
        if (value instanceof String str) {
            if (isImageUrl(str)) {
                return indent(depth) + "- **" + name + "**: ![" + name + "](" + str + ")\n";
            } else {
                return indent(depth) + "- **" + name + "**: " + str + "\n";
            }
        }

        // Render collection
        if (value instanceof Collection<?> collection) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent(depth)).append("- **").append(name).append("**:\n");
            for (Object item : collection) {
                sb.append(toMarkdown(item, depth + 1));
            }
            return sb.toString();
        }

        // Render arrays
        if (valueClass.isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent(depth)).append("- **").append(name).append("**:\n");
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                Object item = Array.get(value, i);
                sb.append(toMarkdown(item, depth + 1));
            }
            return sb.toString();
        }

        // Render maps
        if (value instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent(depth)).append("- **").append(name).append("**:\n");
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sb.append(indent(depth + 1))
                        .append("- **").append(entry.getKey()).append("**: ")
                        .append(entry.getValue()).append("\n");
            }
            return sb.toString();
        }

        // Primitive or wrapper
        if (isPrimitiveOrWrapper(valueClass)) {
            return indent(depth) + "- **" + name + "**: " + value + "\n";
        }

        // Nested object
        return indent(depth) + "- **" + name + "**:\n" + toMarkdown(value, depth + 1);
    }

    private String renderAnonymousItem(Object value, int depth) {
        if (value == null) {
            return indent(depth) + "- _null_\n";
        }

        Class<?> valueClass = value.getClass();

        // Basic types
        if (isPrimitiveOrWrapper(valueClass)) {
            return indent(depth) + "- " + value + "\n";
        }

        // Strings/images
        if (value instanceof String str) {
            if (isImageUrl(str)) {
                return indent(depth) + "- ![]( " + str + " )\n";
            } else {
                return indent(depth) + "- " + str + "\n";
            }
        }

        // Otherwise recurse
        return toMarkdown(value, depth + 1);
    }

    //TODO improve this method
    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Long.class
                || type == Double.class
                || type == Float.class
                || type == Boolean.class
                || type == Short.class
                || type == Byte.class
                || type == Character.class
                || type == LocalDateTime.class
                || type == Date.class
        		|| type == java.sql.Date.class;
    }

    private boolean isImageUrl(String value) {
        return (value.startsWith("http://") || value.startsWith("https://"))
                && (value.endsWith(".png") || value.endsWith(".jpg")
                || value.endsWith(".jpeg") || value.endsWith(".gif") || value.endsWith(".webp"));
    }

    private String indent(int depth) {
        return "  ".repeat(depth);
    }
}
