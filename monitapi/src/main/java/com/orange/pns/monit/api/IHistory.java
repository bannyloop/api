package com.orange.pns.monit.api;


import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/monit/v1/history")
@Api(value="Capacity History")
@Produces(MediaType.APPLICATION_JSON)
public interface IHistory {
	
	@GET
	@ApiOperation(value="Get historique capacity", response = Response.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get cassandra capacity ok"),
    @ApiResponse(code = 400, message = "Bad Request"),
    @ApiResponse(code = 404, message = "Resource not Found"),
    @ApiResponse(code = 500, message = "Internal Server Error")})
    Response getHistory(@QueryParam("starttime") String starttime,@QueryParam("endtime") String endtime) throws IOException;
}
