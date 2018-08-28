package org.dragberry.eshop.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.dragberry.eshop.service.DataImporter;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class TestDataImporter implements DataImporter {

    @Override
    public void importData(InputStream is) {
        log.info("Starting import...");
        try (Workbook wb = new HSSFWorkbook(is)) {
            wb.getSheetAt(0).rowIterator().forEachRemaining(row -> {
                log.info("Product " + row.getRowNum() + " " +  row.getCell(0));
            });
            
        } catch (IOException exc) {
            log.error("An error has ooccured during importing data", exc);
        }
        log.info("Finishing import...");
    }

}
