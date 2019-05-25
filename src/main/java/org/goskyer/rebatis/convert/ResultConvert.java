package org.goskyer.rebatis.convert;

import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.ResultSet;
import com.github.jasync.sql.db.RowData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResultConvert {

    public static List<RowMap<String,Object>> convert(QueryResult result) {
        return convertToListMap((int) result.getRowsAffected(), result.getRows());
    }

    public static List<RowMap<String,Object>> convertToListMap(int rows, ResultSet result) {

        List<String> columnNames = result.columnNames();

        List<RowMap<String,Object>> listMap = new ArrayList<>(rows);

        for (RowData row : result) {
            RowMap<String, Object> map = new RowMap<String, Object>(columnNames.size());
            for (String columnName : columnNames) {
                map.put(columnName, row.get(columnName));
            }
            listMap.add(map);
        }

        return listMap;
    }

}
