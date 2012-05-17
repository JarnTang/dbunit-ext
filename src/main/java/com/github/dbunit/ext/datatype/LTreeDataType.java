/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.datatype;

import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.StringDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 描述：
 * Created by JarnTang at 12-4-25 下午3:06
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class LTreeDataType extends AbstractDataType {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(StringDataType.class);

    public LTreeDataType(){
        super("ltree", Types.OTHER, LTree.class, false);
    }


    public LTreeDataType(String name, int sqlType, Class classType, boolean isNumber) {
        super(name, sqlType, classType, isNumber);
    }

    @Override
    public Object typeCast(Object value) throws TypeCastException {
        return null;
    }

    public void setSqlValue(Object value, int column, PreparedStatement statement)
            throws SQLException, TypeCastException {
        if (logger.isDebugEnabled())
            logger.debug("setSqlValue(value={}, column={}, statement={}) - start",
                    new Object[]{value, new Integer(column), statement});
        statement.setObject(column, new LTree(value));
    }


}
