package data;

import java.util.Collection;

/**
 * Read-Only ExecutionDataStore interface.
 */
public interface RExecutionDataStore {

    boolean contains(String name);

    boolean contains(long id);

    boolean contains(ExecutionInfo info);

    Collection<? extends RExecutionData> getContents();

    /**
     * Returns an array of all RExecutionData that contain the parameter as an id, in
     * no specific order. This function returns an array since it is technically
     * possible (though extremely unlikely) for two different RExecutionData to have
     * the same id.
     */
    RExecutionData[] get(long id);

    /**
     * Returns the RExecutionData corresponding to the provided ExecutionInfo in this
     * RExecutionDataStore. If no such RExecutionData exists, then an empty one is
     * returned.
     */
    RExecutionData get(ExecutionInfo info);
}
