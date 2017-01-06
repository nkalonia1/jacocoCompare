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

    public static IntExecutionDataStore intersect(IntExecutionDataStore a, IntExecutionDataStore b) {
        IntExecutionDataStore out = null;
        try {
            out = (IntExecutionDataStore) a.clone();
            out.intersect(b);
        } catch (CloneNotSupportedException cnse) {
            System.err.println(cnse);
        }
        return out;
    }

    public static IntExecutionDataStore filter(IntExecutionDataStore a, IntExecutionDataStore b) {
        IntExecutionDataStore out = null;
        try {
            out = (IntExecutionDataStore) a.clone();
            out.filter(b);
        } catch (CloneNotSupportedException cnse) {
            System.err.println(cnse);
        }
        return out;
    }
}
