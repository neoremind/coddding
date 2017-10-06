// Command: thrift  -r -gen java ./thrift-service.thrift

namespace cpp mycode
namespace java net.neoremind.mycode.thrift.codegen

enum QueryState {
  OK = 1,
  ERROR = 2
}

struct QueryRequest {
 1: required i64 id,
 2: required string name;
 3: optional list<string> optList,
 4: optional bool quiet = false
}

struct QueryResponse {
 1: required string queryId,
 2: required QueryState state = QueryState.OK,
 3: optional string message
}

service MyQueryService {
 string echo(1: string msg),
 i32 echoInt(1: i32 id),
 void helloVoid(),
 QueryResponse submitQuery(1: QueryRequest query)
}