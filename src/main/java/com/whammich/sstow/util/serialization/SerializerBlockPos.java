package com.whammich.sstow.util.serialization;

import com.google.gson.*;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Type;

public class SerializerBlockPos implements JsonDeserializer<BlockPos>, JsonSerializer<BlockPos> {

    @Override
    public BlockPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int x = json.getAsJsonObject().get("x").getAsInt();
        int y = json.getAsJsonObject().get("y").getAsInt();
        int z = json.getAsJsonObject().get("z").getAsInt();

        return new BlockPos(x, y, z);
    }

    @Override
    public JsonElement serialize(BlockPos src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("x", src.getX());
        jsonObject.addProperty("y", src.getY());
        jsonObject.addProperty("z", src.getZ());

        return jsonObject;
    }
}
