package edu.ucalgary.oop;

import java.sql.SQLException;

public class MockDatabase implements DatabaseQueryHandler {
    public MockDatabase() {
        
    }

    /**
     * Returns the result of a query with a return
     * @param query
     * @param data
     * @param types
     * @return a formatted string
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public String returnQuery(String query, String[] data, String[] types) throws SQLException, IllegalArgumentException {
        if(query.matches("^SELECT.*FROM.*$")) {
            if(query.matches("^.*SupplyAllocation.*$")) {
                return "1<\t>1<\t>null<\t>2025-04-09 07:18:28.626915<\t>\n" + //
                                        "2<\t>1<\t>null<\t>2025-04-09 07:18:28.626915<\t>\n" + //
                                        "3<\t>1<\t>null<\t>2025-04-09 07:18:28.626915<\t>\n" + //
                                        "4<\t>1<\t>null<\t>2025-04-09 07:18:28.626915<\t>\n" + //
                                        "5<\t>null<\t>1<\t>2025-04-09 07:18:28.626915<\t>\n" + //
                                        "6<\t>null<\t>1<\t>2025-04-09 07:18:28.626915<\t>\n" + //
                                        "7<\t>null<\t>2<\t>2025-04-09 07:18:28.626915<\t>\n";
            }else if (query.matches("^.*PersonLocation.*$")) {
                return "1<\t>1<\t>\n" + //
                                        "2<\t>2<\t>\n" + //
                                        "5<\t>2<\t>";
            }else if (query.matches("^.*Person.*$")) {
                return "1<\t>Aurélie<\t>Dupont<\t>1985-03-15<\t>Non-binary person<\t>Speaks only French<\t>null<\t>1<\t>\n" + //
                                        "2<\t>Raman<\t>Narayan<\t>1980-06-22<\t>Man<\t>null<\t>null<\t>2<\t>\n" + //
                                        "3<\t>Chinoso<\t>Nwosu<\t>null<\t>null<\t>null<\t>555-9876<\t>null<\t>\n" + //
                                        "4<\t>Chanida<\t>Chaiyapong<\t>null<\t>null<\t>null<\t>555-6543<\t>null<\t>\n" + //
                                        "5<\t>Nathalie<\t>Dupont-Nwosu<\t>2024-12-01<\t>Woman<\t>null<\t>null<\t>1<\t>\n";
            }else if (query.matches("^.*Supply.*$")) {
                return "1<\t>water<\t>null<\t>\n" + //
                                        "2<\t>blanket<\t>null<\t>\n" + //
                                        "3<\t>cot<\t>410 G16<\t>\n" + //
                                        "4<\t>personal item<\t>green leather suitcase<\t>\n" + //
                                        "5<\t>water<\t>null<\t>\n" + //
                                        "6<\t>water<\t>null<\t>\n" + //
                                        "7<\t>water<\t>null<\t>\n";
            }else if(query.matches("^.*Location.*$")) {
                return "1<\t>TELUS<\t>136 8 Ave SE<\t>\n" + //
                        "2<\t>University of Calgary<\t>2500 University Dr NW<\t>\n";
            }else if (query.matches("^.*MedicalRecord.*$")) {
                return "1<\t>1<\t>1<\t>2025-01-05 00:00:00<\t>Broken arm<\t>\n" + //
                                        "2<\t>2<\t>5<\t>2025-01-06 00:00:00<\t>Dehydration<\t>\n";
            }else if (query.matches("^.*Inquiry.*$")) {
                return "1<\t>3<\t>1<\t>1<\t>2025-01-01 00:00:00<\t>Seeking spouse<\t>\n" + //
                                        "2<\t>4<\t>1<\t>1<\t>2025-01-02 00:00:00<\t>Checking on neighbor<\t>\n" + //
                                        "3<\t>1<\t>5<\t>1<\t>2025-01-02 00:00:00<\t>Daughter missing<\t>\n";
            }else{
                throw new SQLException(query + ": table not found");
            }
        }else{
            throw new SQLException(query + ": query should be a select query");
        }
    }

    /**
     * Returns the result of a query with a return (ie select queries)
     * @param Query
     * @param data
     * @param types
     * @return the number of rows affected (assume 1)
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public int deadEndQuery(String Query, String[] data, String[] types) throws SQLException, IllegalArgumentException {
        if(data == null || types == null) {
            return 1;
        }
        if(data.length != types.length) {
            throw new SQLException("Data and types length do not match");
        }
        return 1;
    }

    /**
     * override get entries to use moack db
     * @param tableName
     * @return
     * @throws SQLException
     */
    @Override
    public String getEntries(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        String results = returnQuery(query, new String[]{}, new String[]{});
        return results;
    }

    /**
     * override get entries to use mock db
     * @param tableName
     * @param order
     * @return
     * @throws SQLException
     */
    @Override
    public String getEntries(String tableName, String order) throws SQLException {
        String query = "SELECT * FROM " + tableName + " ORDER BY " + order + " ASC";
        String results = returnQuery(query, new String[]{}, new String[]{});
        return results;
    }

}
