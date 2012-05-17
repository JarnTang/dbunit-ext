/*
* $$Id$$
* Copyright (c) 2011 Qunar.com. All Rights Reserved.
*/
package com.github.dbunit.ext.datatype;

import org.postgresql.util.PGobject;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * 描述：
 * Created by JarnTang at 12-4-25 下午3:11
 *
 * @author <a href="mailto:changjiang.tang@qunar.com">JarnTang</a>
 */
public class LTree extends PGobject implements Serializable, Cloneable {

    private static final long serialVersionUID = -2365829713619117075L;

    String value;

    public LTree() {
        setType("ltree");
    }

    /**
     * Initialize a hstore with a given string representation
     *
     * @param value String representated hstore
     * @throws java.sql.SQLException Is thrown if the string representation has an unknown format
     * @see #setValue(String)
     */
    public LTree(Object value)
            throws SQLException {
        this();
        this.value = value.toString();
    }

    /**
     */
    public void setValue(String value)
            throws SQLException {
        this.value = value;
    }

    /**  'value'::ltree
     * Returns the stored information as a string
     *
     * @return String represented hstore
     */
    public String getValue() {
        return this.value;
    }

    private static void writeValue(StringBuffer buf, Object o) {
        if (o == null) {
            buf.append("NULL");
            return;
        }
        String s = o.toString();
        buf.append('\'');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\\') {
                buf.append('\\');
            }
            buf.append(c);
        }
        buf.append('\'');
        buf.append("::ltree");
    }

}
