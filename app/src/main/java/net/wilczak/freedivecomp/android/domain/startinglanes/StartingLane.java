package net.wilczak.freedivecomp.android.domain.startinglanes;

import java.util.ArrayList;
import java.util.List;

public class StartingLane {
    private boolean exists;
    private String id;
    private String shortName, fullName;
    private StartingLane parent;
    private List<StartingLane> children;

    public StartingLane(String id, String shortName) {
        this(id, shortName, true);
    }

    private StartingLane(String id, String shortName, boolean exists) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = shortName;
        this.exists = exists;
        this.children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public boolean doesExist() {
        return exists;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public StartingLane getParent() {
        return parent;
    }

    public List<StartingLane> getChildren() {
        return children;
    }

    public StartingLane addChild(StartingLane child) {
        children.add(child);
        child.parent = this;
        child.refreshFullName(fullName);
        return this;
    }

    private void refreshFullName(String parentFullName) {
        this.fullName = parentFullName + " " + shortName;
        for (StartingLane child : children) {
            child.refreshFullName(this.fullName);
        }
    }

    public static StartingLane missing(String startingLaneId) {
        return new StartingLane(startingLaneId, startingLaneId, false);
    }
}
