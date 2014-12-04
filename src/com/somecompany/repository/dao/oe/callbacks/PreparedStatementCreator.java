package com.somecompany.repository.dao.oe.callbacks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCreator {
	public PreparedStatement createPreparedStatement(Connection c) throws SQLException;
}
