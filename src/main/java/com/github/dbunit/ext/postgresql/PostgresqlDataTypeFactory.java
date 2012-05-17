/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.postgresql;

import com.github.dbunit.ext.datatype.ArrayDataType;
import com.github.dbunit.ext.datatype.HStoreDataType;
import com.github.dbunit.ext.datatype.LTreeDataType;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.ext.postgresql.GenericEnumType;
import org.dbunit.ext.postgresql.InetType;
import org.dbunit.ext.postgresql.IntervalType;
import org.dbunit.ext.postgresql.UuidType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 描述：
 * Created by JarnTang at 12-4-23 下午5:29
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class PostgresqlDataTypeFactory extends DefaultDataTypeFactory {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(org.dbunit.ext.postgresql.PostgresqlDataTypeFactory.class);
    /**
     * Database product names supported.
     */
    private static final Collection DATABASE_PRODUCTS = Arrays.asList(new String[]{"PostgreSQL"});

    /**
     * @see org.dbunit.dataset.datatype.IDbProductRelatable#getValidDbProducts()
     */
    public Collection getValidDbProducts() {
        return DATABASE_PRODUCTS;
    }

    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        logger.debug("createDataType(sqlType={}, sqlTypeName={})", String.valueOf(sqlType), sqlTypeName);

        if (sqlType == Types.OTHER)
            if ("uuid".equals(sqlTypeName))
                return new UuidType();
            else if ("interval".equals(sqlTypeName))
                return new IntervalType();
            else if ("inet".equals(sqlTypeName))
                return new InetType();
            else if ("hstore".equals(sqlTypeName)) {
                return new HStoreDataType();
            } else if ("ltree".equals(sqlTypeName)) {
                return new LTreeDataType();
            }else if("ARRAY".equals(sqlTypeName)){
                return new ArrayDataType();
            } else {
                if (isEnumType(sqlTypeName)) {
                    if (logger.isDebugEnabled())
                        logger.debug("Custom enum type used for sqlTypeName {} (sqlType '{}')",
                                new Object[]{sqlTypeName, new Integer(sqlType)});
                    return new GenericEnumType(sqlTypeName);
                }
            }
        return super.createDataType(sqlType, sqlTypeName);
    }

    /**
     * Returns a data type for the given sql type name if the user wishes one.
     * <b>Designed to be overridden by custom implementations extending this class.</b>
     * Override this method if you have a custom enum type in the database and want
     * to map it via dbunit.
     *
     * @param sqlTypeName The sql type name for which users can specify a custom data type.
     * @return <code>null</code> if the given type name is not a custom
     *         type which is the default implementation.
     * @since 2.4.6
     */
    public boolean isEnumType(String sqlTypeName) {
        if (enumKeyWordList.contains(sqlTypeName)) {
            return true;
        }
        return false;
    }

    final List<String> enumKeyWordList = Arrays.asList("linkage_status", "check_status", "hstore");

}