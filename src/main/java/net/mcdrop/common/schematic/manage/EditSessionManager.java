package net.mcdrop.common.schematic.manage;

import com.sk89q.worldedit.EditSession;
import net.mcdrop.common.schematic.IEditSessionManager;

import java.util.Stack;

public class EditSessionManager implements IEditSessionManager {
    private final Stack<EditSession> sessionStack = new Stack<>();

    @Override
    public void addSession(EditSession session) {
        if (session != null) {
            sessionStack.push(session);
        }
    }

    @Override
    public void undoLastSession() {
        if (!sessionStack.isEmpty()) {
            try (EditSession lastSession = sessionStack.pop()) {
                lastSession.undo(lastSession);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void clearAllSessions() {
        while (!sessionStack.isEmpty()) {
            EditSession session = sessionStack.pop();
            session.close();
        }
    }
}