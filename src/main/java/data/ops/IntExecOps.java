package data.ops;

import data.ExecutionInfo;
import data.IntExecutionData;

import java.util.Collection;

/**
 * Created by nkalonia1 on 12/20/16.
 */
public class IntExecOps {

    public static IntExecutionData merge(ExecutionInfo info, Collection<IntExecutionData> data) {
        IntExecutionData out = new IntExecutionData(info);
        for (IntExecutionData d : data) {
            out.add(d);
        }
        return out;
    }
}
