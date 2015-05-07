package com.photofall.rest.service;

import com.datastax.driver.core.DataType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;

import org.owasp.esapi.ESAPI;

/**
 * This utility will convert a database data into JSON format.
 * Note:  this java class requires the ESAPI 1.4.4 jar file
 * ESAPI is used to encode data
 * 
 * @author 308tube
 */
public class ToJson {

	/**
	 * This will convert database records into a JSON Array
	 * Simply pass in a ResultSet from a database connection and it
	 * loop return a JSON array.
	 * 
	 * It important to check to make sure that all DataType that are
	 * being used is properly encoding.
	 * 
	 * varchar is currently the only dataType that is being encode by ESAPI
	 * 
	 * @param rs - database ResultSet
	 * @return - JSON array
	 * @throws Exception
	 */
	public JSONArray toJSONArray(ResultSet rs) throws Exception {

        JSONArray json = new JSONArray(); //JSON array that will be returned
        String temp = null;

        try {

        	 //we will need the column names, this will save the table meta-data like column nmae.
        	 
             
             //loop through the ResultSet
        	for(Row r:rs.all()){
            	 ColumnDefinitions rsmd = r.getColumnDefinitions();
                 //each row in the ResultSet will be converted to a JSON Object
                 JSONObject obj = new JSONObject();
                 
                 //loop through all the columns and place them into the JSON Object
                 int i=0;
                 for(int i=1; i<numColumns+1; i++) {

                     String column_name = rsmd.getName(i);

                     if(rsmd.getType(i).getName()==DataType.Name.LIST){
                    	 obj.put(column_name, rs.getArray(column_name));
                    	 /*Debug*/ System.out.println("ToJson: ARRAY");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.BIGINT){
                    	 obj.put(column_name, rs.getInt(column_name));
                    	 /*Debug*/ System.out.println("ToJson: BIGINT");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.BOOLEAN){
                    	 obj.put(column_name, rs.getBoolean(column_name));
                    	 /*Debug*/ System.out.println("ToJson: BOOLEAN");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.BLOB){
                    	 obj.put(column_name, rs.getBlob(column_name));
                    	 /*Debug*/ System.out.println("ToJson: BLOB");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.DOUBLE){
                    	 obj.put(column_name, rs.getDouble(column_name)); 
                    	 /*Debug*/ System.out.println("ToJson: DOUBLE");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.FLOAT){
                    	 obj.put(column_name, rs.getFloat(column_name));
                    	 /*Debug*/ System.out.println("ToJson: FLOAT");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.INTEGER){
                    	 obj.put(column_name, rs.getInt(column_name));
                    	 /*Debug*/ System.out.println("ToJson: INTEGER");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.NVARCHAR){
                    	 obj.put(column_name, rs.getNString(column_name));
                    	 /*Debug*/ System.out.println("ToJson: NVARCHAR");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.VARCHAR){
                    	 
                    	 temp = rs.getString(column_name); //saving column data to temp variable
                    	 temp = ESAPI.encoder().canonicalize(temp); //decoding data to base state
                    	 temp = ESAPI.encoder().encodeForHTML(temp); //encoding to be browser safe
                    	 obj.put(column_name, temp); //putting data into JSON object
                    	 
                    	 //obj.put(column_name, rs.getString(column_name));
                    	 // /*Debug*/ System.out.println("ToJson: VARCHAR");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.TINYINT){
                    	 obj.put(column_name, rs.getInt(column_name));
                    	 /*Debug*/ System.out.println("ToJson: TINYINT");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.SMALLINT){
                    	 obj.put(column_name, rs.getInt(column_name));
                    	 /*Debug*/ System.out.println("ToJson: SMALLINT");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.DATE){
                    	 obj.put(column_name, rs.getDate(column_name));
                    	 /*Debug*/ System.out.println("ToJson: DATE");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.TIMESTAMP){
                    	 obj.put(column_name, rs.getTimestamp(column_name));
                    	 /*Debug*/ System.out.println("ToJson: TIMESTAMP");
                     }
                     else if(rsmd.getType(i).getName()==DataType.Name.NUMERIC){
                    	 obj.put(column_name, rs.getBigDecimal(column_name));
                    	 // /*Debug*/ System.out.println("ToJson: NUMERIC");
                      }
                     else {
                    	 obj.put(column_name, rs.getObject(column_name));
                    	 /*Debug*/ System.out.println("ToJson: Object "+column_name);
                     } 
                 		i++;
                    }//end foreach
                 
                 json.put(obj);

             }//end while

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json; //return JSON array
	}
}
