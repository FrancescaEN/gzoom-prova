package it.mapsgroup.gzoom.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class InterfacciamentoDatiService {

    private final DataSource dataSource;
    private final UserPreferenceService userPreferenceService;
    private final PartyService partyService;
    private static final Logger LOG = getLogger(QueryExecutorService.class);
    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    private final Map<String, String> TIPO_QUERY = new HashMap<String, String>() {{
        put("SELECT", "E");
        put("UPDATE", "A");
    }};

    public InterfacciamentoDatiService(@Qualifier("mainDataSource") DataSource mainDataSource, UserPreferenceService userPreferenceService, PartyService partyService) {
        this.dataSource = mainDataSource;
        this.userPreferenceService = userPreferenceService;
        this.partyService = partyService;
    }

    public static String[] addX(int n, String arr[], String x)
    {
        int i;
        String newarr[] = new String[n + 1];
        for (i = 0; i < n; i++)
            newarr[i] = arr[i];
        newarr[n] = x;
        return newarr;
    }

    @Transactional
    public boolean ExecuteInsertRowFile(MultipartFile file, String nomeFile) throws IOException, SQLException {

        String[] arrayQueryInsert = new String[0];
        String queryFinal = "DELETE FROM " + nomeFile.split("\\.")[0];
        StringBuilder queryInsert = new StringBuilder("INSERT INTO " + nomeFile.split("\\.")[0] + " ( ");
        StringBuilder values = new StringBuilder("VALUES ( ");
        Connection connection =  dataSource.getConnection();
        InputStream fis = file.getInputStream();
        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
        Iterator<Row> rowIterator = mySheet.iterator();
        Iterator<Row> rowIterator2 = mySheet.iterator();
        int cols = 0;
        if(rowIterator.hasNext()){
            Row row = rowIterator.next();
            rowIterator2.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();
                cols ++;
                switch (cell.getCellType()) {
                    case STRING:
                        queryInsert.append(cell.getStringCellValue());
                        if(cellIterator.hasNext()){
                            queryInsert.append(",");
                        }else{
                            queryInsert.append(") ");
                        }
                        break;
                    default:
                }
            }
            arrayQueryInsert = addX(arrayQueryInsert.length,arrayQueryInsert, queryInsert.toString());
        }

        for (int cn=0; cn < cols; cn++) {

            values.append("?");
            if(cn < cols -1){
                values.append(",");
            }
        }

        values.append(")");
        arrayQueryInsert = addX(arrayQueryInsert.length,arrayQueryInsert, values.toString());

        StringBuilder query = new StringBuilder();
        for (String item : arrayQueryInsert)
        {
            query.append(item);
        }

        PreparedStatement preparedStatementDelete = connection.prepareStatement(queryFinal);
        preparedStatementDelete.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(query));

        DataFormatter formatter = new DataFormatter();


        while (rowIterator2.hasNext()) {
            Row row = rowIterator2.next();
            if(!isRowEmpty(row)){
                for (int cn = 0; cn < cols; cn++) {

                    Cell cell = row.getCell(cn);

                    preparedStatement.setString(cn + 1, formatter.formatCellValue(cell));
                }

                preparedStatement.addBatch();
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
        }

        return true;
    }

    private boolean isRowEmpty(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() != CellType.BLANK) {
                return false; // La riga non è vuota
            }
        }
        return true; // La riga è vuota
    }
}


