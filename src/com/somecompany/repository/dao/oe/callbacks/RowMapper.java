package com.somecompany.repository.dao.oe.callbacks;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
	T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
