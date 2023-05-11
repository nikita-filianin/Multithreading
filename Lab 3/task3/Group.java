package org.example.task3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    private String groupName;
    private Map<Integer, List<Mark>> students;

    public Group(String groupName, int sizeOfGroup) {
        this.groupName = groupName;
        this.students = new HashMap<>();
        setGroup(sizeOfGroup);
    }

    public void setGroup(int sizeOfGroup) {
        for (int i = 0; i < sizeOfGroup; i++) {
            students.put(i + 1, new ArrayList<>());
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public Map<Integer, List<Mark>> getStudents() {
        return students;
    }
}
