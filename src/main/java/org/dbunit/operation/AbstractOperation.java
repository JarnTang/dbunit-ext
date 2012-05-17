/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2004, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.dbunit.operation;

import com.github.dbunit.ext.operation.KeyWordsFilter;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.util.QualifiedTableName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Laflamme
 * @version $Revision: 953 $
 * @since Jan 17, 2004
 */
public abstract class AbstractOperation extends DatabaseOperation {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(org.dbunit.operation.AbstractOperation.class);

    protected String getQualifiedName(String prefix, String name, IDatabaseConnection connection) {
        if (logger.isDebugEnabled()) {
            logger.debug("getQualifiedName(prefix={}, name={}, connection={}) - start",
                    new Object[]{prefix, name, connection});
        }
        String escapePattern = (String) connection.getConfig().getProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN);
        QualifiedTableName qualifiedTableName = new QualifiedTableName(name, prefix, escapePattern);
        String columnName = qualifiedTableName.getQualifiedName();
        if (KeyWordsFilter.isKeyWords(columnName)) {
            columnName = "\"" + columnName + "\"";
        }
        return columnName;
    }

    /**
     * Returns the metadata to use in this operation. It is retrieved
     * from the database connection using the information from the physical
     * database table.
     *
     * @param connection the database connection
     * @param metaData   the XML table metadata
     */
    static ITableMetaData getOperationMetaData(IDatabaseConnection connection,
                                               ITableMetaData metaData) throws DatabaseUnitException, SQLException {
        logger.debug("getOperationMetaData(connection={}, metaData={}) - start", connection, metaData);

        IDataSet databaseDataSet = connection.createDataSet();
        String tableName = metaData.getTableName();

        ITableMetaData tableMetaData = databaseDataSet.getTableMetaData(tableName);
        Column[] columns = metaData.getColumns();

        List<Column> columnList = new ArrayList<Column>();
        for (Column column : columns) {
            String columnName = column.getColumnName();
            // Check if column exists in database
            // method "getColumnIndex()" throws NoSuchColumnsException when columns have not been found
            int dbColIndex = tableMetaData.getColumnIndex(columnName);
            // If we get here the column exists in the database
            Column dbColumn = tableMetaData.getColumns()[dbColIndex];
            columnList.add(dbColumn);
        }

        return new DefaultTableMetaData(tableMetaData.getTableName(),columnList.toArray(new Column[columnList.size()]),
                tableMetaData.getPrimaryKeys());
    }
}
