/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.datatype;

import com.github.dbunit.ext.util.PGHStore;
import com.github.dbunit.ext.util.StringUtil;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.StringDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * Created by JarnTang at 12-4-24 上午10:48
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class ArrayDataType extends AbstractDataType {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(StringDataType.class);

    public ArrayDataType() {
        this("ARRAY", 2003);
    }

    public ArrayDataType(String name, int sqlType) {
        super(name, sqlType, Array.class, false);
    }

    public Object typeCast(Object value) throws TypeCastException {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null || value == ITable.NO_VALUE) {
            return null;
        }

        if (value instanceof String) {
            return new String[]{(String) value};
        }
        if (value instanceof String[]) {
            return value;
        }

        if (value instanceof Date ||
                value instanceof Time ||
                value instanceof Timestamp) {
            return new String[]{value.toString()};
        }

        if (value instanceof Boolean) {
            return new String[]{value.toString()};
        }

        if (value instanceof Number) {
            try {
                return new String[]{value.toString()};
            } catch (NumberFormatException e) {
                throw new TypeCastException(value, this, e);
            }
        }

        if (value instanceof Array) {
            try {
                Array a = (Array) value;
                Object array = a.getArray();
                return array;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (value instanceof Blob) {
            try {
                Blob blob = (Blob) value;
                byte[] blobValue = blob.getBytes(1, (int) blob.length());
                return typeCast(blobValue);
            } catch (SQLException e) {
                throw new TypeCastException(value, this, e);
            }
        }

        if (value instanceof Clob) {
            try {
                Clob clobValue = (Clob) value;
                int length = (int) clobValue.length();
                if (length > 0) {
                    return clobValue.getSubString(1, length);
                }
                return "";
            } catch (SQLException e) {
                throw new TypeCastException(value, this, e);
            }
        }

        logger.warn("Unknown/unsupported object type '{}' - " +
                "will invoke toString() as last fallback which " +
                "might produce undesired results",
                value.getClass().getName());
        return value.toString();
    }

    public Object getSqlValue(int column, ResultSet resultSet)
            throws SQLException, TypeCastException {
        if (logger.isDebugEnabled())
            logger.debug("getSqlValue(column={}, resultSet={}) - start", column, resultSet);

        String value = resultSet.getString(column);
        if (value == null || resultSet.wasNull()) {
            return null;
        }
        return value;
    }

    public void setSqlValue(Object value, int column, PreparedStatement statement)
            throws SQLException, TypeCastException {
        if (logger.isDebugEnabled())
            logger.debug("setSqlValue(value={}, column={}, statement={}) - start",
                    new Object[]{value, column, statement});

        Array array = statement.getConnection().createArrayOf("varchar", toArray(value));
        Map map = new HashMap();
        map.put("", null);
        statement.setObject(column, new PGHStore(map));
        statement.setArray(column, array);
    }


    private Object[] toArray(Object value) {
        List list = new ArrayList(0);
        if (value instanceof String) {
            String valueStr = (String) value;
            if (StringUtil.isNotBlank(valueStr)) {
                valueStr = valueStr.replaceAll("[{}]", "");
                return valueStr.split(",");
            }
        }
        return list.toArray();

    }

}
