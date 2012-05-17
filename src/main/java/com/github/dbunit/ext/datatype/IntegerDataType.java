/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.datatype;

import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 描述：
 * Created by JarnTang at 12-4-24 下午1:59
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class IntegerDataType extends AbstractDataType {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(org.dbunit.dataset.datatype.IntegerDataType.class);

    IntegerDataType(String name, int sqlType) {
        super(name, sqlType, Integer.class, true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // DataType class

    public Object typeCast(Object value) throws TypeCastException {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null || value == ITable.NO_VALUE) {
            return null;
        }

        if (value instanceof Number) {
            return new Integer(((Number) value).intValue());
        }

        // Bugfix in release 2.4.6
        String stringValue = value.toString().trim();
        if (stringValue.length() <= 0) {
            return null;
        }

        try {
            return typeCast(new BigDecimal(stringValue));
        } catch (NumberFormatException e) {
            throw new TypeCastException(value, this, e);
        }
    }

    public Object getSqlValue(int column, ResultSet resultSet)
            throws SQLException, TypeCastException {
        if (logger.isDebugEnabled())
            logger.debug("getSqlValue(column={}, resultSet={}) - start", new Integer(column), resultSet);

        int value = resultSet.getInt(column);
        if (resultSet.wasNull()) {
            return null;
        }
        return new Integer(value);
    }

    public void setSqlValue(Object value, int column, PreparedStatement statement)
            throws SQLException, TypeCastException {
        if (logger.isDebugEnabled())
            logger.debug("setSqlValue(value={}, column={}, statement={}) - start",
                    new Object[]{value, new Integer(column), statement});

        Integer integer = (Integer) typeCast(value);
        statement.setInt(column, integer == null ? 0 : integer.intValue());
    }
}