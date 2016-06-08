package com.whammich.sstow.compat.theoneprobe;

import com.google.common.base.Function;
import com.whammich.sstow.compat.theoneprobe.provider.DataProviderCage;
import mcjty.theoneprobe.api.ITheOneProbe;

import javax.annotation.Nullable;

public class HandlerTheOneProbe implements Function<ITheOneProbe, Void> {

    public static ITheOneProbe theOneProbe;

    @Nullable
    @Override
    public Void apply(@Nullable ITheOneProbe input) {
        theOneProbe = input;
        theOneProbe.registerProvider(new DataProviderCage());
        return null;
    }
}
