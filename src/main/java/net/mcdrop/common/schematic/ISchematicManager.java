package net.mcdrop.common.schematic;

import org.bukkit.Location;

import java.io.File;

public interface ISchematicManager {
    void pasteSchematic(Location location, File schematicFile);

    void undoLastPaste();
}