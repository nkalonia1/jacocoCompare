package data;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.IExecutionDataVisitor;

import java.util.*;

/**
 * New ExecutionDataStore. This class is mostly equivalent to an ExecutionDataStore
 * with the exception of:
 *     1) It contains NExecutionData as opposed to ExecutionData
 *     2) The methods of storing and accessing information differ
 *     3) It has additional methods compared to ExecutionDataStore
 * Any operational methods in this class essentially iterate through each of the
 * NExecutionData in this class and calls the NExecutionData's corresponding
 * method.
 */
public class NExecutionDataStore implements IExecutionDataVisitor, RExecutionDataStore {

    /**
     * Two maps are used to store NExecutionData. One is a direct mapping from an
     * ExecutionInfo to a NExecutionData while the other just maps an id to a
     * NExecutionData. The latter map stores the NExecutionData in an ArrayList since
     * it is possible (though extremely unlikely) for an id collision to happen.
     */
    private Map<ExecutionInfo, NExecutionData> _info_map;
    private Map<Long, Set<ExecutionInfo>> _long_map;

    private Set<String> _names;

    public NExecutionDataStore() {
        _info_map = new HashMap<ExecutionInfo, NExecutionData>();
        _long_map = new HashMap<Long, Set<ExecutionInfo>>();
        _names = new HashSet<String>();
    }

    @Override
    public boolean contains(String name) {
        return _names.contains(name);
    }

    @Override
    public boolean contains(long id) {
        return _long_map.keySet().contains(id);
    }

    @Override
    public boolean contains(ExecutionInfo info) {
        return _info_map.keySet().contains(info);
    }

    @Override
    public Collection<NExecutionData> getContents() {
        return new ArrayList<NExecutionData>(_info_map.values());
    }

    public void put(NExecutionData data) {
        ExecutionInfo info = data.getInfo();
        _info_map.put(info, data);
        _names.add(info.getName());
        if (_long_map.containsKey(info.getId())) {
            Set<ExecutionInfo> set = _long_map.get(info.getId());
            set.add(info);
        } else {
            Set<ExecutionInfo> set = new HashSet<ExecutionInfo>(1, 1);
            _long_map.put(info.getId(), set);
            set.add(info);
        }
    }

    /**
     * Returns an array of all NExecutionData that contain the parameter as an id, in
     * no specific order. This function returns an array since it is technically
     * possible (though extremely unlikely) for two different NExecutionData to have
     * the same id. Modifying the array has no direct effect on this
     * NExecutionDataStore, but modifying the array's contents does. This method returns
     * an empty array if no NExecutionData satisfy the criteria.
     */
    @Override
    public NExecutionData[] get(long id) {
        if (!_long_map.containsKey(id)) return new NExecutionData[0];
        Set<ExecutionInfo> set = _long_map.get(id);
        NExecutionData[] out = new NExecutionData[set.size()];
        int i = 0;
        for (NExecutionData data : out) {
            out[i++] = data;
        }
        return out;
    }

    /**
     * Returns the NExecutionData corresponding to the provided ExecutionInfo in this
     * NExecutionDataStore. If no such NExecutionData exists, then an empty one is
     * created and added to the store. Therefore the size of the store increases with
     * every call for a nonexistent object in the store.
     */
    public NExecutionData getMutable(ExecutionInfo info) {
        if (_info_map.containsKey(info)) return _info_map.get(info);
        NExecutionData out = new NExecutionData(info);
        put(out);
        return out;
    }

    @Override
    public RExecutionData get(ExecutionInfo info) {
        if (_info_map.containsKey(info)) return _info_map.get(info);
        return new EmptyExecutionData(info);
    }

    /**
     * Sets all probes in each NExecutionData in this store to false
     */
    public void reset() {
        for (NExecutionData data : _info_map.values()) {
            data.reset();
        }
    }

    /**
     * Adds the probes in the provided RExecutionData to this store.
     */
    public void add(RExecutionData other) {
        _info_map.get(other.getInfo()).add(other);
    }

    /**
     * Adds the probes in the provided NExecutionDataStore to this store.
     */
    public void add(NExecutionDataStore other) {
        for (RExecutionData data : other._info_map.values()) {
            add(data);
        }
    }

    /**
     * Subtracts the probes in the provided RExecutionData from this store.
     */
    public void sub(RExecutionData other) {
        ExecutionInfo info = other.getInfo();
        if (_info_map.containsKey(info)) {
            _info_map.get(info).sub(other);
        }
    }

    /**
     * Subtracts the probes in the provided NExecutionDataStore from this store.
     */
    public void sub(NExecutionDataStore other) {
        for (RExecutionData data : other._info_map.values()) {
            sub(data);
        }
    }

    /**
     * Intersects the probes in the provided RExecutionData with the ones in this
     * store.
     */
    public void intersect(RExecutionData other) {
        if (_info_map.containsKey(other.getInfo())) getMutable(other.getInfo()).intersect(other);
    }

    /**
     * Intersects the probes in the provided NExecutionDataStore with the ones in this
     * store.
     */
    public void intersect(NExecutionDataStore other) {
        for (RExecutionData data : other._info_map.values()) {
            intersect(data);
        }
    }

    /**
     * Writes the content of the store to the given visitor interface.
     */
    public void accept(IExecutionDataVisitor visitor) {
        for (NExecutionData data : _info_map.values()) {
            visitor.visitClassExecution(data.toExecutionData());
        }
    }

    @Override
    public void visitClassExecution(ExecutionData data) {
        put(NExecutionData.create(data));
    }
}
