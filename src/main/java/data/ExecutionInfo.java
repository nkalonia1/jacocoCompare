package data;

import org.jacoco.core.data.ExecutionData;

/**
 * An immutable wrapper class that stores the id, name, and probe count of a (R)ExecutionData
 */
public class ExecutionInfo {
    private final long _id;
    private final String _name;
    private final int _probe_count;

    public static ExecutionInfo getInfo(ExecutionData data) {
        return new ExecutionInfo(data.getId(), data.getName(), data.getProbes().length);
    }

    public static ExecutionInfo getInfo(RExecutionData data) {
        return new ExecutionInfo(data.getId(), data.getName(), data.getProbeCount());
    }

    public ExecutionInfo(long id, String name, int probe_count) {
        _id = id;
        _name = name;
        _probe_count = probe_count;
    }

    public long getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getProbeCount() {
        return _probe_count;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof ExecutionInfo) ) return false;
        ExecutionInfo i = (ExecutionInfo) o;
        return _id == i._id && _name.equals(i._name) && _probe_count == i._probe_count;
    }

    @Override
    public int hashCode() {
        int id_1 = (int) _id;
        int id_2 = (int) (_id >> 32);
        int name = _name.hashCode();
        return id_1 + id_2 + name + _probe_count;
    }

}
