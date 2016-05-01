package com.whammich.sstow.util.serialization;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tehnut.lib.util.BlockStack;

import java.lang.reflect.Type;

public class SerializerBlockStack implements JsonDeserializer<BlockStack>, JsonSerializer<BlockStack> {

    @Override
    public BlockStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String name = json.getAsJsonObject().get("name").getAsString();
        int meta = 0;
        if (json.getAsJsonObject().get("meta") != null)
            meta = json.getAsJsonObject().get("meta").getAsInt();

        return new BlockStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)), meta);
    }

    @Override
    public JsonElement serialize(BlockStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", src.getBlock().getRegistryName().toString());
        if (src.getMeta() != 0)
            jsonObject.addProperty("meta", src.getMeta());

        return jsonObject;
    }
}
