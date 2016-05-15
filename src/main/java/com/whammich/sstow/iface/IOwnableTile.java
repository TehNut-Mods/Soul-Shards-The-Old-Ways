package com.whammich.sstow.iface;

import java.util.UUID;

public interface IOwnableTile {

    UUID getOwnerUUID();

    String getOwnerDisplayName();
}
