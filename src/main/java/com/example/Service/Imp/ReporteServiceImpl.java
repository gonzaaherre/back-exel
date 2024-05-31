package com.example.Service.Imp;

import com.example.Entity.Pedido;
import com.example.Entity.PedidoDetalle;
import com.example.Service.PedidoService;
import com.example.Service.ReporteService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private PedidoService pedidoService;

    @Override
    public byte[] generarReporteExcel(Date fechaDesde, Date fechaHasta) throws IOException {
        List<Pedido> pedidos = pedidoService.findByFecha(fechaDesde, fechaHasta);

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Pedidos");

        // Crear encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Fecha Pedido", "Instrumento", "Marca", "Modelo", "Cantidad", "Precio", "Subtotal"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Llenar datos
        int rowNum = 1;
        for (Pedido pedido : pedidos) {
            for (PedidoDetalle detalle : pedido.getPedidosDetalle()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(pedido.getFechaPedido()));
                row.createCell(1).setCellValue(detalle.getInstrumento().getInstrumento());
                row.createCell(2).setCellValue(detalle.getInstrumento().getMarca());
                row.createCell(3).setCellValue(detalle.getInstrumento().getModelo());
                row.createCell(4).setCellValue(detalle.getCantidad());
                row.createCell(5).setCellValue(detalle.getInstrumento().getPrecio());
                row.createCell(6).setCellValue(detalle.getCantidad() * detalle.getInstrumento().getPrecio());
            }
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }
    }

