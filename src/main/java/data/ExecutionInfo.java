package data;

/**
 * An immutable wrapper class that stores the id, name, and probe count of an ExecutionData
 */
public class ExecutionInfo {
    private final long _id;
    private final String _name;
    private final int _probe_count;

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

}
