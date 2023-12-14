package net.lyflow.skyblock.database.request;

import net.lyflow.skyblock.database.Database;

import java.sql.SQLException;

public class DefaultRequest {

    protected final Database database;
    protected final boolean autoClose;

    public DefaultRequest(Database database, boolean autoClose) {
        this.database = database;
        this.autoClose = autoClose;
    }

    protected void autoClose() throws SQLException {
        if (autoClose) database.closeConnection();
    }

}
