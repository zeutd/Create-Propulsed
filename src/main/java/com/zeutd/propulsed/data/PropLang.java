package com.zeutd.propulsed.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.zeutd.propulsed.Propulsed;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class PropLang {

    public static LangBuilder builder() {
        return Lang.builder(Propulsed.MOD_ID);
    }

    public static LangBuilder text(final String text) {
        return builder().text(text);
    }

    public static LangBuilder translate(final String key, final Object... args) {
        return builder().translate(key, args);
    }

    public static LangBuilder number(final double number) {
        return builder().text(LangNumberFormat.format(number));
    }

    public static LangBuilder space() {
        return builder().space();
    }

    public static void emptyLine(final List<Component> tooltip) {
        builder().text("").forGoggles(tooltip);
    }

    public static LangBuilder blockName(final BlockState blockState) {
        return builder().add(blockState.getBlock().getName());
    }

    public static List<Component> translatedOptions(final String prefix, final String... keys) {
        final List<Component> result = new ArrayList<>(keys.length);
        for (final String key : keys)
            result.add(translate((prefix != null ? prefix + "." : "") + key).component());
        return result;
    }

    public static LangBuilder kilopixelGram(final double value) {
        return translate("unit.kpg", String.format("%,.2f", value));
    }

    public static LangBuilder kilopixelGram(final double value, final String format) {
        return getPrefixedUnit("pg", value, format,1);
    }

    public static LangBuilder pixelNewton(final double value) {
        return pixelNewton(value, "%,.2f");
    }

    public static LangBuilder pixelNewton(final double value, final String format) {
        return getPrefixedUnit("pn", value, format,0);
    }

    private static LangBuilder getPrefixedUnit(String unit, double value, final String format, final int offset) {
        final String[] prefixes = {"k", "m", "g"};
        int index = offset-1;
        while (value >= 1000 && index < prefixes.length - 1) {
            value /= 1000;
            index++;
        }
        if(index >= 0)
            unit = prefixes[index] + unit;

        return translate("unit." + unit, format.formatted(value));
    }


    public static void registrateLang(final RegistrateLangProvider provider) {
        final BiConsumer<String, String> consumer = provider::add;

        final Map<String, String> lang = getLangMap("en_us");
        lang.forEach(consumer);
    }

    private static Map<String, String> getLangMap(final String lang) {
        final String filepath = "datagen/lang/%s.json".formatted(lang);
        final JsonObject langObject = FilesHelper.loadJsonResource(filepath).getAsJsonObject();

        final Map<String, String> langMap = new HashMap<>();
        flattenJson(langMap, langObject, null);
        return langMap;
    }

    private static void flattenJson(final Map<String, String> outputMap, final JsonElement element, final String currentPath) {
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            final String string = element.getAsJsonPrimitive().getAsString();
            outputMap.put(currentPath, string);
            return;
        }

        if(element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            for (final String key : object.keySet()) {
                final JsonElement value = object.get(key);
                final String path = currentPath != null ? currentPath + "." + key : key;
                flattenJson(outputMap, value, path);
            }
        }
    }
}
