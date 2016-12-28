package data.ops;

import data.ExecutionInfo;
import data.IntExecutionData;
import data.IntExecutionDataStore;

import java.util.Collection;

/**
 * Container class for static, non-destructive operations on IntExecutionData and IntExecutionDataStore
 */
public class IntExecOps {

    /**
     * Merges a collection of IntExecutionData. The ExecutionInfo must be manually provided for the cases of:
     *     a) All of the IntExecutionData in the collection are null
     *     b) The collection contains IntExecutionData with different ExecutionInfo
     * If an incompatible IntExecutionData is found in the collection, it is simply skipped.
     */
    public static IntExecutionData merge(ExecutionInfo info, Collection<IntExecutionData> data) {
        IntExecutionData out = new IntExecutionData(info);
        for (IntExecutionData d : data) {
            try {
                out.add(d);
            } catch (IllegalStateException ise) {
                System.err.println(ise);
            }
        }
        return out;
    }

    /**
     * Merges a collection of IntExecutionDataStores
     */
    public static IntExecutionDataStore merge(Collection<IntExecutionDataStore> stores) {
        IntExecutionDataStore out = new IntExecutionDataStore();
        for ( IntExecutionDataStore store : stores) {
            out.put(store);
        }
        return out;
    }

    /**
     * Compares two IntExecutionData. Returns a new IntExecutionData that represents the intersection of the data
     * from the two objects.
     * To be more specific the output contains the minumum value of any probe in the two IntExecutionData.
     */
    //public static IntExecutionData compare()
}
