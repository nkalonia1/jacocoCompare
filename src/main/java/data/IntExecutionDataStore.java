package data;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.IExecutionDataVisitor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * A secondary version of ExecutionDataStore that keeps execution data as IntExecutionData.
 * This class must be converted from an existing ExecutionDataStore
 */
public class IntExecutionDataStore implements IExecutionDataVisitor, Cloneable {
    private Path _source;
    private Map<ExecutionInfo, IntExecutionData> _store;
    private Set<String> _names;

    public IntExecutionDataStore() {
        this(Paths.get("."));
    }

    public IntExecutionDataStore(Path source) {
        _source = source;
        _store = new HashMap<ExecutionInfo, IntExecutionData>();
        _names = new HashSet<String>();
    }

    public Collection<IntExecutionData> getContents() {
        return new ArrayList<IntExecutionData>(_store.values());
    }

    public void put(ExecutionData data) {
        IntExecutionData lol = get(new ExecutionInfo(data));
        lol.add(data);
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
            IntExecutionData out  = new IntExecutionData(id);
            _store.put(id, out);
            return out;
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

    public boolean subtract(ExecutionData data) {
        return get(new ExecutionInfo(data)).subtract(data);
    }

    public boolean subtract(IntExecutionData data) {
        return get(data.getInfo()).subtract(data);
    }

    public Collection<String> subtract(ExecutionDataStore store) {
        Collection<String> out =  new HashSet<String>();
        for (ExecutionData data : store.getContents()) {
            if (subtract(data)) out.add(data.getName());
        }
        return out;
    }

    public Collection<String> subtract(IntExecutionDataStore store) {
        Collection<String> out = new HashSet<String>();
        for (IntExecutionData data : store.getContents()) {
            if (subtract(data)) out.add(data.getInfo().getName());
        }
        return out;
    }

    public void accept(IExecutionDataVisitor visitor) {
        for (IntExecutionData data : _store.values()) {
            visitor.visitClassExecution(data.toExecutionData());
        }
    }

    /**
     * Compares this class to another IntExecutionDataStore. Returns a new IntExecutionDataStore that represents the
     * intersection of the IntExecutionData in these two objects.
     * See the intersect() function in IntExecutionData for more details.
     */
    public void intersect(IntExecutionDataStore other) {
        for (IntExecutionData data : _store.values()) {
            if (other.contains(data.getInfo())) {
                data.intersect(other.get(data.getInfo()));
            }
        }
    }

    /**
     * Iterates through all of IntExecutionData in this object and executes their filter() methods, passing in the
     * corresponding IntExecutionData found in the parameter.
     * See the filter() method in IntExecutionData for more details.
     */
    public void filter(IntExecutionDataStore other) {
        for ( IntExecutionData data : _store.values() ) {
            if (other.contains(data.getInfo())) {
                data.filter(other.get(data.getInfo()));
            }
        }
    }

    public Path getSource() {
        return _source;
    }

    @Override
    public void visitClassExecution(ExecutionData data) {
        put(data);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        IntExecutionDataStore out = new IntExecutionDataStore();
        for (ExecutionInfo info : _store.keySet()) {
            out._store.put(info, (IntExecutionData) _store.get(info).clone());
        }
        for (String name : _names) {
            out._names.add(name);
        }
        return out;
    }
}
