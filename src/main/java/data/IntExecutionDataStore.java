package data;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.IExecutionDataVisitor;

import java.util.*;

/**
 * A secondary version of ExecutionDataStore that keeps execution data as IntExecutionData.
 * This class must be converted from an existing ExecutionDataStore
 */
public class IntExecutionDataStore implements IExecutionDataVisitor {
    private Map<ExecutionInfo, IntExecutionData> _store;
    private Set<String> _names;


    public IntExecutionDataStore() {
        _store = new HashMap<ExecutionInfo, IntExecutionData>();
        _names = new HashSet<String>();
    }

    public Collection<IntExecutionData> getContents() {
        return new ArrayList<IntExecutionData>(_store.values());
    }

    public void put(ExecutionData data) {
        get(new ExecutionInfo(data)).add(data);
    }

    public void put(IntExecutionData data) {
        get(data.getInfo()).add(data);
    }

    public void put(ExecutionDataStore store) {
        for ( ExecutionData item : store.getContents()) {
            put(item);
        }
    }

    public void put(IntExecutionDataStore store) {
        for ( IntExecutionData item : store._store.values()) {
            put(item);
        }
    }

    public IntExecutionData get(ExecutionInfo id) {
        if (_store.containsKey(id)) {
            return _store.get(id);
        } else {
            _names.add(id.getName());
            return _store.put(id, new IntExecutionData(id));
        }
    }

    public boolean contains(String name) {
        return _names.contains(name);
    }

    public boolean contains(ExecutionInfo info) {
        return _store.containsKey(info);
    }

    public void reset() {
        for ( IntExecutionData item : _store.values()) {
            item.reset();
        }
    }

    public void subtract(ExecutionData data) {
        get(new ExecutionInfo(data)).subtract(data);
    }

    public void subtract(IntExecutionData data) {
        get(data.getInfo()).subtract(data);
    }

    public void subtract(ExecutionDataStore store) {
        for (ExecutionData data : store.getContents()) {
            subtract(data);
        }
    }

    public void subtract(IntExecutionDataStore store) {
        for (IntExecutionData data : store.getContents()) {
            subtract(data);
        }
    }

    public void accept(IExecutionDataVisitor visitor) {
        for (IntExecutionData data : _store.values()) {
            visitor.visitClassExecution(data.toExecutionData());
        }
    }

    /**
     * Compares this class to another IntExecutionDataStore. Returns a new IntExecutionDataStore that represents the
     * intersection of the IntExecutionData in these two objects. This function will not modify either object.
     * See the intersect() function in IntExecutionData for more details.
     */
    public IntExecutionDataStore intersect(IntExecutionDataStore other) {
        IntExecutionDataStore out = new IntExecutionDataStore();
        for (IntExecutionData data : _store.values()) {
            if (other.contains(data.getInfo())) {
                out.put(data.intersect(other.get(data.getInfo())));
            }
        }
        return out;
    }

    @Override
    public void visitClassExecution(ExecutionData data) {
        put(data);
    }
}
