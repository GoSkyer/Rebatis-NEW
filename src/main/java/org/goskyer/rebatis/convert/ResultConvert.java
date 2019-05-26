package org.goskyer.rebatis.convert;

import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.ResultSet;
import com.github.jasync.sql.db.RowData;
import org.goskyer.rebatis.Result;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResultConvert {

    public static Result convert(QueryResult result) {
        return convertToListMap((int) result.getRowsAffected(), result.getRows());
    }

    public static Result convertToListMap(int rows, ResultSet set) {

        List<String> columnNames = set.columnNames();

        Result result = new Result();
        List<RowMap<String, Object>> listMap = new ArrayList<>(rows);
        result.setRaws(listMap);
        result.setEffectedRows(rows);

        // 当rows大于0并且result为空时，说明没有
        if (rows > 0 && set.size() == 0) {
            for (int i = 0; i < rows; i++) {
                listMap.add(null);
            }
            return result;
        }

        for (RowData row : set) {
            RowMap<String, Object> map = new RowMap<String, Object>(columnNames.size());
            for (String columnName : columnNames) {
                map.put(columnName, row.get(columnName));
            }
            listMap.add(map);
        }

        return result;
    }

}
