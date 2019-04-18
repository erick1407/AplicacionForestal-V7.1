package com.example.erick.aplicacionforestal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TemplatePDFNewton {
    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.HELVETICA,20, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.HELVETICA,18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.HELVETICA,12, Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.HELVETICA,15, Font.BOLD, BaseColor.RED);
    public TemplatePDFNewton(Context context) {
        this.context= context;
    }

    public void openDocument(){
        createFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e){
            Log.e("openDocument",e.toString());
        }
    }

    private void createFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDFCubicacion");
        if (!folder.exists())
            folder.mkdir();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dataTime = sdf.format(new Date());
        pdfFile = new File(folder,"ResNewton"+dataTime+".pdf");
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String title, String subjet){
        document.addTitle(title);
        document.addSubject(subjet);
    }

    public void addTitles(String title, String subtitle, String date, String time){
        try {
            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subtitle, fSubTitle));
            addChildP(new Paragraph("Generado: "+ date + " Hora: " + time, fHighText));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        } catch (Exception e){
            Log.e("addTitles", e.toString());
        }
    }

    private void addChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text){
        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void addParagraphResul(String text){
        try {
            paragraph = new Paragraph(text);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraphResul", e.toString());
        }
    }

    public void addParagraphResu(String text){
        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(160);
            paragraph.setSpacingBefore(50);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraphResu", e.toString());
        }
    }

    public void addParagraphCenter(String text){
        try {
            paragraph = new Paragraph(text, fText);
            //paragraph.setSpacingAfter(5);
            //paragraph.setSpacingBefore(5);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraphCenter", e.toString());
        }
    }

    public void addImage(){
        try {
            Drawable drawable = context.getResources().getDrawable(R.drawable.memnuevo);
            BitmapDrawable bitDw = ((BitmapDrawable) drawable);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAbsolutePosition(25f,750f);
            image.scaleToFit(550,78);
            document.add(image);
        } catch (Exception e) {
            Log.e("addImage", e.toString());
        }
    }

    public void createTable(String[]header, ArrayList<String[]> clients){
        try {
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(20);
            PdfPCell pdfPCell;
            int indexC = 0;
            while (indexC<header.length){
                pdfPCell = new PdfPCell(new Phrase(header[indexC++],fSubTitle));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setBackgroundColor(BaseColor.GREEN);
                pdfPTable.addCell(pdfPCell);
            }

            for (int indexR=0;indexR<clients.size();indexR++){
                String[]row= clients.get(indexR);
                for (indexC=0; indexC<header.length;indexC++){
                    pdfPCell = new PdfPCell(new Phrase(row[indexC]));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setFixedHeight(40);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e){
            Log.e("createTable ", e.toString());
        }
    }

    public void viewPDF(){
        Intent intent = new Intent(context, ViewPDFNewtonActivity.class);
        intent.putExtra("path",pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
