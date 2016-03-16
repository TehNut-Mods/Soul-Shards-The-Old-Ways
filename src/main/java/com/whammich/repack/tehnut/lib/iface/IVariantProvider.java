package com.whammich.repack.tehnut.lib.iface;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IVariantProvider {

    List<Pair<Integer, String>> getVariants();
}
