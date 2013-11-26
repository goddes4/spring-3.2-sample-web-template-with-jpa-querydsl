package net.octacomm.sample.view;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ConnectStatsExcelView extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(
			Map<String, Object> model, HSSFWorkbook workbook,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		String userAgent = req.getHeader("User-Agent");
		String fileName = "사용자_접속통계.xls";

		if (userAgent.indexOf("MSIE") > -1) {
			fileName = URLEncoder.encode(fileName, "utf-8");
		} else {
			fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		}

		res.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\";");
		res.setHeader("Content-Transfer-Encoding", "binary");

		HSSFSheet sheet = createFirstSheet(workbook);
		createColumnLabel(sheet);
	}

	private HSSFSheet createFirstSheet(HSSFWorkbook workbook) {
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "접속통계");
		sheet.setColumnWidth(0, (int)(100 * 36.57));
		return sheet;
	}

	private void createColumnLabel(HSSFSheet sheet) {
		HSSFRow firstRow = sheet.createRow(0);

		setColumnLabels(firstRow, new String[]{"", "학교", "산업체", "연구원", "관공서", "언론기관", "기타", "합계"});
	}
	
	private void setColumnLabels(HSSFRow rows, String[] columnLabels){
		for (int i = 0; i < columnLabels.length; i++) {
			HSSFCell cell = rows.createCell(i);
			cell.setCellValue(columnLabels[i]);
		}
	}

}
