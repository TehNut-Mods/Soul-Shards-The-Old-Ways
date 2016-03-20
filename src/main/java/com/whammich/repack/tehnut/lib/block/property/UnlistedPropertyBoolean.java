package com.whammich.repack.tehnut.lib.block.property;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyBoolean implements IUnlistedProperty<Boolean> {
    private String propName;

    public UnlistedPropertyBoolean(String propName) {
        this.propName = propName;
    }

    @Override
    public String getName() {
        return propName;
    }

    @Override
    public boolean isValid(Boolean value) {
        return true;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public String valueToString(Boolean value) {
        return value.toString();
    }
}
