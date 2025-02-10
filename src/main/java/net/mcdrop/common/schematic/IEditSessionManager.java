package net.mcdrop.common.schematic;

import com.sk89q.worldedit.EditSession;

public interface IEditSessionManager {

    void addSession(EditSession session);

    void undoLastSession();

    void clearAllSessions();
}
