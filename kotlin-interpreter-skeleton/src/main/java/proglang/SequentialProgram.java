package proglang;

import java.util.Map;
import java.util.HashMap;

public final class SequentialProgram {
    private Stmt topLevelStatement;

    public SequentialProgram(Stmt topLevelStatement) {
        this.topLevelStatement = topLevelStatement;
    }

    public Map<String, Integer> execute(Map<String, Integer> initialStore) {
        Map<String, Integer> workingStore = new HashMap<String, Integer>();
        for (String storeEntryKey: initialStore.keySet()) {
            workingStore.put(storeEntryKey, initialStore.get(storeEntryKey));
        }
        // Made a copy of the initial Store and it's now in workingStore
        while (topLevelStatement != null) topLevelStatement = StmtKt.step(topLevelStatement, workingStore);
        return workingStore;
    }

    @Override
    public String toString() {
        return topLevelStatement.toString();
    }
}
