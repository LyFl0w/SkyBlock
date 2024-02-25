package net.lyflow.skyblock.database.request;

import net.lyflow.skyblock.database.Database;

import java.sql.SQLException;

public class DefaultRequest {

    protected final Database database;
    protected final boolean isAutoClose;

    public DefaultRequest(Database database, boolean isAutoClose) {
        this.database = database;
        this.isAutoClose = isAutoClose;
    }

    protected void autoClose() throws SQLException {
        if (isAutoClose) database.closeConnection();
    }

}
