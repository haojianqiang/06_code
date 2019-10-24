package pub.dao.sql.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Columns implements Iterable<Column>{
    private List<Column> columns;

    public Columns(){
        this.columns = new ArrayList<Column>();
    }

    public Columns add(String columnSource){
        Column column = new Column();
        column.setSource(columnSource);
        this.columns.add(column);
        return this;
    }

    public Columns as(String columnAlias){

        return this;
    }

    public Iterator<Column> iterator() {
        return columns.iterator();
    }
}
