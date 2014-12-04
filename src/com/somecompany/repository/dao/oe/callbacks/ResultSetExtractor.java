package com.somecompany.repository.dao.oe.callbacks;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {
	public T extractData(ResultSet rs) throws SQLException;
}
