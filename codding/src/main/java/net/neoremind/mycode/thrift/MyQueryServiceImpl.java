package net.neoremind.mycode.thrift;

import com.google.common.base.Joiner;
import net.neoremind.mycode.thrift.codegen.MyQueryService;
import net.neoremind.mycode.thrift.codegen.QueryRequest;
import net.neoremind.mycode.thrift.codegen.QueryResponse;
import net.neoremind.mycode.thrift.codegen.QueryState;
import org.apache.thrift.TException;

/**
 * @author xu.zhang
 */
public class MyQueryServiceImpl implements MyQueryService.Iface {

    @Override
    public String echo(String msg) throws TException {
        return msg;
    }

    @Override
    public int echoInt(int id) throws TException {
        return 100;
    }

    @Override
    public void helloVoid() throws TException {
        System.out.println("This is helloVoid");
    }

    @Override
    public QueryResponse submitQuery(QueryRequest query) throws TException {
        QueryResponse response = new QueryResponse();
        if (!query.isQuiet()) {
            System.out.println(query);
        }
        response.setQueryId(String.valueOf(query.getId()));
        response.setMessage(Joiner.on(",").join(query.getOptList()));
        response.setState(QueryState.OK);
        return response;
    }
}
