package com.photofall.rest.store;

import com.datastax.driver.core.*;
import com.photofall.rest.model.Cache;
import com.photofall.rest.utils.ToJson;
import org.codehaus.jettison.json.JSONArray;

import java.nio.ByteBuffer;
import java.sql.SQLException;

public class GeoStore
{
	private Cluster cluster;
	private Session session;

	String table="caches";
	
	public GeoStore() { 
		cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
		cluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(100000);
		session = cluster.connect();
		session.execute("USE photofall;");
	}
     
    public String populateData(String cacheId, String userId, int expiration,ByteBuffer xml) throws SQLException {
		
	    PreparedStatement ps = session.prepare("insert into "+table+" (cacheid, userid, expiration, message, photo) values (?,?,?,?,?);");
	    BoundStatement boundStatement = new BoundStatement(ps);
	    ResultSet rs =session.execute(boundStatement.bind( cacheId, userId, expiration, "BeboStunazxoxoxo",xml));
	    return toJSON(rs);
    }
    public String deleteCache(String cacheId, String userId) {
        PreparedStatement ps = session.prepare("delete from "+table+" where cacheid=? and userid=?;");
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs =session.execute(boundStatement.bind(cacheId, userId));
        return "Success "+ cacheId + "/" + userId;
    }
	public void deleteData(Cache cache) throws SQLException {
		PreparedStatement ps = session.prepare(
				"delete from "+table+" where cacheid=? and userid=?;"
				);
	    BoundStatement boundStatement = new BoundStatement(ps);
	    session.execute(boundStatement.bind( cache.getCacheId(), cache.getUserId()));
	}
	public void updateData() throws SQLException {
        String t = "update " + table + " set category='sports', linkcounts=1 where key='user5'";
        session.execute(t);
    }
    public int getRows(){
        PreparedStatement ps = session.prepare(
                "SELECT * FROM "+table+";"
        );
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs =session.execute(boundStatement);
        return rs.all().size();
    }
	public Cache getFirst() throws SQLException {

        PreparedStatement ps = session.prepare(
				"SELECT * FROM "+table+" LIMIT 1;"
				);
	    BoundStatement boundStatement = new BoundStatement(ps);
	    ResultSet rs =session.execute(boundStatement);
        System.out.println("Attempting to take out first cache");
        Row r = rs.one();
	    return(new Cache(r.getString("userId"),r.getString("cacheId"),r.getInt("expiration"),r.getString("message"),r.getBytes("photo")));
	}
	public String getData(String cacheId, String userId) throws SQLException {
	    String t = "SELECT * FROM "+table+" WHERE userid='"+userId+"' and cacheid='"+cacheId+"'";
	    ResultSet rs =session.execute(t);
	    return(toJSON(rs));
	}
	public void close(){
		session.close();
		cluster.close();
	}
	public String toJSON(ResultSet rs){
	    ToJson converter = new ToJson();
	    JSONArray json;
	    json = converter.toJSONArray(rs);
	    return(json.toString());
	}

}