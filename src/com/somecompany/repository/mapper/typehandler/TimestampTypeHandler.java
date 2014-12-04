package com.somecompany.repository.mapper.typehandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.SqlTimestampTypeHandler;

@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class TimestampTypeHandler extends SqlTimestampTypeHandler {

	@Override
	public Timestamp getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		String timeZoneId = "America/LA";
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(timeZone);
		
		return rs.getTimestamp(columnName, cal);
	}
}
