/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.datatype;

import com.github.dbunit.ext.util.PGHStore;
import com.github.dbunit.ext.util.StringUtil;
import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.StringDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 * Created by JarnTang at 12-4-24 上午11:57
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class HStoreDataType extends AbstractDataType {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(StringDataType.class);

    public HStoreDataType(){
        super("hstore", Types.OTHER, HStoreDataType.class, false);
    }

    public HStoreDataType(String name, int sqlType) {
        super(name, sqlType, PGHStore.class, false);
    }

    public Object typeCast(Object value) throws TypeCastException {
        logger.debug("typeCast(value={}) - start", value);

        return value;
    }

    public Object getSqlValue(int column, ResultSet resultSet)
            throws SQLException, TypeCastException {
        if (logger.isDebugEnabled())
            logger.debug("getSqlValue(column={}, resultSet={}) - start", new Integer(column), resultSet);

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
                    new Object[]{value, new Integer(column), statement});
        statement.setObject(column, new PGHStore(toMap(value)));
    }


    private Map toMap(Object value) {
        Map map = new HashMap<String, String>();
        if (value instanceof String) {
            String valueStr = (String) value;
            if (StringUtil.isNotBlank(valueStr)) {
                valueStr = valueStr.replaceAll("=>", ":");
                valueStr = valueStr.replaceAll("->", ":");
                valueStr = valueStr.replaceAll("=", ":");
                valueStr = valueStr.replaceAll("[{}\"]", "");
                String[] split = valueStr.split(",");
                if (split != null && split.length > 0) {
                    for (String str : split) {
                        String[] keyValue = str.split(":");
                        if (keyValue.length == 1) {
                            map.put(keyValue[0],null);
                        }else if (keyValue.length == 2) {
                            map.put(keyValue[0], keyValue[1]);
                        }
                    }
                }
            }
        }
        return map;
    }

}
