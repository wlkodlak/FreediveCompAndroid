package net.wilczak.freedivecomp.android.ui.application;

import net.wilczak.freedivecomp.android.BR;

import java.util.Arrays;

public class DirtyProperties {
    private boolean complete;
    private int[] changes;
    private int count;

    public void add(int propertyId) {
        if (propertyId == BR._all) {
            complete = true;
            changes = null;
        } else if (changes == null) {
            changes = new int[4];
            changes[0] = propertyId;
            count = 1;
        } else {
            if (count >= changes.length) {
                changes = Arrays.copyOf(changes, changes.length * 2);
            }
            changes[count] = propertyId;
            count++;
        }
    }

    public boolean contains(int propertyId) {
        if (complete) return true;
        for (int i = 0; i < count; i++) {
            if (changes[i] == propertyId) return true;
        }
        return false;
    }
}
